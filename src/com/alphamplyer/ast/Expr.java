package com.alphamplyer.ast;

public abstract class Expr {
    public abstract <R> R accept(Visitor<R> visitor);
}
