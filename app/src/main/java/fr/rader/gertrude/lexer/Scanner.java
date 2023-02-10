package fr.rader.gertrude.lexer;

import fr.rader.gertrude.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Read the command string token by token, where a token is either delimited by spaces, diamonds or square brackets
 */
public final class Scanner {

    private final Tokenizer tokenizer;

    // A buffer of saved tokens.
    // This is used when using lookahead
    private final List<Token> savedTokens;

    // The current token (set by nextToken())
    private Token currentToken;

    /**
     * Create a new Scanner with the given command
     *
     * @param command   The command to read
     */
    public Scanner(String command) {
        this.savedTokens = new ArrayList<>();
        this.tokenizer = new Tokenizer(command);

        this.currentToken = null;

        // load the scanner with the first token
        nextToken();
    }

    /**
     * Return the token at the given lookahead.
     *
     * @param lookahead     The position to get the token at
     * @return              The token at the given lookahead
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
     * @return  Return the current token
     */
    public Token token() {
        return token(0);
    }

    /**
     * Consume the next token.
     */
    public void nextToken() {
        if (!this.savedTokens.isEmpty()) {
            this.currentToken = this.savedTokens.remove(0);
        } else {
            this.currentToken = this.tokenizer.readToken();
        }
    }
}
