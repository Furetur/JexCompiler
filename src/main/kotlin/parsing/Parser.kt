package parsing

import ast.AstNode
import lexing.TokenReader

interface Parser<T : AstNode> {
    fun parse(tokenReader: TokenReader): T
}
