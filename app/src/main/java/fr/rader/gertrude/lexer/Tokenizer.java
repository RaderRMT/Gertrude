package fr.rader.gertrude.lexer;

import fr.rader.gertrude.lexer.exceptions.LexingException;
import fr.rader.gertrude.lexer.tokens.Token;
import fr.rader.gertrude.lexer.tokens.TokenKind;

/**
 * Turns the given command into tokens.
 */
public final class Tokenizer {

    private final String command;

    private int end = 0;
    private int start = 0;

    /**
     * Create a new tokenizer
     *
     * @param command   The command to read
     */
    public Tokenizer(String command) {
        this.command = command;
    }

    /**
     * Read a token from the command string.
     *
     * @return  The token read from the command string
     */
    public Token readToken() {
        while (hasNext()) {
            // we get the next char
            char c = next();

            switch (c) {
                case ' ':
                    // if it's a space, we ignore all whitespaces, and we try reading a token
                    ignoreWhitespaces();
                    break;

                case '<':
                    // if it's a '<', we read a required parameter token
                    return required();

                case '[':
                    // if it's a '[', we read an optional parameter token
                    return optional();

                default:
                    // if the given character isn't an alpha char, we throw an exception
                    if (!isAlpha(c)) {
                        throw new LexingException("Expected a character matching [a-zA-Z0-9_] but got '" + c + "' instead");
                    }

                    // otherwise, we read a name token
                    return name();
            }
        }

        return null;
    }

    /**
     * Skip all following whitespaces from the command string.
     * This will continue running as long as we read the ' ' char
     */
    private void ignoreWhitespaces() {
        while (peek() == ' ') {
            this.end++;
        }

        this.end++;
        this.start = this.end;
    }

    /**
     * Read a name from the command string.
     * This will continue running until reading a character where {@code isAlpha(char)} returns false
     */
    private Token name() {
        while (hasNext() && isAlpha(next())) {
            this.end++;
        }

        return new Token(
                TokenKind.NAME,
                this.command.substring(this.start, this.end)
        );
    }

    /**
     * Return true if the given character matches the following regex: {@code [a-zA-Z0-9_]}
     */
    private boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' ||
               c >= 'A' && c <= 'Z' ||
               c >= '0' && c <= '9' ||
               c == '_';
    }

    /**
     * Read an optional argument from the command string.
     * This will continue running until reading a '>'
     */
    private Token required() {
        while (next() != '>') {
            this.end++;
        }

        return new Token(
                TokenKind.PARAM_REQUIRED,
                this.command.substring(this.start + 1, this.end++)
        );
    }

    /**
     * Read an optional argument from the command string.
     * This will continue running until reading a ']'
     */
    private Token optional() {
        while (next() != ']') {
            this.end++;
        }

        return new Token(
                TokenKind.PARAM_OPTIONAL,
                this.command.substring(this.start + 1, this.end++)
        );
    }

    /**
     * Return the next character without moving the lexeme end pointer
     */
    private char next() {
        return this.command.charAt(this.end);
    }

    /**
     * Return the second next character without moving the lexeme end pointer
     */
    private char peek() {
        return this.command.charAt(this.end + 1);
    }

    /**
     * @return  true if there are more tokens to read, false otherwise
     */
    public boolean hasNext() {
        return this.end < this.command.length();
    }
}
