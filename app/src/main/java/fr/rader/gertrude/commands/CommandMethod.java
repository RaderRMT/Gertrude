package fr.rader.gertrude.commands;

import fr.rader.gertrude.annotations.Param;
import fr.rader.gertrude.commands.getters.ClassToCommandElementGetter;
import fr.rader.gertrude.commands.getters.ClassToOptionGetter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;

/**
 * Holds handy methods to cache, validate and call autocompletion methods, and to call command methods
 */
public final class CommandMethod {

    private static final String AUTOCOMPLETE_COLLECTION_TYPE = "net.dv8tion.jda.api.interactions.commands.Command$Choice";

    private final String name;
    private final String subcommand;
    private final String subcommandGroup;

    private final Method method;
    private final Command instance;

    private final Map<String, Method> autoCompleteMethods;

    CommandMethod(
            @NotNull final String name,
            @Nullable final String subcommand,
            @Nullable final String subcommandGroup,
            @NotNull final Method method,
            @NotNull final Command instance
    ) {
        this.name = name;
        this.subcommand = subcommand;
        this.subcommandGroup = subcommandGroup;
        this.method = method;
        this.instance = instance;

        this.autoCompleteMethods = new HashMap<>();
    }

    /**
     * Invoke the command method.
     *
     * @param event     The event that triggered the invoke
     */
    public void invoke(@NotNull final SlashCommandInteractionEvent event) {
        Checks.notNull(event, "event");

        List<Object> parameters = buildParameters(event);

        try {
            this.method.invoke(
                    this.instance,
                    parameters.toArray(new Object[0])
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the autocompletion choices from the given option and event
     *
     * @param optionName    The option to get the autocompletion choices for
     * @param event         The event to give to the autocompletion method
     * @return              The list of choices to send to Discord
     */
    @NotNull
    public List<Choice> getAutoCompleteChoices(@NotNull final String optionName, @NotNull final CommandAutoCompleteInteractionEvent event) {
        Checks.notNull(optionName, "optionName");
        Checks.notNull(event, "event");

        Method autoCompleteMethod = this.autoCompleteMethods.get(optionName);
        if (autoCompleteMethod == null) {
            return new ArrayList<>();
        }

        List<Choice> choices = null;
        try {
            choices = (List<Choice>) autoCompleteMethod.invoke(this.instance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (choices == null) {
            choices = new ArrayList<>();
        }

        return choices;
    }

    /**
     * Get all object instances from the {@link ParameterRegistry}, or the {@link SlashCommandInteractionEvent} and add them to a list.
     *
     * @param event     The even to get data from
     * @return          A list of objects (the parameters) to give to the command method
     */
    @NotNull
    private List<Object> buildParameters(@NotNull final SlashCommandInteractionEvent event) {
        List<Object> parameters = new ArrayList<>();

        int optionIndex = 0;
        for (Parameter parameter : this.method.getParameters()) {
            Class<?> type = parameter.getType();

            // parameters with the @Param annotation have the highest priority.
            // the data will be extracted from the event's parameters
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);

                if (optionIndex >= event.getOptions().size()) {
                    parameters.add(null);
                    continue;
                }

                OptionMapping option = event.getOptions().get(optionIndex);
                if (!option.getName().equals(param.name())) {
                    parameters.add(null);
                    continue;
                }

                Object paramObject = ClassToOptionGetter.get(type, option);
                parameters.add(paramObject);

                optionIndex++;
                continue;
            }

            // the next highest priority is the event's data
            if (ClassToCommandElementGetter.has(type)) {
                parameters.add(ClassToCommandElementGetter.get(type, event));
                continue;
            }

            // the next highest priority is the event itself
            if (type.isAssignableFrom(SlashCommandInteractionEvent.class)) {
                parameters.add(event);
                continue;
            }

            // the lowest priority is the parameter registered in the ParameterRegistry
            parameters.add(ParameterRegistry.getInstance().get(type));
        }

        for (int i = parameters.size(); i < this.method.getParameterCount(); i++) {
            parameters.add(null);
        }

        return parameters;
    }

    /**
     * Get all the autocompletion methods and caches them
     *
     * @param options   The options to get the option names from
     */
    void cacheAutoCompleteMethods(@NotNull final List<OptionData> options) {
        int optionIndex = 0;
        for (Parameter parameter : this.method.getParameters()) {
            Param param = parameter.getAnnotation(Param.class);
            if (param == null) {
                continue;
            }

            OptionData option = options.get(optionIndex++);
            String autoCompleteMethodName = param.autocomplete();
            if (autoCompleteMethodName.isEmpty()) {
                continue;
            }

            Method autoCompleteMethod = null;
            try {
                autoCompleteMethod = this.instance.getClass().getDeclaredMethod(autoCompleteMethodName, CommandAutoCompleteInteractionEvent.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (autoCompleteMethod == null) {
                continue;
            }

            if (Modifier.isStatic(autoCompleteMethod.getModifiers())) {
                System.err.println("AutoComplete method " + autoCompleteMethod + " is static!");
                continue;
            }

            if (!Collection.class.isAssignableFrom(autoCompleteMethod.getReturnType())) {
                System.err.println("Return value for autocomplete method " + autoCompleteMethod + " is not a Collection!");
                continue;
            }

            ParameterizedType type = (ParameterizedType) autoCompleteMethod.getGenericReturnType();
            Type[] args = type.getActualTypeArguments();
            if (args.length != 1 || !args[0].getTypeName().equals(AUTOCOMPLETE_COLLECTION_TYPE)) {
                System.err.println("Collection type for autocomplete method " + autoCompleteMethod + " return value is not a JDA Command.Choice!");
                continue;
            }

            autoCompleteMethod.setAccessible(true);
            this.autoCompleteMethods.put(option.getName(), autoCompleteMethod);
        }
    }

    /**
     * Returns true if this command has the same name, subcommand and subcommand group as the given parameters, false otherwise
     *
     * @param name              The command's name
     * @param subcommand        The command's subcommand
     * @param subcommandGroup   The command's subcommand group
     * @return                  true if the command matches with the given string, false otherwise
     */
    boolean matches(@NotNull final String name, @Nullable final String subcommand, @Nullable final String subcommandGroup) {
        if (!this.name.equalsIgnoreCase(name)) {
            return false;
        }

        if (this.subcommand != null ^ subcommand != null) {
            return false;
        }

        boolean matches = true;
        if (this.subcommand != null) {
            matches = this.subcommand.equalsIgnoreCase(subcommand);
        }

        if (this.subcommandGroup != null ^ subcommandGroup != null) {
            return false;
        }

        if (this.subcommandGroup != null) {
            matches &= this.subcommandGroup.equalsIgnoreCase(subcommandGroup);
        }

        return matches;
    }
}
