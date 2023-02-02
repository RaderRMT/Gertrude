package fr.rader.gertrude.commands;

import fr.rader.gertrude.annotations.Param;
import fr.rader.gertrude.commands.getters.ClassToCommandElementGetter;
import fr.rader.gertrude.commands.getters.ClassToOptionGetter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CommandMethod {

    private final String name;
    private final String subcommand;
    private final String subcommandGroup;

    private final Method method;
    private final Command instance;

    public CommandMethod(String name, String subcommand, String subcommandGroup, Method method, Command instance) {
        this.name = name;
        this.subcommand = subcommand;
        this.subcommandGroup = subcommandGroup;
        this.method = method;
        this.instance = instance;
    }

    /**
     * Invoke the command method.
     *
     * @param event     The event that triggered the invoke
     */
    public void invoke(SlashCommandInteractionEvent event) {
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
     * Get all object instances from the {@link ParameterRegistry}, or the {@link SlashCommandInteractionEvent} and add them to a list.
     *
     * @param event     The even to get data from
     */
    private List<Object> buildParameters(SlashCommandInteractionEvent event) {
        List<Object> parameters = new ArrayList<>();

        int optionIndex = 0;
        for (Parameter parameter : this.method.getParameters()) {
            // parameters with the @Param annotation have the highest priority.
            // the data will be extracted from the event's parameters
            if (parameter.isAnnotationPresent(Param.class)) {
                parameters.add(ClassToOptionGetter.get(parameter.getType()).apply(event.getOptions().get(optionIndex++)));
                continue;
            }

            if (ClassToOptionGetter.get(parameter.getType()) != null) {
                // the next highest priority is the event's data
                parameters.add(ClassToCommandElementGetter.get(parameter.getType()).apply(event));
            } else if (parameter.getType().isAssignableFrom(SlashCommandInteractionEvent.class)) {
                // the next highest priority is the event itself
                parameters.add(event);
            } else {
                // the lowest priority is the parameter registered in the ParameterRegistry
                parameters.add(ParameterRegistry.getInstance().get(parameter.getType()));
            }
        }

        for (int i = parameters.size(); i < this.method.getParameterCount(); i++) {
            parameters.add(null);
        }

        return parameters;
    }

    /**
     * Returns true if this command has the same name, subcommand and subcommand group as the given parameters, false otherwise
     */
    public boolean matches(String name, String subcommand, String subcommandGroup) {
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
