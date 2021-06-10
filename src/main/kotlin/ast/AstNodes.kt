package ast

class Program(val statements: List<Statement>) : AstNode {
    override val children = statements
}

class Block(val statements: List<Statement>) : AstNode {
    override val children: List<AstNode> = statements
}

// Statements

class VariableDeclarationStatement(val variableName: Identifier, val value: Expression) : Statement {
    override val children = listOf(variableName, value)
}

class AssignmentStatement(val assignee: AstNode, val value: Expression) : Statement {
    override val children = listOf(assignee, value)
}

class FunctionDeclarationStatement(
    val name: Identifier,
    val formalArguments: List<Identifier>,
    val body: Block
) : Statement {
    override val children: List<AstNode> = listOf(name) + formalArguments + listOf(body)
}

class IfStatement(
    val condition: Expression,
    val thenBlock: Block,
    val elseBlock: Block?
) : Statement {
    override val children = listOfNotNull(condition, thenBlock, elseBlock)
}

class WhileStatement(
    val condition: Expression,
    val body: Block
) : Statement {
    override val children = listOf(condition, body)
}

class ReturnStatement(
    val value: Expression
) : Statement {
    override val children = listOf(value)
}

class PrintStatement(
    val value: Expression
) : Statement {
    override val children = listOf(value)
}

class ExpressionStatement(
    val expression: Expression
) : Statement {
    override val children = listOf(expression)
}

class BlockStatement(
    val block: Block
) : Statement {
    override val children = listOf(block)
}

// Expressions

class CallExpression(val callee: Expression, val arguments: List<Expression>) : Expression {
    override val children = listOf(callee) + arguments
}

class UnaryOperatorExpression(val operator: Token, val operand: Expression) : Expression {
    override val children = listOf(operand)
}

class BinaryOperatorExpression(val operator: Token, val left: Expression, val right: Expression) : Expression {
    override val children = listOf(left, right)
}

class StringLiteralExpression(val value: String) : Expression {
    override val children = emptyList<AstNode>()
}

class NumberLiteralExpression(val value: Int) : Expression {
    override val children = emptyList<AstNode>()
}

class BooleanLiteralExpression(val value: Boolean) : Expression {
    override val children = emptyList<AstNode>()
}

object NullLiteralExpression : Expression {
    override val children = emptyList<AstNode>()
}

class IdentifierExpression(val identifier: Identifier) : Expression {
    override val children = listOf(identifier)
}




