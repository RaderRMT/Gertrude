package fr.rader.gertrude.commands.getters;

import fr.rader.gertrude.utils.Checks;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Util class to get a {@link Function} from the given class.
 * This Function is then used to get a specific object from JDA's {@link SlashCommandInteractionEvent}s
 */
public final class ClassToCommandElementGetter {

    private static final Map<Class<?>, Function<SlashCommandInteractionEvent, Object>> functionMap = new HashMap<>();

    // private constructor, we don't want an instance of this class
    private ClassToCommandElementGetter() {
    }

    /**
     * Get a getter {@link Function} depending on the given class.
     * This Function is then used to get specific objects from JDA's {@link SlashCommandInteractionEvent}s
     *
     * @param clazz The class to get the getter for
     * @return      A {@link Function} if the class has a getter for it, {@code null} otherwise
     */
    public static Function<SlashCommandInteractionEvent, Object> get(Class<?> clazz) {
        Checks.notNull("clazz", "ClassToCommandElementGetter#get", clazz);

        return functionMap.get(clazz);
    }

    static {
        // mentionables
        functionMap.put(Member.class,         SlashCommandInteractionEvent::getMember);
        functionMap.put(User.class,           SlashCommandInteractionEvent::getUser);

        // guild
        functionMap.put(Guild.class,          SlashCommandInteractionEvent::getGuild);

        // channels
        functionMap.put(ChannelType.class,    SlashCommandInteractionEvent::getChannelType);
        functionMap.put(GuildChannel.class,   SlashCommandInteractionEvent::getGuildChannel);
        functionMap.put(MessageChannel.class, SlashCommandInteractionEvent::getMessageChannel);
        functionMap.put(TextChannel.class,    SlashCommandInteractionEvent::getTextChannel);
        functionMap.put(NewsChannel.class,    SlashCommandInteractionEvent::getNewsChannel);
        functionMap.put(ThreadChannel.class,  SlashCommandInteractionEvent::getThreadChannel);
        functionMap.put(VoiceChannel.class,   SlashCommandInteractionEvent::getVoiceChannel);
    }
}
