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
}
