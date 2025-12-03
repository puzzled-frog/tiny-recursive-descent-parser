# Tiny Recursive Descent Parser

A minimal hand written recursive descent parser in Java that evaluates arithmetic expressions using a simple grammar:

```
expr   = term (('+' | '-') term)*
term   = factor (('*' | '/') factor)*
factor = NUMBER | '(' expr ')'
```

The parser demonstrates core concepts of top down parsing, operator precedence, and syntactic invariants. It handles integers, basic operators, and parentheses without building an AST, evaluating expressions directly during parsing.

**Example**

```java
ExprEvaluator eval = new ExprEvaluator("(1 + 2) * 3");
System.out.println(eval.evaluate());  // 9
```
