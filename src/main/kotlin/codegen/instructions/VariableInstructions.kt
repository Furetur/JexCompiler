package codegen.instructions

import codegen.ByteInstruction
import codegen.ConstantId

class GetLocalInstruction(relativeSlot: Byte) : ByteInstruction(Opcode.GetLocal, relativeSlot)

class SetLocalInstruction(relativeSlot: Byte) : ByteInstruction(Opcode.SetLocal, relativeSlot)

class GetGlobalInstruction(identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.GetGlobal, identifierConstantId)

class SetGlobalInstruction(identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.SetGlobal, identifierConstantId)

class DefineGlobalInstruction(identifierConstantId: ConstantId) :
    ByteInstruction(Opcode.DefineGlobal, identifierConstantId)
