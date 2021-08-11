package parsing

import SimpleLanguageBaseVisitor
import ast.*
import org.antlr.v4.runtime.tree.TerminalNode

class AstBuilder : SimpleLanguageBaseVisitor<AstNode?>() {
    var blockId = 0

    override fun visitProg(ctx: SimpleLanguageParser.ProgContext?): Program {
        val statements = ctx?.statement()!!.map { visit(it) }.filterIsInstance<Statement>()
        return Program(statements)
    }

    override fun visitBlock(ctx: SimpleLanguageParser.BlockContext?): Block {
        val statements = ctx?.statement()!!.map { visit(it) }.filterIsInstance<Statement>()
        return Block(blockId++, statements)
    }

    // Statements

    override fun visitVariableDefStmt(ctx: SimpleLanguageParser.VariableDefStmtContext?): VariableDeclarationStatement {
        val variableName = ctx!!.variableDefinition().ID().asIdentifier()
        val value = visit(ctx.variableDefinition().expression()!!) as Expression
        return VariableDeclarationStatement(variableName, value)
    }

    override fun visitVariableAssignmentStmt(ctx: SimpleLanguageParser.VariableAssignmentStmtContext?): AssignmentStatement {
        val variableName = ctx!!.variableAssignment().ID().asIdentifier()
        val value = visit(ctx.variableAssignment().expression()) as Expression
        return AssignmentStatement(variableName, value)
    }

    override fun visitBlockStmt(ctx: SimpleLanguageParser.BlockStmtContext?): BlockStatement {
        return BlockStatement(visitBlock(ctx!!.block()))
    }

    override fun visitExpressionStmt(ctx: SimpleLanguageParser.ExpressionStmtContext?): ExpressionStatement {
        return ExpressionStatement(visit(ctx!!.expression()) as Expression)
    }

    override fun visitIfStmt(ctx: SimpleLanguageParser.IfStmtContext?): IfStatement {
//        val condition = visit(ctx!!.expression()) as Expression
//        val thenBlock = visitBlock(ctx.thenBlock)
//        val elseBlock = ctx.elseBlock?.let { visitBlock(it) }
//        return IfStatement(condition, thenBlock, elseBlock)

        val ifCondition = visit(ctx!!.ifExpression) as Expression
        val elseIfConditionContexts = ctx.expression().drop(1)
        val elseIfConditions = elseIfConditionContexts.map { visit(it) as Expression }
        val ifThenBlock = visitBlock(ctx.ifThenBlock)
        val elseIfThenBlockContexts = ctx.block().drop(1)
        val elseIfThenBlocks = elseIfThenBlockContexts.map { visitBlock(it) }
        val elseBlock = ctx.elseBlock?.let { visitBlock(it) }

        return IfStatement(ifCondition, elseIfConditions, ifThenBlock, elseIfThenBlocks, elseBlock)
    }

    override fun visitWhileStmt(ctx: SimpleLanguageParser.WhileStmtContext?): AstNode {
        val condition = visit(ctx!!.expression()) as Expression
        val bodyBlock = visitBlock(ctx.body)
        return WhileStatement(condition, bodyBlock)
    }

    override fun visitFunctionDefinitionStmt(ctx: SimpleLanguageParser.FunctionDefinitionStmtContext?): AstNode {
        val functionName = ctx!!.functionDefinition().ID().asIdentifier()
        val formalArguments = ctx.functionDefinition().functionArguments()?.ID()?.map { it.asIdentifier() } ?: emptyList()
        val statements = ctx.functionDefinition().functionBody().statement().map { visit(it) }.filterIsInstance<Statement>()
        return FunctionDeclarationStatement(functionName, formalArguments, statements)
    }

    override fun visitReturnStatement(ctx: SimpleLanguageParser.ReturnStatementContext?): AstNode {
        return ReturnStatement(visit(ctx?.expression()!!) as Expression)
    }

    override fun visitNewlineStmt(ctx: SimpleLanguageParser.NewlineStmtContext?) = null

    override fun visitPropertyAssignment(ctx: SimpleLanguageParser.PropertyAssignmentContext?): AssignmentStatement {
        val receiver = visit(ctx!!.receiver!!) as Expression
        val fieldName = ctx.ID().asIdentifier()
        val value = visit(ctx.value) as Expression
        return AssignmentStatement(
            FieldAccess(receiver, fieldName),
            value
        )
    }

    // Expressions

    override fun visitParenExpr(ctx: SimpleLanguageParser.ParenExprContext?): AstNode? {
        return visit(ctx!!.expression())
    }

    override fun visitExpressionCallExpr(ctx: SimpleLanguageParser.ExpressionCallExprContext?): CallExpression {
        val callee = visit(ctx!!.expression()) as Expression
        val arguments = ctx.commaSeparatedExpressions()?.expression()?.map { visit(it) as Expression } ?: emptyList()
        return CallExpression(callee, arguments)
    }

    override fun visitUnaryOperatorExpr(ctx: SimpleLanguageParser.UnaryOperatorExprContext?): UnaryOperatorExpression {
        return UnaryOperatorExpression(ctx!!.op!!.asMyToken(), visit(ctx.expression()) as Expression)
    }

    override fun visitOperatorExpr(ctx: SimpleLanguageParser.OperatorExprContext?): BinaryOperatorExpression {
        return BinaryOperatorExpression(
            ctx!!.op!!.asMyToken(),
            visit(ctx.left) as Expression,
            visit(ctx.right) as Expression
        )
    }

    override fun visitRelExpr(ctx: SimpleLanguageParser.RelExprContext?): BinaryOperatorExpression {
        return BinaryOperatorExpression(
            ctx!!.rel!!.asMyToken(),
            visit(ctx.left) as Expression,
            visit(ctx.right) as Expression
        )
    }

    override fun visitStringLiteralExpr(ctx: SimpleLanguageParser.StringLiteralExprContext?): StringLiteralExpression {
        val stringValue = ctx?.text?.drop(1)?.dropLast(1) ?: ""
        return StringLiteralExpression(stringValue)
    }

    override fun visitNumberLiteralExpr(ctx: SimpleLanguageParser.NumberLiteralExprContext?): NumberLiteralExpression {
        return NumberLiteralExpression(ctx!!.number.text.toInt())
    }

    override fun visitBooleanLiteral(ctx: SimpleLanguageParser.BooleanLiteralContext?): BooleanLiteralExpression {
        return BooleanLiteralExpression(ctx!!.booleanValue.text.toBoolean())
    }

    override fun visitIdLiteralExpr(ctx: SimpleLanguageParser.IdLiteralExprContext?): IdentifierExpression {
        return IdentifierExpression(ctx!!.ID().asIdentifier())
    }

    override fun visitNullExpr(ctx: SimpleLanguageParser.NullExprContext?) = NullLiteralExpression

    override fun visitPropertyAccessExpression(ctx: SimpleLanguageParser.PropertyAccessExpressionContext?): FieldAccessExpression {
        val receiver = visit(ctx!!.receiver!!) as Expression
        val fieldName = ctx.ID().asIdentifier()
        return FieldAccessExpression(FieldAccess(receiver, fieldName))
    }

    // Common

    private fun TerminalNode.asIdentifier(): Identifier = Identifier(symbol!!.asMyToken())

    fun org.antlr.v4.runtime.Token.asMyToken() = Token(text!!, line, charPositionInLine)
}
