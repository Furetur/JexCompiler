package parsing.expressions

import ast.BinaryOperatorExpression
import ast.Expression
import lexing.*
import parsing.Parser

/**
 * Readable grammar:
 *
 * binaryExpression: nonBinaryExpression (BINOP nonBinaryExpression)+
 *
 * or with precedence:
 * binaryExpression
 *      : expression op=(MUL|DIV|AND) expression
 *      | expression op=(ADD|SUB|OR) expression
 *      | expression rel=(GT|GTE|LT|LTE|EQ|NEQ) expression
 *
 * Grammar used for parsing:
 *
 * binaryExpression: relationExpression;
 * relationExpression: additiveExpression (REL additiveExpression)*;
 * additiveExpression: multiplicativeExpression (ADDOP multiplicativeExpression)*;
 * multiplicativeExpression: nonBinaryExpression (MULOP nonBinaryExpression)*;
 *
 * relationExpression, additiveExpression and multiplicativeExpression are binary operator expressions.
 *
 */
object BinaryExpressionParser : Parser<Expression> {
    override fun parse(tokenReader: TokenReader): Expression = parseRelationExpression(tokenReader)

    private fun parseRelationExpression(tokenReader: TokenReader): Expression =
        parseBinaryOperatorExpression(tokenReader, relationOperators, ::parseAdditiveExpression)

    private fun parseAdditiveExpression(tokenReader: TokenReader): Expression =
        parseBinaryOperatorExpression(tokenReader, additiveOperators, ::parseMultiplicativeExpression)

    private fun parseMultiplicativeExpression(tokenReader: TokenReader): Expression =
        parseBinaryOperatorExpression(tokenReader, multiplicativeOperators, NonBinaryExpressionParser::parse)

    private fun parseBinaryOperatorExpression(
        tokenReader: TokenReader,
        currentOperators: List<TokenType>,
        parseNextExpressionLevel: (TokenReader) -> Expression
    ): Expression {
        var result = parseNextExpressionLevel(tokenReader)

        while (true) {
            if (tokenReader.peek()?.type in currentOperators) {
                val relation = tokenReader.advance()
                requireNotNull(relation)
                val expression = parseNextExpressionLevel(tokenReader)
                result = BinaryOperatorExpression(relation, result, expression)
            } else {
                break
            }
        }
        return result
    }
}
