package parsing.expressions

import ast.*
import lexing.*
import parsing.Parser


// expression
//   : binaryExpression
//   | nonBinaryExpression
//
// binaryExpression: nonBinaryExpression (BINOP nonBinaryExpression)+
//
// nonBinaryExpression
//   : literalExpression
//   | chainExpression
//   | LPAREN expression RPAREN
//   | UNOP nonBinaryExpression
//
// literalExpression
//   : STRINGLITERAL # StringLiteralExpr
//   | number=NUMBER # NumberLiteralExpr
//   | booleanLiteral # BooleanLiteralExpr
//   | ID # IdLiteralExpr
//   | NULL # NullExpr
//
// chainExpression
//   : idOrFunctionCall (DOT idOrFunctionCall)+
//
// idOrFunctionCall
//   : ID
//   | ID LPAREN commaSeparatedExpressions? RPAREN

/**
 * expression
 *   : binaryExpression
 *   | nonBinaryExpression
 */
object ExpressionParser : Parser<Expression> {
    override fun parse(tokenReader: TokenReader): Expression {
        // binaryExpression always starts with nonBinaryExpression
        // we parse nonBinaryExpression and then look for operator
        val start = tokenReader.mark()
        val nonBinaryExpression = NonBinaryExpressionParser.parse(tokenReader)
        return if (tokenReader.peek()?.type in binaryOperators) {
            // it was a binary expression. Let's parse it again
            start.jump()
            BinaryExpressionParser.parse(tokenReader)
        } else {
            nonBinaryExpression
        }
    }
}



/**
 * nonBinaryExpression
 *   : literalExpression
 *   | chainExpression
 *   | LPAREN expression RPAREN
 *   | UNOP nonBinaryExpression
 *
 * Starts with STRINGLITERAL, NUMBER, BOOLEANLITERAL, NULL, ID, LPAREN, UNOP
 */
object NonBinaryExpressionParser : Parser<Expression> {
    override fun parse(tokenReader: TokenReader): Expression {
        val token = tokenReader.peek()
        requireNotNull(token) { "Expected a token while was parsing an expression" }
        return when (token.type) {
            TokenType.LParen -> {
                tokenReader.require(TokenType.LParen)
                val expression = ExpressionParser.parse(tokenReader)
                tokenReader.require(TokenType.RParen)
                expression
            }
            in unaryOperators -> {
                val operatorToken = tokenReader.advance()
                requireNotNull(operatorToken) { "Should not be null" }
                val operand = NonBinaryExpressionParser.parse(tokenReader)
                UnaryOperatorExpression(operatorToken, operand)
            }
            TokenType.Str, TokenType.Num, TokenType.Bool, TokenType.Null -> {
                val literalExpression = LiteralExpressionParser.parse(tokenReader)
                literalExpression
            }
            TokenType.Name -> ChainExpressionParser.parse(tokenReader)
            else -> error("Unexpected token")
        }
    }
}

/**
 * literalExpression
 *   : STRINGLITERAL
 *   | NUMBER
 *   | BOOLEANLITERAL
 *   | NULL
 */
private object LiteralExpressionParser : Parser<Expression> {
    override fun parse(tokenReader: TokenReader): Expression {
        val token = tokenReader.advance()
        requireNotNull(token) { "Expected string literal, number literal, boolean literal, name or null but received nothing" }
        return when (token.type) {
            TokenType.Str -> StringLiteralExpression(token as StringToken)
            TokenType.Num -> NumberLiteralExpression(token as NumberToken)
            TokenType.Bool -> BooleanLiteralExpression(token as BooleanToken)
            TokenType.Null -> NullLiteralExpression(token)
            else -> throw IllegalArgumentException("Expected a literal but got $token")
        }
    }
}

/**
 * chainExpression
 *   : idOrFunctionCall (DOT idOrFunctionCall)*
 *
 * idOrFunctionCall
 *   : ID
 *   | ID LPAREN commaSeparatedExpressions? RPAREN
 */
private object ChainExpressionParser : Parser<Expression> {
    override fun parse(tokenReader: TokenReader): Expression {
        val idsOrFunctionCalls = parseChain(tokenReader)
        assert(idsOrFunctionCalls.isNotEmpty()) { "Internal error: Chain was empty" }

        val start = idsOrFunctionCalls.first()

        var currentNode = if (start.callArguments == null) {
            IdentifierExpression(start.id)
        } else {
            CallExpression(IdentifierExpression(start.id), start.callArguments)
        }

        for (idOrFunctionCall in idsOrFunctionCalls.drop(1)) {
            val fieldAccessOfCurrentNode = FieldAccessExpression(FieldAccess(currentNode, idOrFunctionCall.id))
            currentNode = if (idOrFunctionCall.callArguments == null) {
                fieldAccessOfCurrentNode
            } else {
                CallExpression(fieldAccessOfCurrentNode, idOrFunctionCall.callArguments)
            }
        }
        return currentNode
    }

    private fun parseChain(tokenReader: TokenReader): List<IdOrFunctionCall> {
        val idsOrFunctionCalls = mutableListOf<IdOrFunctionCall>()
        while (true) {
            idsOrFunctionCalls.add(parseIdOrFunctionCall(tokenReader))
            if (tokenReader.peek()?.type != TokenType.Dot) {
                break
            } else {
                tokenReader.require(TokenType.Dot)
            }
        }
        return idsOrFunctionCalls
    }

    private fun parseIdOrFunctionCall(tokenReader: TokenReader): IdOrFunctionCall {
        val name = tokenReader.requireName()
        return if (tokenReader.peek()?.type == TokenType.LParen) {
            tokenReader.require(TokenType.LParen)
            val arguments = parseCommaSeparatedExpressions(tokenReader)
            tokenReader.require(TokenType.RParen)
            IdOrFunctionCall(Identifier(name), arguments)
        } else {
            IdOrFunctionCall(Identifier(name), null)
        }
    }

    data class IdOrFunctionCall(val id: Identifier, val callArguments: List<Expression>?)
}

/**
 * commaSeparatedExpressions: expression (COMMA expression)*
 */
private fun parseCommaSeparatedExpressions(tokenReader: TokenReader): List<Expression> {
    val expressions = mutableListOf<Expression>()

    while (true) {
        expressions.add(ExpressionParser.parse(tokenReader))
        if (tokenReader.peek()?.type != TokenType.Comma) {
            break
        } else {
            tokenReader.require(TokenType.Comma)
        }
    }
    return expressions
}

/**
 * UNOP nonBinaryExpression
 */
private object UnaryOperatorExpressionParser : Parser<UnaryOperatorExpression> {
    override fun parse(tokenReader: TokenReader): UnaryOperatorExpression {
        val operator = tokenReader.advance()
        require(operator?.text in listOf("-", "!")) { "Expected - or ! but received $operator" }
        val value = NonBinaryExpressionParser.parse(tokenReader)
        return UnaryOperatorExpression(operator!!, value)
    }
}


