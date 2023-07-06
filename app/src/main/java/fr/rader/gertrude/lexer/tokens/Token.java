package fr.rader.gertrude.lexer.tokens;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is used by the lexer to turn the command string to a Discord command
 */
public final class Token {

    private final TokenKind kind;
    private final String lexeme;

    /**
     * Create a new token with the given kind and lexeme
     *
     * @param kind      The token kind (see {@link TokenKind})
     * @param lexeme    The lexeme
     */
    public Token(@NotNull final TokenKind kind, @NotNull final String lexeme) {
        this.kind = kind;
        this.lexeme = lexeme;
    }

    /**
     * @return  The token kind
     */
    @NotNull
    public TokenKind getKind() {
        return this.kind;
    }

    /**
     * @return  The token lexeme
     */
    @NotNull
    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public String toString() {
        return "Token{" +
                "kind=" + this.kind +
                ", lexeme='" + this.lexeme + '\'' +
                '}';
    }
}
