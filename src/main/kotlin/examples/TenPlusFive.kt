package examples

import codegen.BytecodeBuilder
import codegen.dsl.*
import java.io.File

fun main() {
    val builder = BytecodeBuilder()
    builder.addChunk {
        literal(10)
        literal(5)
        add()
        print()
    }
    val bytecode = builder.bytecode()
    println(bytecode)
    println(bytecode.compile())
    val file = File("output/tenPlusFive.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
}
