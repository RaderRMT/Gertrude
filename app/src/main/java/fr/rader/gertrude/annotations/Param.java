package fr.rader.gertrude.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that gives a description and an autocomplete method to a parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * This is a parameter's description.
     * This must be set in the method's definition:
     *
     * Example:
     * <pre><code>
     * &#064;SlashCommand(&#47;* The usual command declaration *&#47;)
     * public void command(
     *         &#064;Param("Option Description")
     *         String option
     * ) {
     *     // code...
     * }
     * </code></pre>
     *
     * @see     SlashCommand
     *
     * @return  The parameter's description
     */
    String value();

    /**
     * The value of this should be the autocompletion method's name.
     * The autocompletion method must have the following definition:
     *
     * Example:
     * <pre><code>
     * // our command with a parameter that has autocompletion
     * &#064;SlashCommand(&#47;* The usual command declaration *&#47;)
     * public void command(
     *         &#064;Param(
     *                 value = "Option Description",
     *                 autocomplete = "autoCompletionMethod"
     *         )
     *         String option
     * ) {
     *     // code...
     * }
     *
     * // the autocompletion method for our "option" parameter
     * public List&lt;Choice&gt; autoCompletionMethod(
     *         CommandAutoCompleteInteractionEvent event
     * ) {
     *     // code...
     * }
     * </code></pre>
     *
     * Note: The return value must be a Collection, this mean it can be a List or a custom object as long as it implements Collection
     *
     * @see     SlashCommand
     * @see     java.util.Collection
     * @see     net.dv8tion.jda.api.interactions.commands.Command.Choice
     * @see     net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
     *
     * @return  The autocomplete method name
     */
    String autocomplete() default "";
}
