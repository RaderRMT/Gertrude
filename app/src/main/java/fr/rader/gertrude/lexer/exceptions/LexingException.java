package fr.rader.gertrude.lexer.exceptions;

/**
 * An exception thrown when an issue has been found when lexing the command string
 */
public class LexingException extends RuntimeException {

    /**
     * Throw an exception when an issue has been found when lexing the command string
     *
     * @param message   The error message
     */
    public LexingException(String message) {
        super(message);
    }
}
