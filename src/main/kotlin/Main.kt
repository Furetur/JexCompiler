import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import compiler.compile
import parsing.parseSourceCode
import resolve.resolveIdentifiers
import stdlib.FactFunction
import stdlib.PrintlnFunction
import java.io.File

val builtInFunctions = listOf(FactFunction, PrintlnFunction)

class CompilerCommand : CliktCommand() {
    val inputFile by argument("input", help = "Input file").file(mustExist = true, canBeDir = false, mustBeReadable = true)
    val printAssembly by option("-a", "--asm", help = "Print assembly").flag(default = false)

    override fun run() {
        val program = parseSourceCode(inputFile.readText())
        val resolutionResult = resolveIdentifiers(builtInFunctions, program)
        val bytecode = compile(builtInFunctions, resolutionResult, program, printAssembly)

        val outputFile = File("output/compiler.bytecode")
        bytecode.write(outputFile)
    }
}

fun main(args: Array<String>) = CompilerCommand().main(args)
