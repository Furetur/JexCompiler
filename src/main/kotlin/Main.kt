import ast.prettyPrint
import code.BytecodeBuilder
import code.literal
import code.pop
import parsing.parseSourceCode
import java.io.File

fun main() {
    val file = File("src/test/resources/example.txt")
    val program = parseSourceCode(file.readText())
    program.prettyPrint()

    BytecodeBuilder().addChunk {
        literal(1)
        pop()
    }
}
