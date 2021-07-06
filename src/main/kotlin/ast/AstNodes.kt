package ast

class Program(val statements: List<Statement>) : AstNode {
    override val children = statements

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitProgram(this)

}

class Block(val statements: List<Statement>) : AstNode {
    override val children: List<AstNode> = statements

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBlock(this)

}

// Statements

class VariableDeclarationStatement(val variableName: Identifier, val value: Expression) : Statement {
    override val children = listOf(variableName, value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitVariableDeclarationStatement(this)

}

class AssignmentStatement(val assignee: AstNode, val value: Expression) : Statement {
    override val children = listOf(assignee, value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitAssignmentStatement(this)

}

class FunctionDeclarationStatement(
    val name: Identifier,
    val formalArguments: List<Identifier>,
    val body: Block
) : Statement {
    override val children: List<AstNode> = listOf(name) + formalArguments + listOf(body)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitFunctionDeclarationStatement(this)

}

class IfStatement(
    val condition: Expression,
    val thenBlock: Block,
    val elseBlock: Block?
) : Statement {
    override val children = listOfNotNull(condition, thenBlock, elseBlock)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIfStatement(this)

}

class WhileStatement(
    val condition: Expression,
    val body: Block
) : Statement {
    override val children = listOf(condition, body)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitWhileStatement(this)

}

class ReturnStatement(
    val value: Expression
) : Statement {
    override val children = listOf(value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitReturnStatement(this)

}

class PrintStatement(
    val value: Expression
) : Statement {
    override val children = listOf(value)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitPrintStatement(this)

}

class ExpressionStatement(
    val expression: Expression
) : Statement {
    override val children = listOf(expression)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitExpressionStatement(this)

}

class BlockStatement(
    val block: Block
) : Statement {
    override val children = listOf(block)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBlockStatement(this)

}

// Expressions

class CallExpression(val callee: Expression, val arguments: List<Expression>) : Expression {
    override val children = listOf(callee) + arguments

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitCallExpression(this)

}

class UnaryOperatorExpression(val operator: Token, val operand: Expression) : Expression {
    override val children = listOf(operand)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitUnaryOperatorExpression(this)

}

class BinaryOperatorExpression(val operator: Token, val left: Expression, val right: Expression) : Expression {
    override val children = listOf(left, right)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBinaryOperatorExpression(this)

}

class StringLiteralExpression(val value: String) : Expression {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitStringLiteralExpression(this)

}

class NumberLiteralExpression(val value: Int) : Expression {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitNumberLiteralExpression(this)

}

class BooleanLiteralExpression(val value: Boolean) : Expression {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitBooleanLiteralExpression(this)

}

object NullLiteralExpression : Expression {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitNullLiteralExpression(this)

}

class IdentifierExpression(val identifier: Identifier) : Expression {
    override val children = listOf(identifier)

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIdentifierExpression(this)

}
