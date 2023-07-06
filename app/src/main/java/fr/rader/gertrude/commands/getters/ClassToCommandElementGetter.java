package fr.rader.gertrude.commands.getters;

import fr.rader.gertrude.utils.ChannelUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param event The event to get the element from
     * @return      A {@link Function} if the class has a getter for it, {@code null} otherwise
     */
    @Nullable
    public static Object get(@NotNull final Class<?> clazz, @NotNull final SlashCommandInteractionEvent event) {
        Checks.notNull(clazz, "clazz");

        if (Channel.class.isAssignableFrom(clazz)) {
            return ChannelUtils.getAsNormalChannel(event.getChannel());
        }

        Function<SlashCommandInteractionEvent, Object> function = functionMap.get(clazz);
        if (function == null) {
            return null;
        }

        return function.apply(event);
    }

    /**
     * Returns true if the function map has the given class, false otherwise
     *
     * @param clazz The class to check
     * @return      true if the function map has the given class
     */
    public static boolean has(@NotNull final Class<?> clazz) {
        return functionMap.containsKey(clazz) || ChannelUtils.isChannel(clazz);
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
    }
}
