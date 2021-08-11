package codegen.instructions

import codegen.ByteInstruction
import codegen.ConstantId
import codegen.NullaryInstruction

object NewInstanceInstruction : NullaryInstruction(Opcode.NewInstance)

class GetFieldInstruction(private val constantIndex: ConstantId) : ByteInstruction(Opcode.GetField, constantIndex) {
    override fun toAssemblyString(): String = "GetField @$constantIndex"
}

class SetFieldInstruction(private val constantIndex: ConstantId) : ByteInstruction(Opcode.SetField, constantIndex) {
    override fun toAssemblyString(): String = "SetField @$constantIndex"
}
