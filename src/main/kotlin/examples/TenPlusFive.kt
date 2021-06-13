package examples

import codegen.BytecodeBuilder
import codegen.dsl.*
import java.io.File

fun main() {
    val builder = BytecodeBuilder()
    builder.addChunk {
        print {
            add(
                left = { literal(10) },
                right = { literal(5) }
            )
        }
    }
    val file = File("output/tenPlusFive.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
}
