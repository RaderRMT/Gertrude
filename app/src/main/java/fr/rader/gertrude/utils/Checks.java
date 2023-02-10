package fr.rader.gertrude.utils;

import fr.rader.gertrude.Gertrude;

/**
 * Provides various checks
 */
public class Checks {

    /**
     * Checks if the given object is null
     *
     * @param name      The name of the variable holding the object (for better errors)
     * @param caller    The method that called this notNull method (for better errors)
     * @param object    The object instance to check
     */
    public static void notNull(String name, String caller, Object object) {
        if (object == null) {
            // if the object is null, we shutdown the bot
            Gertrude.summonGertrude(null).getJda().shutdownNow();

            // and we throw an exception
            throw new IllegalArgumentException("Parameter \"" + name + "\" from \"" + caller + "\" cannot be null");
        }
    }
}
