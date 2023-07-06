package fr.rader.gertrude.commands;

import fr.rader.gertrude.annotations.SlashCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class which caches and helps Gertrude to get all the slash commands you defined.
 * You don't have to do anything else except extend this class.
 *
 * <h2>Example:</h2>
 * <pre><code>
 * public class MyCommand extends Command {
 *     // code...
 * }
 * </code></pre>
 */
public abstract class Command {

    private final List<Method> commandMethods;

    /**
     * Caches all the methods annotated with the {@link SlashCommand} annotation.
     * This constructor is private because we don't want to be able to create empty Commands.
     */
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
    @NotNull
    final List<Method> getCommandMethods() {
        return this.commandMethods;
    }
}
