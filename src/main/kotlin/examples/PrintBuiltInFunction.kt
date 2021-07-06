package examples

import codegen.BytecodeBuilder
import codegen.dsl.call
import codegen.dsl.function
import codegen.dsl.literal
import codegen.dsl.print
import stdlib.PrintlnFunction
import stdlib.addBuiltInFunction
import java.io.File

fun main() {
    val builder = BytecodeBuilder()

    val printlnFunction = builder.addBuiltInFunction(PrintlnFunction)

    builder.main {
        print { function(printlnFunction) }
        print {
            function(printlnFunction)
            literal(6)
            call(1)
        }
    }

    val file = File("output/println.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
    builder.printAssembly()
}
