import ast.prettyPrint
import compiler.compile
import parsing.parseSourceCode
import resolve.resolveIdentifiers
import stdlib.FactFunction
import stdlib.PrintlnFunction
import java.io.File

val builtInFunctions = listOf(FactFunction, PrintlnFunction)

fun main() {
    val file = File("src/test/resources/example.txt")
    val program = parseSourceCode(file.readText())
    val resolutionResult = resolveIdentifiers(builtInFunctions, program)
    val bytecode = compile(resolutionResult, program)

    val outputFile = File("output/compiler.bytecode")
    bytecode.write(outputFile)
}
