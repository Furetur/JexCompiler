package identifiers

import codegen.Code
import codegen.dsl.getGlobalVariable
import codegen.dsl.getLocalVariable
import codegen.dsl.setGlobalVariable
import codegen.dsl.setLocalVariable

interface Value {
    val name: String
}

interface GettableValue : Value {
    fun getValue(): Code
}

interface SettableValue : Value {
    fun setValue(code: Code): Code
}

class UserLocalVariable(override val name: String, private val localStackSlot: Byte) : GettableValue, SettableValue {
    override fun getValue(): Code = {
        getLocalVariable(localStackSlot)
    }

    override fun setValue(code: Code): Code = {
        setLocalVariable(localStackSlot, code)
    }
}

class UserGlobalVariable(override val name: String) : GettableValue, SettableValue {
    override fun getValue(): Code = {
        getGlobalVariable(name)
    }

    override fun setValue(code: Code): Code = {
        setGlobalVariable(name, code)
    }
}
