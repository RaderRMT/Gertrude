package fr.rader.gertrude.commands;

import java.util.HashMap;
import java.util.Map;

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
    public void add(Class<?> clazz, Object instance) {
        this.classToInstance.put(clazz, instance);
    }

    /**
     * Get the corresponding {@link Object} instance based on the given Class.
     *
     * @param clazz     The class to get the corresponding Object instance to
     * @return          {@code null} if the class has no corresponding object instance,
     *                  or the corresponding object instance if one was added
     */
    public Object get(Class<?> clazz) {
        return this.classToInstance.get(clazz);
    }

    // return the ParameterRegistry's instance.
    // this is a singleton as we only want one instance of this class
    public static ParameterRegistry getInstance() {
        if (instance == null) {
            instance = new ParameterRegistry();
        }

        return instance;
    }
}
