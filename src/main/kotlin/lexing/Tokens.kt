package lexing


sealed class Token(val text: String, val line: Int, val position: Int) {
    override fun toString(): String = "Token '$text' at $line:$position"
}

enum class KeywordType(val text: String) {
    Var("var"),
    If("if"),
    Else("else"),
    While("while"),
    Fn("fn"),
    Return("return");
}

class KeywordToken(val type: KeywordType, line: Int, position: Int) : Token(type.text, line, position)

enum class OperatorType(val text: String) {
    // assign
    Assign("="),

    // arithmetic
    Plus("+"),
    Minus("-"),
    Mul("*"),
    Div("/"),

    // logic
    And("&&"),
    Or("||"),
    Not("!"),

    // equality
    Eq("=="),
    Neq("!="),

    // comparison
    Lt("<"),
    Lte("<="),
    Gt(">"),
    Gte(">="),
    // Dot
    Dot(".");
}

class OperatorToken(val type: OperatorType, line: Int, position: Int) : Token(type.text, line, position)

enum class PunctuationType(val text: String) {
    Comma(","),
    Semi(";"),
    LParen("("),
    RParen(")"),
    LBrace("{"),
    RBrace("}")
}

class PunctuationToken(val type: PunctuationType, line: Int, position: Int) : Token(type.text, line, position)

class NameToken(text: String, line: Int, position: Int) : Token(text, line, position)

class NumberToken(val value: Int, line: Int, position: Int) : Token(value.toString(), line, position)

class BooleanToken(val value: Boolean, line: Int, position: Int) : Token(value.toString(), line, position)

class StringToken(val value: String, line: Int, position: Int) : Token(value, line, position)

class NullToken(line: Int, position: Int) : Token("null", line, position)
