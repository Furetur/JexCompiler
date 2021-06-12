package codegen.instructions

import codegen.ByteInstruction
import codegen.ConstantId
import codegen.NullaryInstruction

class ConstantInstruction(constantIndex: ConstantId) : ByteInstruction(Opcode.Constant, constantIndex)

object NullInstruction : NullaryInstruction(Opcode.Null)

object TrueInstruction : NullaryInstruction(Opcode.True)

object FalseInstruction : NullaryInstruction(Opcode.False)
