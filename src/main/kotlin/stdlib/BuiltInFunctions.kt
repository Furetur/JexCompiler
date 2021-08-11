package stdlib

import codegen.BytecodeBuilder
import codegen.Code
import codegen.dsl.*
import codegen.instructions.NewInstanceInstruction
import codegen.instructions.ParseIntInstruction
import codegen.instructions.ReadLineInstruction
import codegen.instructions.ToStringInstruction
import resolve.GettableValue

interface BuiltInFunction : GettableValue {
    override val name: String
    val arity: Int
    val code: Code

    override fun getValue() = code
}

fun BytecodeBuilder.addBuiltInFunction(builtInFunction: BuiltInFunction): BytecodeBuilder.ChunkReference =
    addFunction(builtInFunction.name, builtInFunction.arity, builtInFunction.code)

object FactFunction : BuiltInFunction {
    override val name: String = "fact"
    override val arity: Int = 1
    override val code: Code = {
        ifStatement(
            ifCondition = {
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

object PrintlnFunction : BuiltInFunction {
    override val name: String = "println"
    override val arity: Int = 1
    override val code: Code = {
        ret {
            print {
                getLocalVariable(1)
            }
        }
    }
}

object ReadLineFunction : BuiltInFunction {
    override val name: String = "readLine"
    override val arity: Int = 0
    override val code: Code = {
        ret {
            +ReadLineInstruction
        }
    }
}

object IntFunction : BuiltInFunction {
    override val name: String = "int"
    override val arity: Int = 1
    override val code: Code = {
        ret {
            getLocalVariable(1)
            +ParseIntInstruction
        }
    }
}

object StrFunction : BuiltInFunction {
    override val name: String = "str"
    override val arity: Int = 1
    override val code: Code = {
        ret {
            getLocalVariable(1)
            +ToStringInstruction
        }
    }
}

object ObjectFunction : BuiltInFunction {
    override val name: String = "object"
    override val arity: Int = 0
    override val code: Code = {
        ret {
            +NewInstanceInstruction
        }
    }
}
