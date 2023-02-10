package fr.rader.gertrude.lexer.tokens;

/**
 * The different kind of tokens we can find in a command string
 */
public enum TokenKind {

    /**
     * This denotes a subcommand or subcommand group
     */
    NAME,

    /**
     * This denotes a required parameters
     */
    PARAM_REQUIRED,

    /**
     * This denotes an optional parameters
     */
    PARAM_OPTIONAL
}
