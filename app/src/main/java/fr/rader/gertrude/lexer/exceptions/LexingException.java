package fr.rader.gertrude.lexer.exceptions;

import org.jetbrains.annotations.Nullable;

/**
 * An exception thrown when an issue has been found when lexing the command string
 */
public class LexingException extends RuntimeException {

    /**
     * Throw an exception when an issue has been found when lexing the command string
     *
     * @param message   The error message
     */
    public LexingException(@Nullable final String message) {
        super(message);
    }
}
