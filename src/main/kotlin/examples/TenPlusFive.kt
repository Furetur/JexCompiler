package examples

import code.BytecodeBuilder
import code.add
import code.literal

fun main() {
    val builder = BytecodeBuilder()
    builder.addChunk {
        literal(10)
        literal(5)
        add()
    }
    println(builder.compile())
}