package codegen

import codegen.instructions.Opcode

interface Directive {
    val nBytes: Int
    fun compile(): List<Byte>
}

abstract class Instruction : Directive {
    abstract val opcode: Opcode
    abstract val nArgumentBytes: Int
    protected abstract fun compileArguments(): List<Byte>

    override val nBytes: Int
        get() = 1 + nArgumentBytes

    override fun compile() = listOf(opcode.compile()) + compileArguments()
}

abstract class NullaryInstruction(override val opcode: Opcode) : Instruction() {
    override val nArgumentBytes = 0
    override fun compileArguments() = emptyList<Byte>()
}

abstract class ByteInstruction(override val opcode: Opcode, private val argument: Byte) : Instruction() {
    override val nArgumentBytes = 1
    override fun compileArguments(): List<Byte>  = listOf(argument)
}
