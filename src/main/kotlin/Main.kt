import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import compiler.compile
import org.antlr.v4.runtime.misc.ParseCancellationException
import parsing.ParsingErrorListener
import parsing.parseSourceCode
import resolve.resolveIdentifiers
import stdlib.*
import java.lang.NullPointerException
import kotlin.system.exitProcess

val builtInFunctions = listOf(FactFunction, PrintlnFunction, ReadLineFunction, IntFunction, StrFunction, ObjectFunction)


fun exitWithExceptionMessage(e: Exception) {
    System.err.println(e.message)
    exitProcess(1)
}

class CompilerCommand : CliktCommand() {
    val inputFile by argument("input", help = "Input file").file(mustExist = true, canBeDir = false, mustBeReadable = true)
    val outputFile by argument("output", help = "Output file").file(mustExist = false, canBeDir = false)

    val printAssembly by option("-a", "--asm", help = "Print assembly").flag(default = false)

    override fun run() {
        try {
            val program = parseSourceCode(inputFile.readText())
            ParsingErrorListener.throwErrorsStackIfNotEmpty()

            val resolutionResult = resolveIdentifiers(builtInFunctions, program)
            val bytecode = compile(builtInFunctions, resolutionResult, program, printAssembly)

            outputFile.createNewFile()

            bytecode.write(outputFile)
        } catch (e: ParseCancellationException) {
            exitWithExceptionMessage(e)
        } catch (e: IllegalStateException) {
            exitWithExceptionMessage(e)
        } catch (e: NullPointerException) {
            exitWithExceptionMessage(e)
        }
    }
}

fun main(args: Array<String>) = CompilerCommand().main(args)
