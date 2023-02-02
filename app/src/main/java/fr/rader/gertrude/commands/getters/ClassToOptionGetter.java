package fr.rader.gertrude.commands.getters;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClassToOptionGetter {

    private static final Map<Class<?>, Function<OptionMapping, Object>> functionMap = new HashMap<>();

    /**
     * Get the corresponding {@link OptionType} based on the given Class.
     *
     * @param clazz     The class to get the corresponding OptionType to
     * @return          {@code null} if the class has no corresponding type,
     *                  or the corresponding OptionType if it exists
     */
    public static Function<OptionMapping, Object> get(Class<?> clazz) {
        return functionMap.get(clazz);
    }

    static {
        // string type
        functionMap.put(String.class,         OptionMapping::getAsString);

        // numbers/boolean
        functionMap.put(Boolean.class,        OptionMapping::getAsBoolean);
        functionMap.put(boolean.class,        OptionMapping::getAsBoolean);
        functionMap.put(Long.class,           OptionMapping::getAsLong);
        functionMap.put(long.class,           OptionMapping::getAsLong);
        functionMap.put(Integer.class,        OptionMapping::getAsInt);
        functionMap.put(int.class,            OptionMapping::getAsInt);
        functionMap.put(Double.class,         OptionMapping::getAsDouble);
        functionMap.put(double.class,         OptionMapping::getAsDouble);

        // mentionables
        functionMap.put(IMentionable.class,   OptionMapping::getAsMentionable);
        functionMap.put(Member.class,         OptionMapping::getAsMember);
        functionMap.put(User.class,           OptionMapping::getAsUser);
        functionMap.put(Role.class,           OptionMapping::getAsRole);

        // channels
        functionMap.put(ChannelType.class,    OptionMapping::getChannelType);
        functionMap.put(GuildChannel.class,   OptionMapping::getAsGuildChannel);
        functionMap.put(MessageChannel.class, OptionMapping::getAsMessageChannel);
        functionMap.put(TextChannel.class,    OptionMapping::getAsTextChannel);
        functionMap.put(NewsChannel.class,    OptionMapping::getAsNewsChannel);
        functionMap.put(ThreadChannel.class,  OptionMapping::getAsThreadChannel);
        functionMap.put(AudioChannel.class,   OptionMapping::getAsAudioChannel);
        functionMap.put(VoiceChannel.class,   OptionMapping::getAsVoiceChannel);
        functionMap.put(StageChannel.class,   OptionMapping::getAsStageChannel);
    }
}
