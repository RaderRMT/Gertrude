package fr.rader.gertrude.utils;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.IThreadContainer;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * A basic utility class to get data from a channel, cast a channel to another channel, etc...
 */
public class ChannelUtils {

    private static final List<Class<?>> CHANNELS = List.of(
            MessageChannel.class,
            TextChannel.class,
            NewsChannel.class,
            ThreadChannel.class,
            VoiceChannel.class
    );

    /**
     * Get the channel associated with the given MessageChannelUnion
     *
     * @param channel   The channel to get
     * @return          The channel associated with the given MessageChannelUnion
     */
    @Nullable
    public static Channel getAsNormalChannel(@NotNull final MessageChannelUnion channel) {
        ChannelType type = channel.getType();

        if (type == ChannelType.PRIVATE) {
            return channel.asPrivateChannel();
        }

        if (type == ChannelType.TEXT) {
            return channel.asTextChannel();
        }

        if (type == ChannelType.NEWS) {
            return channel.asNewsChannel();
        }

        if (type.isThread()) {
            return channel.asThreadChannel();
        }

        if (type == ChannelType.VOICE) {
            return channel.asVoiceChannel();
        }

        if (channel instanceof IThreadContainer) {
            return channel.asThreadContainer();
        }

        if (type.isGuild()) {
            return channel.asGuildMessageChannel();
        }

        return null;
    }

    /**
     * Get the channel associated with the given GuildChannelUnion
     *
     * @param channel   The channel to get
     * @return          The channel associated with the given GuildChannelUnion
     */
    @Nullable
    public static Channel getAsNormalChannel(@NotNull final GuildChannelUnion channel) {
        ChannelType type = channel.getType();

        if (type == ChannelType.TEXT) {
            return channel.asTextChannel();
        }

        if (type == ChannelType.NEWS) {
            return channel.asNewsChannel();
        }

        if (type.isThread()) {
            return channel.asThreadChannel();
        }

        if (type == ChannelType.VOICE) {
            return channel.asVoiceChannel();
        }

        if (type.isGuild() && type.isMessage()) {
            return channel.asGuildMessageChannel();
        }

        if (type == ChannelType.STAGE) {
            return channel.asStageChannel();
        }

        if (type == ChannelType.CATEGORY) {
            return channel.asCategory();
        }

        if (type == ChannelType.FORUM) {
            return channel.asForumChannel();
        }

        if (type.isAudio()) {
            return channel.asAudioChannel();
        }

        if (channel instanceof StandardGuildMessageChannel) {
            return channel.asStandardGuildMessageChannel();
        }

        if (channel instanceof IThreadContainer) {
            return channel.asThreadContainer();
        }

        if (channel instanceof StandardGuildChannel) {
            return channel.asStandardGuildChannel();
        }

        System.err.println("Unknown channel type: " + channel.getClass());
        return null;
    }

    public static boolean isChannel(@NotNull final Class<?> clazz) {
        return CHANNELS.contains(clazz);
    }
}
