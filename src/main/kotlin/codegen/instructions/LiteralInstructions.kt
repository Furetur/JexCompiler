package codegen.instructions

import codegen.ByteInstruction
import codegen.ConstantId
import codegen.NullaryInstruction

class ConstantInstruction(private val constantIndex: ConstantId) : ByteInstruction(Opcode.Constant, constantIndex) {
    override fun toAssemblyString(): String = "Constant @$constantIndex"
}

object NullInstruction : NullaryInstruction(Opcode.Null)

object TrueInstruction : NullaryInstruction(Opcode.True)

object FalseInstruction : NullaryInstruction(Opcode.False)
