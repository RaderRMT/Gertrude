package fr.rader.gertrude.events;

import fr.rader.gertrude.commands.CommandMethod;
import fr.rader.gertrude.commands.CommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandAutoCompleteListener extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        CommandMethod command = CommandRegistry.getInstance().getCommandMethod(event.getName(), event.getSubcommandName(), event.getSubcommandGroup());
        if (command == null) {
            return;
        }

        event.replyChoices(command.getAutoCompleteChoices(event.getFocusedOption().getName(), event)).queue();
    }
}
