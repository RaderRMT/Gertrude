package fr.rader.gertrude.commands;

import fr.rader.gertrude.annotations.Param;
import fr.rader.gertrude.annotations.SlashCommand;
import fr.rader.gertrude.lexer.Scanner;
import fr.rader.gertrude.lexer.tokens.Token;
import fr.rader.gertrude.lexer.tokens.TokenKind;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that holds a list of all the methods we have and can call.
 * This is used as a builder to turn methods with the {@link SlashCommand} annotation
 * to {@link DiscordSlashCommand} objects, and later {@link SlashCommandData} before sending them to discord
 */
public final class CommandRegistry {

    private static CommandRegistry instance;

    private final List<DiscordSlashCommand> commands;
    private final List<CommandMethod> commandMethods;

    private Scanner scanner;

    private String subcommandName;
    private String subcommandGroupName;

    private CommandRegistry() {
        this.commands = new ArrayList<>();
        this.commandMethods = new ArrayList<>();
    }

    /**
     * Register all the methods annotated with the {@link SlashCommand} annotation in the given {@link Command} class
     *
     * @param command   The class to register the commands from
     */
    public void registerCommandClass(@NotNull final Command command) {
        Checks.notNull(command, "command");

        for (Method method : command.getCommandMethods()) {
            addCommand(command, method);
        }
    }

    /**
     * Build the given command method to the internal command list to be added to Discord later on
     *
     * @param commandInstance   The command instance that owns the method
     * @param method            The command method to execute
     */
    private void addCommand(@NotNull final Command commandInstance, @NotNull final Method method) {
        SlashCommand slashCommand = method.getAnnotation(SlashCommand.class);
        this.scanner = new Scanner(slashCommand.command());

        this.subcommandName = null;
        this.subcommandGroupName = null;

        Token name = expect(TokenKind.NAME, "command name");
        if (name == null) {
            return;
        }

        DiscordSlashCommand command = getCommand(name.getLexeme());
        if (command == null) {
            command = new DiscordSlashCommand(
                    name.getLexeme(),
                    slashCommand.description()
            );

            this.commands.add(command);
        }

        scanSubcommands();
        List<OptionData> options;
        if (this.subcommandName != null) {
            if (command.hasSubcommand(this.subcommandGroupName, this.subcommandName)) {
                System.err.println("Command '" + slashCommand.command() + "' already exists (ignoring the options)");
                return;
            }

            SubcommandData subcommandData = new SubcommandData(
                    this.subcommandName,
                    slashCommand.subcommandDescription()
            );

            options = buildOptions(method);
            if (options == null) {
                return;
            }

            subcommandData.addOptions(options);

            if (this.subcommandGroupName != null) {
                SubcommandGroupData subcommandGroupData = command.getSubcommandGroup(this.subcommandGroupName);

                if (subcommandGroupData != null) {
                    subcommandGroupData.addSubcommands(subcommandData);
                } else {
                    subcommandGroupData = new SubcommandGroupData(
                            this.subcommandGroupName,
                            slashCommand.subcommandGroupDescription()
                    );

                    subcommandGroupData.addSubcommands(subcommandData);
                    command.addSubcommandGroups(subcommandGroupData);
                }
            } else {
                command.addSubcommands(subcommandData);
            }
        } else {
            options = buildOptions(method);
            if (options == null) {
                return;
            }

            command.addOptions(options);
        }

        CommandMethod commandMethod = new CommandMethod(
                name.getLexeme(),
                this.subcommandName,
                this.subcommandGroupName,
                method,
                commandInstance
        );

        commandMethod.cacheAutoCompleteMethods(options);

        this.commandMethods.add(commandMethod);
    }

