package ast

interface AstNode {
    val children: List<AstNode>

    fun <T> acceptVisitor(visitor: AstVisitor<T>): T
}

interface Expression : AstNode

interface Statement : AstNode

abstract class TerminalAstNode : Expression {
    abstract val token: Token
    override val children: List<AstNode> = emptyList()
}

class Identifier(val token: Token) : AstNode {
    override val children = emptyList<AstNode>()

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIdentifier(this)
}

data class Token(val text: String, val line: Int, val position: Int)