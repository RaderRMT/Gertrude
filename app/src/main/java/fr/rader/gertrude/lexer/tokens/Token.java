package fr.rader.gertrude.lexer.tokens;

public final class Token {

    private final TokenKind kind;
    private final String lexeme;

    public Token(TokenKind kind, String lexeme) {
        this.kind = kind;
        this.lexeme = lexeme;
    }

    public TokenKind getKind() {
        return this.kind;
    }

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
