// A tiny arithmetic expression evaluator that parses and computes expressions
// like "1 + 2 * 3" using a hand written recursive descent parser.
//
// Grammar:
//   expr   = term (('+' | '-') term)*
//   term   = factor (('*' | '/') factor)*
//   factor = NUMBER | '(' expr ')'
//
// This evaluator combines parsing and evaluation in one pass.

public class ExprEvaluator {

    // The full input expression
    private final String input;

    // Current position (cursor) inside the input string
    private int pos;

    public ExprEvaluator(String input) {
        this.input = input;
        this.pos = 0;
    }

    // Entry point: parse a full expression and return its numeric result
    public int evaluate() {
        int value = parseExpression();

        // After parsing the expression, consume any remaining whitespace
        skipWhitespace();

        // If we are not at the end, the input had extra garbage
        if (pos != input.length()) {
            throw new IllegalStateException("Unexpected characters at end: " + input.substring(pos));
        }

        return value;
    }

    // ------------------------------------------------------------
    // Grammar rule:
    //   expr = term (('+' | '-') term)*
    //
    // This handles + and - at the top level
    // ------------------------------------------------------------
    private int parseExpression() {
        int value = parseTerm();

        // After reading the first term, keep consuming + term or - term
        while (true) {
            skipWhitespace();
            char c = peek();

            if (c == '+') {
                match('+');
                int right = parseTerm();
                value = value + right;

            } else if (c == '-') {
                match('-');
                int right = parseTerm();
                value = value - right;

            } else {
                // No more + or - operators, expression ends here
                break;
            }
        }

        return value;
    }

    // ------------------------------------------------------------
    // Grammar rule:
    //   term = factor (('*' | '/') factor)*
    //
    // This handles * and /, which have higher precedence,
    // so this is called before parseExpression processes + and -.
    // ------------------------------------------------------------
    private int parseTerm() {
        int value = parseFactor();

        // Keep consuming * factor or / factor
        while (true) {
            skipWhitespace();
            char c = peek();

            if (c == '*') {
                match('*');
                int right = parseFactor();
                value = value * right;

            } else if (c == '/') {
                match('/');
                int right = parseFactor();
                value = value / right;  // integer division

            } else {
                break;
            }
        }

        return value;
    }

    // ------------------------------------------------------------
    // Grammar rule:
    //   factor = NUMBER | '(' expr ')'
    //
    // A factor is either a literal number or a parenthesized expression.
    // ------------------------------------------------------------
    private int parseFactor() {
        skipWhitespace();
        char c = peek();

        // Case 1: expression inside parentheses
        if (c == '(') {
            match('(');                 // consume '('
            int value = parseExpression(); // recursively parse inside
            if (!match(')')) {
                throw new IllegalStateException("Expected ')' at position " + pos);
            }
            return value;

            // Case 2: number
        } else {
            return parseNumber();
        }
    }

    // ------------------------------------------------------------
    // Parse a sequence of digits and return the integer value.
    // Example: input "123 + ..." should return 123 and move pos forward.
    // ------------------------------------------------------------
    private int parseNumber() {
        skipWhitespace();
        int start = pos;

        // Read all consecutive digits
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }

        // If no digits were found, that's a syntax error
        if (start == pos) {
            throw new IllegalStateException("Expected number at position " + pos);
        }

        // Convert the substring to an integer
        String slice = input.substring(start, pos);
        return Integer.parseInt(slice);
    }

    // ------------------------------------------------------------
    // Utility: consume whitespace characters
    // ------------------------------------------------------------
    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }

    // ------------------------------------------------------------
    // Utility: try to match a specific character.
    // If the next non whitespace character is `expected`, consume it and return true.
    // Otherwise return false and do NOT consume anything.
    //
    // This method is where the cursor invariant lives.
    // ------------------------------------------------------------
    private boolean match(char expected) {
        skipWhitespace();

        if (pos < input.length() && input.charAt(pos) == expected) {
            pos++;  // consume the character
            return true;
        }

        return false; // do not consume anything if it does not match
    }

    // ------------------------------------------------------------
    // Utility: look at the next non whitespace character without consuming it.
    // Returns '\0' if we reached the end.
    // ------------------------------------------------------------
    private char peek() {
        skipWhitespace();
        if (pos >= input.length()) {
            return '\0';
        }
        return input.charAt(pos);
    }
}