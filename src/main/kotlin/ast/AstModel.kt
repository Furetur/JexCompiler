package ast

import lexing.Token

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

data class Identifier(val token: Token) : AstNode {
    override val children = emptyList<AstNode>()

    val text: String
        get() = token.text

    override fun <T> acceptVisitor(visitor: AstVisitor<T>): T = visitor.visitIdentifier(this)
}
