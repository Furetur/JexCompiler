package examples

import codegen.BytecodeBuilder
import codegen.dsl.*
import stdlib.FactFunction
import stdlib.addBuiltInFunction
import java.io.File

fun main() {
    val builder = BytecodeBuilder()

    val factFunction = builder.addBuiltInFunction(FactFunction)

    builder.main {
        print {
            function(factFunction)
            literal(6)
            call(1)
        }
    }

    val file = File("output/fact_6_builtin.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
    builder.printAssembly()
}
