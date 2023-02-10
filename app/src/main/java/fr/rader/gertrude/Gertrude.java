package fr.rader.gertrude;

import fr.rader.gertrude.commands.*;
import fr.rader.gertrude.events.SlashCommandAutoCompleteListener;
import fr.rader.gertrude.events.SlashCommandListener;
import fr.rader.gertrude.utils.Checks;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This is the brain where you can add new commands and parameters, and send them to Discord as actual commands.
 * To get an instance of this class, you must call {@link Gertrude#summonGertrude(JDA)}.
 * I recommend you call the summonGertrude method after getting your JDA instance.
 */
public final class Gertrude {

    private static Gertrude instance;

    private final JDA jda;

    private GertrudeState state;

    private Gertrude(JDA jda) {
        this.jda = jda;
        this.state = GertrudeState.NONE;
    }

    /**
     * Register Gertrude's events.
     * This must be the first Gertrude's method to be executed.
     */
    public void registerEvents() {
        checkState("registerEvents", GertrudeState.NONE);
        this.state = GertrudeState.EVENTS_REGISTERED;

        this.jda.addEventListener(new SlashCommandListener());
        this.jda.addEventListener(new SlashCommandAutoCompleteListener());
    }

    /**
     * Add a command parameter for the slash command methods.
     *
     * Registering a {@code SomeClass} parameter looks like this:
     * <pre>
     *     addCommandParameter(SomeClass.class, this.someClassInstance);
     * </pre>
     *
     * The {@code SomeClass} instance can be retrieved like this in a command:
     * <pre>
     *     public void command(SomeClass someClass) {
     *         // do stuff with SomeClass.
     *         // someClass is the same instance as the this.someClassInstance we gave above.
     *     }
     * </pre>
     *
     * @param clazz     The class of the instance to add
     * @param instance  The instance to add
     * @param <T>       The type of the instance to add
     */
    public <T> void addCommandParameter(Class<T> clazz, T instance) {
        Checks.notNull("class", "Gertrude#addCommandParameter", clazz);
        Checks.notNull("class instance", "Gertrude#addCommandParameter", instance);

        checkState("addCommandParameter", GertrudeState.EVENTS_REGISTERED, GertrudeState.PARAMS_ADDED);
        this.state = GertrudeState.PARAMS_ADDED;

        if (
                TypeTable.get(clazz) != null ||
                ParameterRegistry.getInstance().get(clazz) != null
        ) {
            System.err.println("Cannot add parameter: parameter with type " + clazz.getName() + " already exist.");
            return;
        }

        ParameterRegistry.getInstance().add(clazz, instance);
    }

    /**
     * Add a command to Gertrude. All methods with the {@link fr.rader.gertrude.annotations.SlashCommand} annotation will be processed.
     *
     * @param command   The command class to process
     * @param <T>       The type of the class. We only accept classes extending {@link Command}
     */
    public <T extends Command> void addCommand(T command) {
        Checks.notNull("command", "Gertrude#addCommand", command);

        checkState("addCommand", GertrudeState.EVENTS_REGISTERED, GertrudeState.PARAMS_ADDED, GertrudeState.COMMANDS_ADDED);
        this.state = GertrudeState.COMMANDS_ADDED;

        CommandRegistry.getInstance().registerCommandClass(command);
    }

    /**
     * Register the commands to the given guilds, or all the guilds of none are provided
     *
     * @param guilds    The guilds to add the commands to
     */
    public void registerCommands(Guild... guilds) {
        Checks.notNull("guilds", "Gertrude#registerCommands", guilds);

        checkState("registerCommands", GertrudeState.COMMANDS_ADDED);
        this.state = GertrudeState.COMMANDS_REGISTERED;

        // get the commands from the command registry
        List<SlashCommandData> commands = CommandRegistry.getInstance().getDiscordCommands();

        // register the commands to all guilds
        if (guilds.length == 0) {
            this.jda.updateCommands()
                    .addCommands(commands)
                    .queue();

            return;
        }

        // register the commands to specific guilds
        for (Guild guild : guilds) {
            guild.updateCommands()
                 .addCommands(commands)
                 .queue();
        }
    }

    /**
     * Register the commands to the given guilds, or all the guilds of none are provided
     *
     * @param guilds    The guilds to add the commands to
     */
    public void registerCommands(Collection<? extends Guild> guilds) {
        Checks.notNull("guilds", "Gertrude#registerCommands", guilds);

        registerCommands(guilds.toArray(new Guild[0]));
    }

    /**
     * Get an instance of Gertrude. You should only call this once.
     *
     * @param jda   The JDA instance so we can register commands/events
     * @return      The Gertrude instance
     */
    public static Gertrude summonGertrude(JDA jda) {
        if (instance == null) {
            Checks.notNull("jda", "Gertrude#summonGertrude", jda);
            instance = new Gertrude(jda);
        }

        return instance;
    }

    /**
     * Check if Gertrude's given state is one of the given states.
     *
     * @param caller    The method that called this method. This is used to provide "better" errors.
     * @param states    The states that we expect Gertrude to be in
     */
    private void checkState(String caller, GertrudeState... states) {
        if (Arrays.stream(states).noneMatch(state -> state == this.state)) {
            throw new IllegalStateException(caller + " cannot be called in the " + this.state + " state");
        }
    }

    /**
     * Return Gertrude's JDA instance
     *
     * @return Gertrude's JDA instance
     */
    public JDA getJda() {
        return this.jda;
    }

    // internal gertrude states.
    // we want to execute methods in a specific order
    private enum GertrudeState {
        NONE,
        EVENTS_REGISTERED,
        PARAMS_ADDED,
        COMMANDS_ADDED,
        COMMANDS_REGISTERED
    }
}
