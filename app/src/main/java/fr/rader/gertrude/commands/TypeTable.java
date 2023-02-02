package fr.rader.gertrude.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

public class TypeTable {

    private static final Map<Class<?>, OptionType> typeTable = new HashMap<>();

    /**
     * Get the corresponding {@link OptionType} based on the given Class.
     *
     * @param clazz     The class to get the corresponding OptionType to
     * @return          {@code null} if the class has no corresponding type,
     *                  or the corresponding OptionType if it exists
     */
    public static OptionType get(Class<?> clazz) {
        return typeTable.get(clazz);
    }

    // register the different option types jda supports
    static {
        // string type
        typeTable.put(String.class,         OptionType.STRING);

        // numbers/boolean
        typeTable.put(Boolean.class,        OptionType.BOOLEAN);
        typeTable.put(boolean.class,        OptionType.BOOLEAN);
        typeTable.put(Long.class,           OptionType.INTEGER);
        typeTable.put(long.class,           OptionType.INTEGER);
        typeTable.put(Integer.class,        OptionType.INTEGER);
        typeTable.put(int.class,            OptionType.INTEGER);
        typeTable.put(Double.class,         OptionType.NUMBER);
        typeTable.put(double.class,         OptionType.NUMBER);

        // mentionables
        typeTable.put(IMentionable.class,   OptionType.MENTIONABLE);
        typeTable.put(Member.class,         OptionType.USER);
        typeTable.put(User.class,           OptionType.USER);
        typeTable.put(Role.class,           OptionType.ROLE);

        // channels
        typeTable.put(ChannelType.class,    OptionType.CHANNEL);
        typeTable.put(GuildChannel.class,   OptionType.CHANNEL);
        typeTable.put(MessageChannel.class, OptionType.CHANNEL);
        typeTable.put(TextChannel.class,    OptionType.CHANNEL);
        typeTable.put(NewsChannel.class,    OptionType.CHANNEL);
        typeTable.put(ThreadChannel.class,  OptionType.CHANNEL);
        typeTable.put(AudioChannel.class,   OptionType.CHANNEL);
        typeTable.put(VoiceChannel.class,   OptionType.CHANNEL);
        typeTable.put(StageChannel.class,   OptionType.CHANNEL);
    }
}
