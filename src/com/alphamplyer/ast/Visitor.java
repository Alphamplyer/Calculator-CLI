package com.alphamplyer.ast;

public interface Visitor<R> {

    public R visitBinaryExpr(Binary expr);

    public R visitGroupingExpr(Grouping expr);

    public R visitLiteralExpr(Literal expr);

    public R visitUnaryExpr(Unary expr);
}
