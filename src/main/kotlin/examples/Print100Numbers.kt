package examples

import codegen.BytecodeBuilder
import codegen.dsl.*
import java.io.File

fun main() {
    val builder = BytecodeBuilder()
    builder.addChunk {
        literal(0)
        whileLoop(
            condition = {
                less(left = { getLocalVariable(1) }, right = { literal(100) })
            },
            body = {
                print { getLocalVariable(1) }
                pop()
                setLocalVariable(1) {
                    add(
                        left = { getLocalVariable(1) },
                        right = { literal(1) }
                    )
                }
            }
        )
    }
    val file = File("output/print100numbers.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
}
