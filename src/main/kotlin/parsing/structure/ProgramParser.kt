package parsing.structure

import ast.Program
import ast.Statement
import lexing.TokenReader
import parsing.Parser
import parsing.statements.StatementParser

object ProgramParser : Parser<Program> {
    override fun parse(tokenReader: TokenReader): Program {
        val statements = mutableListOf<Statement>()
        while (tokenReader.hasNext()) {
            statements.add(StatementParser.parse(tokenReader))
        }
        return Program(statements)
    }
}
