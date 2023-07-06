package fr.rader.gertrude.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class to get an {@link OptionType} from the given class.
 */
public final class TypeTable {

    private static final Map<Class<?>, OptionType> typeTable = new HashMap<>();

    // private constructor, we don't want an instance of this class
    private TypeTable() {
    }

    /**
     * Get the corresponding {@link OptionType} based on the given Class.
     *
     * @param clazz     The class to get the corresponding OptionType to
     * @return          {@code null} if the class has no corresponding type,
     *                  or the corresponding OptionType if it exists
     */
    @Nullable
    public static OptionType get(@NotNull final Class<?> clazz) {
        Checks.notNull(clazz, "clazz");

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
