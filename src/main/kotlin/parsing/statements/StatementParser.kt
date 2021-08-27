package parsing.statements

import ast.*
import lexing.*
import parsing.Parser
import parsing.expressions.ExpressionParser
import parsing.structure.BlockParser

/**
 * statement
 *   : VAR ID ASSIGN expression SEMI
 *   | block
 *   | functionDefinition
 *   | IF LPAREN expression RPAREN block (ELSE block)?
 *   | WHILE LPAREN expression RPAREN block
 *   | RETURN expression SEMI
 *   | expression ASSIGN expression SEMI
 *   | expression SEMI
 *   ;
 */
object StatementParser : Parser<Statement> {
    override fun parse(tokenReader: TokenReader): Statement {
        val nextToken = tokenReader.peek()
        requireNotNull(nextToken) { "Expected a statement but code ended" }
        return when (nextToken.type) {
            TokenType.Var -> VarDeclarationStatementParser.parse(tokenReader)
            TokenType.LBrace -> BlockStatementParser.parse(tokenReader)
            TokenType.Fn -> FunctionDeclarationParser.parse(tokenReader)
            TokenType.If -> IfStatementParser.parse(tokenReader)
            TokenType.While -> WhileStatementParser.parse(tokenReader)
            TokenType.Return -> ReturnStatementParser.parse(tokenReader)
            else -> { // assignment statement or expression statement
                val beforeExpressionMark = tokenReader.mark()
                ExpressionParser.parse(tokenReader)
                val nextTokenAfterExpression = tokenReader.peek()
                beforeExpressionMark.jump()
                when (nextTokenAfterExpression?.type) {
                    TokenType.Assign -> AssignmentStatementParser.parse(tokenReader)
                    TokenType.Semi -> ExpressionStatementParser.parse(tokenReader)
                    else -> throw IllegalArgumentException("Failed to parse statement that starts at $nextToken")
                }
            }
        }
    }
}

/**
 * VAR ID ASSIGN expression SEMI
 */
object VarDeclarationStatementParser : Parser<VariableDeclarationStatement> {
    override fun parse(tokenReader: TokenReader): VariableDeclarationStatement {
        tokenReader.require(TokenType.Var)
        val name = tokenReader.advance()
        require(name is NameToken) { "Expected a variable name but received $name" }
        tokenReader.require(TokenType.Assign)
        val expression = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.Semi)
        return VariableDeclarationStatement(Identifier(name), expression)
    }
}

/**
 * block
 */
object BlockStatementParser : Parser<BlockStatement> {
    override fun parse(tokenReader: TokenReader): BlockStatement =
        BlockStatement(BlockParser.parse(tokenReader))
}

/**
 * FN ID LPAREN functionFormalArguments? RPAREN block
 */
object FunctionDeclarationParser : Parser<FunctionDeclarationStatement> {
    override fun parse(tokenReader: TokenReader): FunctionDeclarationStatement {
        tokenReader.require(TokenType.Fn)
        val name = tokenReader.requireName()
        tokenReader.require(TokenType.LParen)
        val formalArguments = parseFormalArguments(tokenReader)
        tokenReader.require(TokenType.RParen)
        val block = BlockParser.parse(tokenReader)
        return FunctionDeclarationStatement(Identifier(name), formalArguments.map { Identifier(it) }, block.statements)
    }

    private fun parseFormalArguments(tokenReader: TokenReader): List<NameToken> {
        val formalArguments = mutableListOf<NameToken>()
        tokenReader.until(TokenType.RParen) {
            formalArguments.add(tokenReader.requireName())
            tokenReader.require(TokenType.Comma)
        }
        return formalArguments
    }
}

/**
 * IF LPAREN expression RPAREN thenBlock=block (ELSE elseBlock=block)?
 */
object IfStatementParser : Parser<IfStatement> {
    override fun parse(tokenReader: TokenReader): IfStatement {
        tokenReader.require(TokenType.If)
        tokenReader.require(TokenType.LParen)
        val condition = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.RParen)
        val thenBlock = BlockParser.parse(tokenReader)

        val possibleElseKeyword = tokenReader.peek()
        return if (possibleElseKeyword?.type == TokenType.Else) {
            tokenReader.require(TokenType.Else)
            val elseBlock = BlockParser.parse(tokenReader)
            IfStatement(condition, thenBlock, elseBlock)
        } else {
            IfStatement(condition, thenBlock, null)
        }
    }
}

/**
 * WHILE LPAREN expression RPAREN body=block
 */
object WhileStatementParser : Parser<WhileStatement> {
    override fun parse(tokenReader: TokenReader): WhileStatement {
        tokenReader.require(TokenType.While)
        tokenReader.require(TokenType.LParen)
        val condition = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.RParen)
        val body = BlockParser.parse(tokenReader)
        return WhileStatement(condition, body)
    }
}

/**
 * RETURN expression SEMI
 * TODO: return should be possible without expression
 */
object ReturnStatementParser : Parser<ReturnStatement> {
    override fun parse(tokenReader: TokenReader): ReturnStatement {
        tokenReader.require(TokenType.Return)
        val expression = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.Semi)
        return ReturnStatement(expression)
    }
}

/**
 * expression ASSIGN expression SEMI
 */
object AssignmentStatementParser : Parser<AssignmentStatement> {
    override fun parse(tokenReader: TokenReader): AssignmentStatement {
        val assignee = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.Assign)
        val value = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.Semi)
        return AssignmentStatement(assignee, value)
    }
}

/**
 * expression SEMI
 */
object ExpressionStatementParser : Parser<ExpressionStatement> {
    override fun parse(tokenReader: TokenReader): ExpressionStatement {
        val expression = ExpressionParser.parse(tokenReader)
        tokenReader.require(TokenType.Semi)
        return ExpressionStatement(expression)
    }
}



