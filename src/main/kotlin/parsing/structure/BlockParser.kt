package parsing.structure

import ast.Block
import ast.Statement
import lexing.*
import parsing.Parser
import parsing.statements.StatementParser

/**
 * LBRACE statement* RBRACE
 */
object BlockParser : Parser<Block> {
    override fun parse(tokenReader: TokenReader): Block {
        tokenReader.require(TokenType.LBrace)

        val statements = mutableListOf<Statement>()
        tokenReader.until(TokenType.RBrace) {
            statements.add(StatementParser.parse(tokenReader))
        }
        return Block(0, statements)
    }
}
