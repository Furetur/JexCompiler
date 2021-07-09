package parsing

import SimpleLanguageLexer
import SimpleLanguageParser
import ast.Program
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun parseSourceCode(code: String): Program {
    val charStream = CharStreams.fromString(code + "\n")
    val lexer = SimpleLanguageLexer(charStream)
    val parser = SimpleLanguageParser(CommonTokenStream(lexer))
    return AstBuilder().visitProg(parser.prog()!!)
}