    /**
     * Turn all the method's parameters to {@link OptionData}
     *
     * @param method    The method to inspect
     * @return          The parameters turned into JDA's {@link OptionData} so they can be added to commands/subcommands
     */
    @Nullable
    private List<OptionData> buildOptions(@NotNull final Method method) {
        List<OptionData> options = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            Param description = parameter.getAnnotation(Param.class);

            if (description == null) {
                continue;
            }

            Token optionToken = this.scanner.token();
            if (optionToken == null) {
                System.err.println("Missing token for variable with following description: " + description.description());
                return null;
            }

            OptionType type = TypeTable.get(parameter.getType());
            if (type == null) {
                System.err.println("Parameter with type " + parameter.getType().getName() + " cannot be used for a command parameter");
                return null;
            }

            options.add(new OptionData(
                    TypeTable.get(parameter.getType()),
                    optionToken.getLexeme(),
                    description.description(),
                    optionToken.getKind().equals(TokenKind.PARAM_REQUIRED),
                    !description.autocomplete().isEmpty()
            ));

            this.scanner.nextToken();
        }

        return options;
    }

    /**
     * Get a {@link DiscordSlashCommand} from the internal command list with the given name
     *
     * @param commandName   The command's name to get
     * @return              {@link DiscordSlashCommand} if the command exists, {@code null} otherwise
     */
    @Nullable
    private DiscordSlashCommand getCommand(@NotNull final String commandName) {
        for (DiscordSlashCommand slashCommand : this.commands) {
            if (slashCommand.getName().equalsIgnoreCase(commandName)) {
                return slashCommand;
            }
        }

        return null;
    }

    /**
     * Check the next one and two tokens to see if they're subcommands, and add them to the command
     */
    private void scanSubcommands() {
        if (this.scanner.token(1) != null && this.scanner.token(1).getKind().equals(TokenKind.NAME)) {
            // check if the next two tokens are subcommands.
            // if that's the case, the first one is the subcommand group and the second one the subcommand itself.
            Token subcommandGroupName = expect(TokenKind.NAME, "subcommand group name");
            Token subcommandName = expect(TokenKind.NAME, "subcommand name");
            if (subcommandGroupName == null || subcommandName == null) {
                return;
            }

            this.subcommandGroupName = subcommandGroupName.getLexeme();
            this.subcommandName = subcommandName.getLexeme();
        } else if (this.scanner.token() != null && this.scanner.token().getKind().equals(TokenKind.NAME)) {
            // check if the next token is a subcommand.
            Token subcommandName = expect(TokenKind.NAME, "subcommand name");
            if (subcommandName == null) {
                return;
            }

            this.subcommandName = subcommandName.getLexeme();
        }
    }

    /**
     * Return and skip the current token if the given TokenKind matches the current token's TokenKind.
     * If they don't match, we print an error message and return {@code null}
     */
    @Nullable
    private Token expect(@NotNull final TokenKind kind, @NotNull final String use) {
        // we get the next token
        Token next = this.scanner.token();

        // we check if they have the same kind
        if (next.getKind() != kind) {
            System.err.println("Expected " + kind + " but got " + next.getKind() + " for " + use + ". Refusing command method.");
            return null;
        }

        // if they have, we skip the token and return it
        this.scanner.nextToken();
        return next;
    }

    /**
     * Build all the commands we added to {@link SlashCommandData} objects and return them as a list.
     *
     * @return  The list of commands to send to discord
     */
    @NotNull
    public List<SlashCommandData> getDiscordCommands() {
        List<SlashCommandData> slashCommands = new ArrayList<>();

        for (DiscordSlashCommand command : this.commands) {
            slashCommands.add(command.build());
        }

        return slashCommands;
    }

    /**
     * Get a {@link CommandMethod} from the internal list
     *
     * @param commandName           The command name
     * @param subcommandName        The subcommand name
     * @param subcommandGroupName   The subcommand group name
     * @return                      The {@link CommandMethod} if one matches, {@code null} otherwise
     */
    @Nullable
    public CommandMethod getCommandMethod(@NotNull final String commandName, @Nullable final String subcommandName, @Nullable final String subcommandGroupName) {
        Checks.notNull(commandName, "commandName");

        for (CommandMethod command : this.commandMethods) {
            if (command.matches(commandName, subcommandName, subcommandGroupName)) {
                return command;
            }
        }

        return null;
    }

    /**
     * As the CommandRegistry is a singleton for practical use, we have a getter for its instance
     *
     * @return  The CommandRegistry's instance
     */
    @NotNull
    public static CommandRegistry getInstance() {
        if (instance == null) {
            instance = new CommandRegistry();
        }

        return instance;
    }
}
