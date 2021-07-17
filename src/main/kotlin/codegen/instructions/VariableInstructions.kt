package codegen.instructions

import codegen.ByteInstruction
import codegen.ConstantId

class GetLocalInstruction(relativeSlot: Byte) : ByteInstruction(Opcode.GetLocal, relativeSlot)

class SetLocalInstruction(relativeSlot: Byte) : ByteInstruction(Opcode.SetLocal, relativeSlot)

class GetGlobalInstruction(private val identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.GetGlobal, identifierConstantId) {
    override fun toAssemblyString(): String = "SetGlobal @$identifierConstantId"
}

class SetGlobalInstruction(private val identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.SetGlobal, identifierConstantId) {
    override fun toAssemblyString(): String = "SetGlobal @$identifierConstantId"
}

class DefineGlobalInstruction(private val identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.DefineGlobal, identifierConstantId) {
    override fun toAssemblyString(): String = "DefineGlobal @$identifierConstantId"
}
