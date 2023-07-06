package fr.rader.gertrude.commands;

import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A representation of command methods (methods annotated with {@link fr.rader.gertrude.annotations.SlashCommand}).
 * This class holds all the subcommands and subcommand groups for one command (commands where the name equals this command's name).
 */
public final class DiscordSlashCommand {

    private final String name;
    private final String description;

    private final List<SubcommandGroupData> subcommandGroups;
    private final List<SubcommandData> subcommands;
    private final List<OptionData> options;

    DiscordSlashCommand(@NotNull final String name, @NotNull final String description) {
        this.name = name;
        this.description = description;

        this.subcommandGroups = new ArrayList<>();
        this.subcommands = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    /**
     * Check if this command has the given subcommand & subcommand group
     *
     * @param subcommandGroupName   The subcommand's group to check
     * @param subcommandName        The subcommand to check
     * @return                      true if this command has the same subcommand & subcommand group, false otherwise
     */
    boolean hasSubcommand(@Nullable final String subcommandGroupName, @NotNull final String subcommandName) {
        if (subcommandGroupName == null) {
            for (SubcommandData subcommandData : this.subcommands) {
                if (subcommandData.getName().equalsIgnoreCase(subcommandName)) {
                    return true;
                }
            }
        } else {
            for (SubcommandGroupData subcommandGroupData : this.subcommandGroups) {
                if (!subcommandGroupData.getName().equalsIgnoreCase(subcommandGroupName)) {
                    continue;
                }

                for (SubcommandData subcommandData : this.subcommands) {
                    if (subcommandData.getName().equalsIgnoreCase(subcommandName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get the given subcommand group from this command
     *
     * @param subcommandGroupName   The subcommand group's name to get
     * @return                      {@link SubcommandGroupData} if this command has the subcommand group, {@code null} otherwise
     */
    @Nullable
    SubcommandGroupData getSubcommandGroup(@NotNull final String subcommandGroupName) {
        for (SubcommandGroupData subcommandGroupData : this.subcommandGroups) {
            if (subcommandGroupData.getName().equalsIgnoreCase(subcommandGroupName)) {
                return subcommandGroupData;
            }
        }

        return null;
    }

    /**
     * Add the given subcommand group(s) to this command
     *
     * @param subcommandGroups  The subcommand group(s) to add
     */
    void addSubcommandGroups(@NotNull final SubcommandGroupData... subcommandGroups) {
        this.subcommandGroups.addAll(Arrays.asList(subcommandGroups));
    }

    /**
     * Add the given subcommand group(s) to this command
     *
     * @param subcommandGroups  The subcommand group(s) to add
     */
    void addSubcommandGroups(@NotNull final Collection<? extends SubcommandGroupData> subcommandGroups) {
        addSubcommandGroups(subcommandGroups.toArray(new SubcommandGroupData[0]));
    }

    /**
     * Add the given subcommand(s) to this command
     *
     * @param subcommands  The subcommand(s) to add
     */
    void addSubcommands(@NotNull final SubcommandData... subcommands) {
        this.subcommands.addAll(Arrays.asList(subcommands));
    }

    /**
     * Add the given subcommand(s) to this command
     *
     * @param subcommands  The subcommand(s) to add
     */
    void addSubcommands(@NotNull final Collection<? extends SubcommandData> subcommands) {
        addSubcommands(subcommands.toArray(new SubcommandData[0]));
    }

    /**
     * Add the given options(s) to this command
     *
     * @param options  The options(s) to add
     */
    void addOptions(@NotNull final OptionData... options) {
        this.options.addAll(Arrays.asList(options));
    }

    /**
     * Add the given options(s) to this command
     *
     * @param options  The options(s) to add
     */
    void addOptions(@NotNull final Collection<? extends OptionData> options) {
        addOptions(options.toArray(new OptionData[0]));
    }

    @NotNull
    String getName() {
        return this.name;
    }

    /**
     * Turn this {@link DiscordSlashCommand} object to a {@link SlashCommandData} that we can give to discord
     *
     * @return  The command as a JDA {@link SlashCommandData}
     */
    @NotNull
    SlashCommandData build() {
        SlashCommandData command = Commands.slash(
                this.name,
                this.description
        );

        command.addSubcommandGroups(this.subcommandGroups);
        command.addSubcommands(this.subcommands);
        command.addOptions(this.options);

        return command;
    }
}
