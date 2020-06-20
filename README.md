# Calculator CLI

An interpreter for do some calculations and test my architecture before 
doing a programming language. This is not the fastest way, and the best 
programming language to make an interpreter but, for me, it's OK when you 
just need to understand how programming language were created, and how I 
can reproduce them. 


## Technologies used
Only done with Java JDK 13.0.1

## Available signs
```
*  : Multiplication
** : Pow
/  : Division
+  : Addition
-  : Substraction
() : Parenthesis
```

## Some calculations

```
> -1 * (10 + 4) / 2
-7

> 2 ** 2
4

> 10 / (3 - 2 - 1)
Can't divide by Zero
```

## Know Issue
- I don't know why but, when I divide by 0, I have a problem of display 
of the thrown error (the `>` char is write before the error). Possibly due
to RuntimeException used to throw the exception. 