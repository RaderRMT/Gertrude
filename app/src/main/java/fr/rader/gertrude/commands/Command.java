package fr.rader.gertrude.commands;

import fr.rader.gertrude.annotations.SlashCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private final List<Method> commandMethods;

    protected Command() {
        this.commandMethods = new ArrayList<>();

        // we cache the methods with the SlashCommand annotation
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SlashCommand.class)) {
                this.commandMethods.add(method);
            }
        }
    }

    /**
     * Returns the cached list of methods having the {@link SlashCommand} annotation associated with them.
     */
    final List<Method> getCommandMethods() {
        return this.commandMethods;
    }
}
