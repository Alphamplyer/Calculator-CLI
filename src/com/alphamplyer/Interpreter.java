package com.alphamplyer;

import com.alphamplyer.ast.*;

import static com.alphamplyer.TokenType.*;

public class Interpreter implements Visitor<Object> {

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    public void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Main.runtimeError(error);
        }
    }

    /**
     * Return a string and a better result display in some case.
     * @param object object to display.
     * @return a nice formed string.
     */
    private String stringify(Object object) {
        if (object == null) return "null";

        // Hack. Work around Java adding ".0" to integer-valued doubles.
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    /**
     * Check if the right operand is equal to 0, and throw a RuntimeError if it is.
     * @param operator the operator of expression
     * @param operand the right operand
     */
    private void checkIfDivideByZero(Token operator, Object operand) {
        if (operand instanceof Double  && (double)operand == 0)
            throw new RuntimeError(operator, "Can't divide by zero");
    }

    /**
     * Check if the operand is a number
     * @param operator the operator of expression
     * @param operand operand to check
     */
    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    /**
     * Check if the operand is a number
     * @param operator the operator of expression
     * @param left the left operand
     * @param right the right operand
     */
    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
            throw new RuntimeError(operator, "Operands must be numbers.");
    }

    /// VISITOR IMPLEMENTATION ////////////////////////////////////////////////

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        // Note :
        // I let "checkNumberOperands" on each, because if I add functions, I would do it anyway.
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left + (double)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                checkIfDivideByZero(expr.operator, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case POW:
                checkNumberOperands(expr.operator, left, right);
                return Math.pow((double)left, (double)right);
        }

        // Unreachable.
        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);

        if (expr.operator.type == MINUS) {
            checkNumberOperand(expr.operator, right);
            return -(double) right;
        }

        // Unreachable.
        return null;
    }
}