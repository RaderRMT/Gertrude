package fr.rader.gertrude.events;

import fr.rader.gertrude.commands.CommandMethod;
import fr.rader.gertrude.commands.CommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * This is an implementation of JDA's ListenerAdapter.
 * It only listens to slash commands.
 * When a slash command even is triggered, we get the command from the command registry
 * and we invoke the command method attached to it.
 */
public final class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        CommandMethod command = CommandRegistry.getInstance().getCommandMethod(
                event.getName(),
                event.getSubcommandName(),
                event.getSubcommandGroup()
        );

        if (command != null) {
            command.invoke(event);
        }
    }
}
