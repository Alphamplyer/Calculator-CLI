package com.alphamplyer;

import com.alphamplyer.ast.Expr;

import java.util.List;
import static com.alphamplyer.TokenType.*;
import com.alphamplyer.ast.*;

public class Parser {
    private static class ParseError extends RuntimeException {}


    private final List<Token> tokens;
    private int current = 0;

    public Parser (List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * advance the current token position if it is not the end of the list
     * @return return the current token before advance the current position.
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * is it the end token ?
     * @return true if it is, false if it isn't.
     */
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    /**
     * Give the current token.
     * @return the current token.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Give the previous token.
     * @return the previous token.
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * Check if the given tokens match with the current token, if it is, advance the pointer.
     * @param types the given tokens.
     * @return true if it match, false if it not match.
     */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the given token is the same as current.
     * @param type given token.
     * @return true if it is, false if it isn't.
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Main.error(token, message);
        return new ParseError();
    }

    /// PARSING CALL //////////////////////////////////////////////////////////////////////////////////////

    public Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    /// RULES /////////////////////////////////////////////////////////////////////////////////////////////

    private Expr expression () {
        return addition();
    }

    private Expr addition () {
        Expr expr = multiplication();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr multiplication () {
        Expr expr = unary();

        while (match(SLASH, STAR, POW)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary () {
        if (match(MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {

        if (match(NUMBER)) {
            return new Literal(previous().value);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }
}
