package fr.rader.gertrude.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * This is a parameter's description.
     * This must be set in the method's definition:
     *
     * <pre>
     *     public void command(
     *             &#064;Desc("Option Description")
     *             String option
     *     ) {
     *         // code...
     *     }
     * </pre>
     */
    String value();

    /**
     * The value of this should be the autocompletion method's name.
     * The autocompletion method must have the following definition:
     *
     * <pre>
     *     public java.util.List<net.dv8tion.jda.api.interactions.commands.Command.Choice> autoCompletionMethod(net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent event) {
     *         // code...
     *     }
     * </pre>
     */
    String autocomplete() default "";
}
