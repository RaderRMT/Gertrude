package fr.rader.gertrude.lexer;

import fr.rader.gertrude.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class Scanner {

    private final Tokenizer tokenizer;

    // A buffer of saved tokens.
    // This is used when using lookahead
    private final List<Token> savedTokens;

    // The current token (set by nextToken())
    private Token currentToken;

    // The previous token (set by nextToken())
    private Token previousToken;

    public Scanner(String command) {
        this.savedTokens = new ArrayList<>();
        this.tokenizer = new Tokenizer(command);

        this.currentToken = null;
        this.previousToken = null;

        // load the scanner with the first token
        nextToken();
    }

    /**
     * Return the token at the given lookahead.
     */
    public Token token(int lookahead) {
        if (lookahead == 0) {
            return this.currentToken;
        }

        ensureLookahead(lookahead);
        return this.savedTokens.get(lookahead - 1);
    }

    /**
     * Fill the savedToken list with {@code lookahead} tokens
     */
    private void ensureLookahead(int lookahead) {
        for (int i = this.savedTokens.size(); i < lookahead; i++) {
            this.savedTokens.add(this.tokenizer.readToken());
        }
    }

    /**
     * Return the current token.
     */
    public Token token() {
        return token(0);
    }

    /**
     * Return the previous token.
     */
    public Token previousToken() {
        return this.previousToken;
    }

    /**
     * Consume the next token.
     */
    public void nextToken() {
        this.previousToken = this.currentToken;

        if (!this.savedTokens.isEmpty()) {
            this.currentToken = this.savedTokens.remove(0);
        } else {
            this.currentToken = this.tokenizer.readToken();
        }
    }

    /**
     * Return true if there are more tokens to read, false otherwise
     */
    public boolean hasNext() {
        return this.currentToken != null || this.savedTokens.size() != 0 || this.tokenizer.hasNext();
    }
}
