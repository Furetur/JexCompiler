package lexing

import SimpleLanguageLexer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Vocabulary

fun tokenizeSourceCode(code: String): List<Token> {
    val charStream = CharStreams.fromString(code + "\n")
    val lexer = SimpleLanguageLexer(charStream)
    val vocabulary = lexer.vocabulary
    val antlrTokens = CommonTokenStream(lexer).tokens?.toList()?.filterNotNull() ?: emptyList()
    return antlrTokens.map { it.asMyToken(vocabulary) }
}

fun org.antlr.v4.runtime.Token.asMyToken(vocabulary: Vocabulary) =
    when (val symbolicName = vocabulary.getSymbolicName(type)) {
        "ASSIGN" -> OperatorToken(OperatorType.Assign, line, charPositionInLine)
        "MUL" -> OperatorToken(OperatorType.Mul, line, charPositionInLine)
        "DIV" -> OperatorToken(OperatorType.Div, line, charPositionInLine)
        "ADD" -> OperatorToken(OperatorType.Plus, line, charPositionInLine)
        "SUB" -> OperatorToken(OperatorType.Minus, line, charPositionInLine)
        "EQ" -> OperatorToken(OperatorType.Eq, line, charPositionInLine)
        "NEQ" -> OperatorToken(OperatorType.Neq, line, charPositionInLine)
        "GT" -> OperatorToken(OperatorType.Gt, line, charPositionInLine)
        "GTE" -> OperatorToken(OperatorType.Gte, line, charPositionInLine)
        "LT" -> OperatorToken(OperatorType.Lt, line, charPositionInLine)
        "LTE" -> OperatorToken(OperatorType.Lte, line, charPositionInLine)
        "AND" -> OperatorToken(OperatorType.And, line, charPositionInLine)
        "OR" -> OperatorToken(OperatorType.Or, line, charPositionInLine)
        "NOT" -> OperatorToken(OperatorType.Not, line, charPositionInLine)
        "DOT" -> OperatorToken(OperatorType.Dot, line, charPositionInLine)
        "LPAREN" -> PunctuationToken(PunctuationType.LParen, line, charPositionInLine)
        "RPAREN" -> PunctuationToken(PunctuationType.RParen, line, charPositionInLine)
        "LBRACE" -> PunctuationToken(PunctuationType.LBrace, line, charPositionInLine)
        "RBRACE" -> PunctuationToken(PunctuationType.RBrace, line, charPositionInLine)
        "COMMA" -> PunctuationToken(PunctuationType.Comma, line, charPositionInLine)
        "VAR" -> KeywordToken(KeywordType.Var, line, charPositionInLine)
        "IF" -> KeywordToken(KeywordType.If, line, charPositionInLine)
        "ELSE" -> KeywordToken(KeywordType.Else, line, charPositionInLine)
        "WHILE" -> KeywordToken(KeywordType.While, line, charPositionInLine)
        "FN" -> KeywordToken(KeywordType.Fn, line, charPositionInLine)
        "RETURN" -> KeywordToken(KeywordType.Return, line, charPositionInLine)
        "STRINGLITERAL" -> StringToken(text.drop(1).dropLast(1), line, charPositionInLine)
        "ID" -> NameToken(text, line, charPositionInLine)
        "NUMBER" -> NumberToken(text.toInt(), line, charPositionInLine)
        "NULL" -> NullToken(line, charPositionInLine)
        "TRUE" -> BooleanToken(true, line, charPositionInLine)
        "FALSE" -> BooleanToken(false, line, charPositionInLine)
        else -> error("Internal error: unknown antlr token $symbolicName")
    }

