package fr.rader.gertrude.commands.getters;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClassToCommandElementGetter {

    private static final Map<Class<?>, Function<SlashCommandInteractionEvent, Object>> functionMap = new HashMap<>();

    /**
     * Get the corresponding Object based on the given Class.
     *
     * @param clazz     The class to get the corresponding Object to
     * @return          {@code null} if the class has no corresponding type,
     *                  or the corresponding Object if it exists
     */
    public static Function<SlashCommandInteractionEvent, Object> get(Class<?> clazz) {
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
