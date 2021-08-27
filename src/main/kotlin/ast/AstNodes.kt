package ast

import lexing.BooleanToken
import lexing.NumberToken
import lexing.StringToken
import lexing.Token

data class Program(val statements: List<Statement>) : AstNode {
    override val children = statements

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitProgram(this)
}

typealias BlockId = Int

data class Block(val blockId: BlockId, val statements: List<Statement>) : AstNode {
    override val children: List<AstNode> = statements

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBlock(this)
}

data class FieldAccess(val receiver: Expression, val fieldName: Identifier) : AstNode {
    override val children: List<AstNode> = listOf(receiver, fieldName)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitFieldAccess(this)
}

// Statements

data class VariableDeclarationStatement(val variableName: Identifier, val value: Expression) : Statement {
    override val children = listOf(variableName, value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitVariableDeclarationStatement(this)
}

data class AssignmentStatement(val assignee: AstNode, val value: Expression) : Statement {
    override val children = listOf(assignee, value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitAssignmentStatement(this)
}

data class FunctionDeclarationStatement(
    val name: Identifier,
    val formalArguments: List<Identifier>,
    val statements: List<Statement>
) : Statement {
    override val children: List<AstNode> = listOf(name) + formalArguments + statements

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitFunctionDeclarationStatement(this)
}

data class IfStatement(
    val condition: Expression,
    val thenBlock: Block,
    val elseBlock: Block?
) : Statement {
    override val children = listOfNotNull(condition, thenBlock, elseBlock)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIfStatement(this)
}

data class WhileStatement(
    val condition: Expression,
    val body: Block
) : Statement {
    override val children = listOf(condition, body)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitWhileStatement(this)
}

data class ReturnStatement(
    val value: Expression
) : Statement {
    override val children = listOf(value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitReturnStatement(this)
}

data class ExpressionStatement(
    val expression: Expression
) : Statement {
    override val children = listOf(expression)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitExpressionStatement(this)
}

data class BlockStatement(
    val block: Block
) : Statement {
    override val children = listOf(block)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBlockStatement(this)
}

// Expressions

data class CallExpression(val callee: Expression, val arguments: List<Expression>) : Expression {
    override val children = listOf(callee) + arguments

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitCallExpression(this)
}

data class UnaryOperatorExpression(val operator: Token, val operand: Expression) : Expression {
    override val children = listOf(operand)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitUnaryOperatorExpression(this)
}

data class BinaryOperatorExpression(val operator: Token, val left: Expression, val right: Expression) : Expression {
    override val children = listOf(left, right)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBinaryOperatorExpression(this)
}

data class StringLiteralExpression(val token: StringToken) : Expression {
    val value = token.value
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitStringLiteralExpression(this)
}

data class NumberLiteralExpression(val token: NumberToken) : Expression {
    val value = token.value
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitNumberLiteralExpression(this)
}

data class BooleanLiteralExpression(val token: BooleanToken) : Expression {
    val value = token.value
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBooleanLiteralExpression(this)
}

data class NullLiteralExpression(val token: Token) : Expression {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitNullLiteralExpression(this)
}

data class IdentifierExpression(val identifier: Identifier) : Expression {
    override val children = listOf(identifier)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIdentifierExpression(this)
}

data class FieldAccessExpression(val fieldAccess: FieldAccess) : Expression {
    override val children: List<AstNode> = listOf(fieldAccess)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitFieldAccessExpression(this)
}
