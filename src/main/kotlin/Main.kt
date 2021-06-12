import ast.prettyPrint
import parsing.parseSourceCode
import java.io.File

fun main() {
    val file = File("src/test/resources/example.txt")
    val program = parseSourceCode(file.readText())
    program.prettyPrint()
}
