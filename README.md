## Gertrude

An Annotation-based Slash Command Utility.

# TODO List:

- [ ] Autocompletion

I'm updating this when I need new features.  
If you need something Gertrude doesn't have, please open an issue and tell me what issue you want in Gertrude.

As I don't really have the time to write a good README, here is the code to setup Gertrude with JDA and register commands.

### Setting Gertrude Up

```java
// you get the gertrude's instance like this
Gertrude gertrude = Gertrude.summonGertrude(this.jda);

// you register Gertrude's event(s)
gertrude.registerEvents();

// if you have parameters to register, do it here.
// you'll see what it does and why this is useful in the next category.
// here i'm just setting the bot's config as a command parameter.
// this step is optional
gertrude.addCommandParameter(BotConfig.class, this.config); // i'm assuming you have a config class

// you add commands like this, you'll see how to create commands later
gertrude.addCommand(new TheCommand());

// you register the commands to all the servers the bot is in like this:
gertrude.registerCommands();
// if you want to register your commands to a few discords only,
// you can add multiple Guild instances in the params like this:
// gertrude.registerCommands(this.jda.getGuildById(<YOUR GUILD ID>));
```

### Creating Commands

You can have multiple commands per class.

```java
import fr.rader.gertrude.annotations.Param;
import fr.rader.gertrude.annotations.SlashCommand;
import fr.rader.gertrude.commands.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TheCommand extends Command {

    @SlashCommand(
            command = "hello <user>",
            description = "Say hello to a player in the chat"
    )
    public void hello(
            @Param("The user to say hello to")
            User user,
            // if the User parameter doesn't have a @Param annotation attached to it,
            // it'll be the command's sender instead of the command parameter
            User sender,
            // the command's event so you can acknowledge the command by replying with a message
            SlashCommandInteractionEvent event,
            // if you have something to set or get from the bot's config file,
            // you can get the config's instance like this
            BotConfig config
    ) {
        event.reply(sender.getName() + " says hello to " + user.getName() + " :)").queue();
    }

    @SlashCommand(
            command = "name subcommandGroup subcommand <required> [optional]",
            description = "Quick and dirty description",
            subcommandDescription = "subcommand Description",
            subcommandGroupDescription = "subcommandGroup Description"
    )
    public void commandShowingSubcommands(
            @Param("The required option")
            String required,
            @Param("The optional option")
            String optional,
            SlashCommandInteractionEvent event
    ) {
        event.reply("Got required option " + required + " and optional option " + optional).queue();
    }
}
```