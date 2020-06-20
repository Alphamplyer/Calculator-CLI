package com.alphamplyer;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object value;

    /**
     * Token Constructor
     * @param type token type
     * @param lexeme string who make that recognize as a token
     * @param value token value
     */
    public Token (TokenType type, String lexeme, Object value) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = value;
    }

    public String toString() {
        return !lexeme.toString().isEmpty() ? "[type = " + type + "] lexeme = " + lexeme : "[type = " + type + "]";
    }
}
