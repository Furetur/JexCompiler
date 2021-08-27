package lexing


sealed class Token(val text: String, val type: TokenType, val line: Int, val position: Int) {
    override fun toString(): String = "Token '$text' at $line:$position"
}

enum class TokenType(val description: String, val isSimple: Boolean = true) {
    // ------------
    // Keywords

    Var("var"),
    If("if"),
    Else("else"),
    While("while"),
    Fn("fn"),
    Return("return"),

    // ------------
    // Operators

    Dot("."),
    Assign("="),
    // Arithmetic
    Plus("+"),
    Minus("-"),
    Mul("*"),
    Div("/"),
    // Logic
    And("&&"),
    Or("||"),
    Not("!"),
    // Comparison and equality
    Eq("=="),
    Neq("!="),
    Lt("<"),
    Lte("<="),
    Gt(">"),
    Gte(">="),

    // ------------
    // Punctuation

    Comma(","),
    Semi(";"),
    LParen("("),
    RParen(")"),
    LBrace("{"),
    RBrace("}"),

    // ------------
    // Name

    Name("name", isSimple = false),

    // ------------
    // Literal

    Num("number literal", isSimple = false),
    Str("string literal", isSimple = false),
    Bool("boolean literal", isSimple = false),
    Null("null");

    override fun toString(): String = description
}

val unaryOperators = listOf(TokenType.Minus, TokenType.Not)

val relationOperators = listOf(TokenType.Eq, TokenType.Neq, TokenType.Gt, TokenType.Gte, TokenType.Lt, TokenType.Lte)

val additiveOperators = listOf(TokenType.Plus, TokenType.Minus, TokenType.Or)

val multiplicativeOperators = listOf(TokenType.Mul, TokenType.Div, TokenType.And)

val binaryOperators = relationOperators + additiveOperators + multiplicativeOperators

class SimpleToken(type: TokenType, line: Int, position: Int) : Token(type.description, type, line, position) {
    init {
        require(type.isSimple) { "Attempted to construct a SimpleToken of non-simple type $type" }
    }
}

class NameToken(text: String, line: Int, position: Int) : Token(text, TokenType.Name, line, position)

class NumberToken(val value: Int, line: Int, position: Int) : Token(value.toString(), TokenType.Num, line, position)

class BooleanToken(val value: Boolean, line: Int, position: Int) : Token(value.toString(), TokenType.Bool, line, position)

class StringToken(val value: String, line: Int, position: Int) : Token(value, TokenType.Str, line, position)
