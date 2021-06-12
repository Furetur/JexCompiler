package codegen.instructions

import codegen.ByteInstruction
import codegen.Directive
import codegen.Instruction
import codegen.NullaryInstruction
import codegen.BytecodeBuilder


class JumpForwardInstruction(
    private val destinationLabel: BytecodeBuilder.ChunkBuilder.Label,
    private val selfLabel: BytecodeBuilder.ChunkBuilder.Label
) : Instruction() {
    override val opcode = Opcode.JumpForward
    override val nArgumentBytes: Int = 1
    override fun compileArguments(): List<Byte> {
        val offset = destinationLabel.position - selfLabel.position
        return listOf(offset.toByte())
    }
}

class JumpForwardIfFalseInstruction(
    private val destinationLabel: BytecodeBuilder.ChunkBuilder.Label,
    private val selfLabel: BytecodeBuilder.ChunkBuilder.Label
) : Instruction() {
    override val opcode = Opcode.JumpForwardIfFalse
    override val nArgumentBytes: Int = 1
    override fun compileArguments(): List<Byte> {
        val offset = destinationLabel.position - selfLabel.position
        return listOf(offset.toByte())
    }
}

class JumpBackwardInstruction(
    private val destinationLabel: BytecodeBuilder.ChunkBuilder.Label,
    private val selfLabel: BytecodeBuilder.ChunkBuilder.Label
) : Instruction() {
    override val opcode = Opcode.JumpBackward
    override val nArgumentBytes: Int = 1
    override fun compileArguments(): List<Byte> {
        val offset = selfLabel.position - destinationLabel.position
        return listOf(offset.toByte())
    }
}

class JumpDirective(
    private val destinationLabel: BytecodeBuilder.ChunkBuilder.Label,
    private val selfLabel: BytecodeBuilder.ChunkBuilder.Label
) : Directive {
    override val nBytes: Int = 2
    override fun compile(): List<Byte> = if (destinationLabel.position > selfLabel.position) {
        JumpForwardInstruction(destinationLabel, selfLabel)
    } else {
        JumpBackwardInstruction(destinationLabel, selfLabel)
    }.compile()
}

class CallInstruction(arity: Byte) : ByteInstruction(Opcode.Call, arity)

object ReturnInstruction : NullaryInstruction(Opcode.Return)
