package com.alphamplyer;

import java.util.ArrayList;
import java.util.List;

import static com.alphamplyer.TokenType.*;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int startPointerPos = 0;
    private int pointerPos = 0;

    public Lexer (String source) {
        this.source = source;
    }

    /**
     * Check if the pointer is at end of the source.
     * @return if it is at end, true, else false.
     */
    private boolean isAtEnd () {
        return pointerPos >= source.length();
    }

    /**
     * move the cursor to the next char and return it.
     * @return return the next char.
     */
    private char advance () {
        return source.charAt(pointerPos++);
    }

    /**
     * Check if the next char is the expected char, and increase pointer if true.
     * @param expected the next expected char.
     * @return if the next char is the expected char, true, else false.
     */
    private boolean match (char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(pointerPos) != expected) return false;
        pointerPos++;
        return true;
    }

    /**
     * Get the current char
     * @return the current char or '\0' if the pointer is at the end of the source
     */
    private char peek () {
        if (isAtEnd()) return '\0';
        return source.charAt(pointerPos);
    }

    /**
     * Get the current + 1 char
     * @return the current + 1 char or '\0' if the pointer is at the end of the source
     */
    private char peekNext () {
        if (pointerPos + 1 >= source.length()) return '\0';
        return source.charAt(pointerPos + 1);
    }

    /**
     * Is the char is a number.
     * @param c char to test.
     * @return true if is it, false if is not.
     */
    private boolean isDigit (char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Build a number token
     */
    private void number () {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(startPointerPos, pointerPos)));
    }

    /**
     * Add token to the token list.
     * @param type token type.
     */
    private void addToken (TokenType type) {
        addToken(type, null);
    }

    /**
     * Add token to the tokens list.
     * @param type token type.
     * @param literal value of the token.
     */
    private void addToken (TokenType type, Object literal) {
        String text = source.substring(startPointerPos, pointerPos);
        tokens.add(new Token(type, text, literal));
    }

    public List<Token> scanTokens () {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            startPointerPos = pointerPos;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null));
        return tokens;
    }

    private void scanToken () {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN);     break;
            case ')': addToken(RIGHT_PAREN);    break;

            case '-': addToken(MINUS);          break;
            case '+': addToken(PLUS);           break;
            case '/': addToken(SLASH);          break;
            case '*': addToken(match('*') ? POW : STAR); break;

            // Ignore whitespace (' ', '\r', '\t')
            case ' ':
            case '\r':
            case '\t':
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else {
                    Main.error("Unexpected character.");
                }
                break;
        }
    }
}
