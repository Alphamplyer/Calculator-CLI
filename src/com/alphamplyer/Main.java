package com.alphamplyer;

import com.alphamplyer.ast.AstPrinter;
import com.alphamplyer.ast.Expr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        runPrompt();
    }

    /**
     * Run a command prompt interpreter.
     * @throws IOException throw if IO error.
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print("> ");
            run(reader.readLine());
            hadError = false;
            hadRuntimeError = false;
        }
    }

    /**
     * Run the lexer on source.
     * @param source the file text or the command prompt line.
     */
    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        // Uncomment to see parser result :
        // System.out.println(new AstPrinter().print(expression));

        interpreter.interpret(expression);
    }

    /**
     * Report an error.
     * @param message error message.
     */
    public static void error(String message) {
        report("", message);
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report("At end", message);
        } else {
            report(" At '" + token.lexeme + "'", message);
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage());
        hadRuntimeError = true;
    }

    /**
     * Report an error.
     * @param where where is the error.
     * @param message error message.
     */
    private static void report(String where, String message) {
        System.err.println("Error" + where + ": " + message);
        hadError = true;
    }


}
