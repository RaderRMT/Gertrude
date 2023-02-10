package fr.rader.gertrude.events;

import fr.rader.gertrude.commands.CommandMethod;
import fr.rader.gertrude.commands.CommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * This is an implementation of JDA's ListenerAdapter.
 * It only listens to command autocompletion.
 * When an autocompletion even is triggered, we get the command from the command registry,
 * execute the autocomplete method and reply with the returned choices.
 */
public final class SlashCommandAutoCompleteListener extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        CommandMethod command = CommandRegistry.getInstance().getCommandMethod(
                event.getName(),
                event.getSubcommandName(),
                event.getSubcommandGroup()
        );

        if (command != null) {
            event.replyChoices(
                    command.getAutoCompleteChoices(
                            event.getFocusedOption().getName(),
                            event
                    )
            ).queue();
        }
    }
}
