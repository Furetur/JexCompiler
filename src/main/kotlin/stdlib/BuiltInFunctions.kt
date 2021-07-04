package stdlib

import codegen.BytecodeBuilder
import codegen.Code
import codegen.dsl.*

interface BuiltInFunction {
    val name: String
    val arity: Int
    val code: Code
}

fun BytecodeBuilder.addBuiltInFunction(builtInFunction: BuiltInFunction): BytecodeBuilder.ChunkReference =
    addFunction(builtInFunction.name, builtInFunction.arity, builtInFunction.code)

object FactFunction : BuiltInFunction {
    override val name: String = "fact"
    override val arity: Int = 1
    override val code: Code = {
        ifStatement(
            condition = {
                equal({ getLocalVariable(1) }, { literal(1) })
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
}
