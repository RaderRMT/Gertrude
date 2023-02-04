package fr.rader.gertrude.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlashCommand {

    /**
     * The command string.
     * <p>This command string should not start with a '/', optional parameters must be
     * between square brackets and required parameters must be inside a diamond.
     *
     * <p>Example:
     * <pre>
     *    &#064;SlashCommand(
     *            command = "name subcommandGroup subcommand &lt;required&gt; [optional]",
     *            ...
     *    )
     * </pre>
     */
    String command();

    /**
     * This is the command's description.
     */
    String description();

    /**
     * This is the subcommand's description.
     * This must be set if the command uses a subcommand.
     */
    String subcommandDescription() default "";

    /**
     * This is the subcommand group's description.
     * This must be set if the command uses a subcommand group.
     */
    String subcommandGroupDescription() default "";
}
