package fr.rader.gertrude.commands;

import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that holds a list of all the parameters we have and can give to our command methods.
 */
public final class ParameterRegistry {

    private static ParameterRegistry instance;

    private final Map<Class<?>, Object> classToInstance;

    private ParameterRegistry() {
        this.classToInstance = new HashMap<>();
    }

    /**
     * Link the given Object instance to the given Class.
     *
     * @param clazz     The class to link the instance to
     * @param instance  The instance to link to the class
     */
    public void add(@NotNull final Class<?> clazz, @NotNull final Object instance) {
        Checks.notNull(clazz, "clazz");
        Checks.notNull(instance, "instance");

        this.classToInstance.put(clazz, instance);
    }

    /**
     * Get the corresponding {@link Object} instance based on the given Class.
     *
     * @param clazz     The class to get the corresponding Object instance to
     * @return          {@code null} if the class has no corresponding object instance,
     *                  or the corresponding object instance if one was added
     */
    @Nullable
    public Object get(@NotNull final Class<?> clazz) {
        return this.classToInstance.get(clazz);
    }

    /**
     * As the ParameterRegistry is a singleton for practical use, we have a getter for its instance
     *
     * @return  The ParameterRegistry's instance
     */
    @NotNull
    public static ParameterRegistry getInstance() {
        if (instance == null) {
            instance = new ParameterRegistry();
        }

        return instance;
    }
}
