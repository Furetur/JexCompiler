package examples

import codegen.BytecodeBuilder
import codegen.dsl.*
import java.io.File

fun main() {
    val builder = BytecodeBuilder()

    val factFunction = builder.addFunction("fact", 1) {
        ifStatement(
            condition = {
                equal(left = { getLocalVariable(1) }, right = { literal(1) })
            },
            thenCode = {
                ret { literal(1) }
            }
        )
        ret {
            multiply(
                left = { getLocalVariable(1) },
                right = {
                    self()
                    subtract(left = { getLocalVariable(1) }, right = { literal(1) })
                    call(1)
                }
            )
        }
    }

    builder.main {
        print {
            function(factFunction)
            literal(6)
            call(1)
        }
    }

    val file = File("output/fact_6.bytecode")
    file.createNewFile()
    builder.bytecode().write(file)
}
