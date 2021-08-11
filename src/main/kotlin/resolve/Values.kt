package resolve

import ast.FieldAccess
import codegen.Code
import codegen.dsl.*
import codegen.getValue
import codegen.instructions.GetFieldInstruction
import codegen.instructions.SetFieldInstruction

interface Value {
    val name: String
}

interface GettableValue : Value {
    fun getValue(): Code
}

interface SettableValue : Value {
    fun declareValue(code: Code): Code
    fun setValue(code: Code): Code
}

interface GettableSettableValue : GettableValue, SettableValue

class UserLocalVariable(override val name: String, private val localStackSlot: Byte) : GettableSettableValue {
    override fun getValue(): Code = {
        getLocalVariable(localStackSlot)
    }

    override fun setValue(code: Code): Code = {
        setLocalVariable(localStackSlot, code)
    }

    override fun declareValue(code: Code): Code = code
}

class UserGlobalVariable(override val name: String) : GettableSettableValue {
    override fun getValue(): Code = {
        getGlobalVariable(name)
    }

    override fun setValue(code: Code): Code = {
        setGlobalVariable(name, code)
    }

    override fun declareValue(code: Code): Code = {
        setGlobalVariable(name, code)
    }
}
