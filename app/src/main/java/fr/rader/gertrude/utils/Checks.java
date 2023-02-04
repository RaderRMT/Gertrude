package fr.rader.gertrude.utils;

import fr.rader.gertrude.Gertrude;

public class Checks {

    public static void notNull(String name, String caller, Object object) {
        if (object == null) {
            // if the object is null, we shutdown the bot
            Gertrude.summonGertrude(null).getJda().shutdownNow();

            // and we throw an exception
            throw new IllegalArgumentException("Parameter \"" + name + "\" from \"" + caller + "\" cannot be null");
        }
    }
}
