package fr.rader.gertrude.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows Gertrude to turn the method into a Discord command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlashCommand {

    /**
     * The command string.<br>
     * This command string should not start with a '/', optional parameters must be
     * between square brackets and required parameters must be inside a diamond.
     *
     * Example:
     * <pre><code>
     * &#064;SlashCommand(
     *         command = "name subcommandGroup subcommand &lt;required&gt; [optional]",
     *         ...
     * )
     * </code></pre>
     *
     * @return  The command string
     */
    String command();

    /**
     * This is the command's description.
     *
     * @return  The command's description
     */
    String description();

    /**
     * This is the subcommand's description.
     * This must be set if the command uses a subcommand.
     *
     * @return  The subcommand's description
     */
    String subcommandDescription() default "";

    /**
     * This is the subcommand group's description.
     * This must be set if the command uses a subcommand group.
     *
     * @return  The subcommand group's description
     */
    String subcommandGroupDescription() default "";
}
