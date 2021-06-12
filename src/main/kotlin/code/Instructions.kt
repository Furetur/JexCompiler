package code

abstract class Instruction {
    abstract val opcode: Opcode
    abstract val nArgumentBytes: Int
    protected abstract fun compileArguments(): List<Byte>

    val nBytes: Int
        get() = 1 + nArgumentBytes

    fun compile() = listOf(opcode.compile()) + compileArguments()
}

class ConstantInstruction(val constantIndex: Int) : Instruction() {
    override val opcode: Opcode = Opcode.Constant
    override val nArgumentBytes: Int = 1

    override fun compileArguments(): List<Byte> = listOf(constantIndex.toByte())
}

fun BytecodeBuilder.ChunkBuilder.literal(value: Int) {
    val constantIndex = putConstantIfNeeded(BytecodeConstant.JexInt(value))
    +ConstantInstruction(constantIndex)
}

fun BytecodeBuilder.ChunkBuilder.literal(value: String) {
    val constantIndex = putConstantIfNeeded(BytecodeConstant.JexString(value))
    +ConstantInstruction(constantIndex)
}

object NullInstruction : Instruction() {
    override val opcode: Opcode = Opcode.Null
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.literal(nullValue: Nothing?) {
    +NullInstruction
}

object TrueInstruction : Instruction() {
    override val opcode: Opcode = Opcode.True
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

object FalseInstruction : Instruction() {
    override val opcode: Opcode = Opcode.False
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.literal(value: Boolean) {
    if (value) {
        +TrueInstruction
    } else {
        +FalseInstruction
    }
}

object PopInstruction : Instruction() {
    override val opcode: Opcode = Opcode.Pop
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.pop() {
    +PopInstruction
}

class GetLocalInstruction(val relativeStackIndex: Int) : Instruction() {
    override val opcode: Opcode = Opcode.GetLocal
    override val nArgumentBytes = 1
    override fun compileArguments(): List<Byte> = listOf(relativeStackIndex.toByte())
}

object PrintInstruction : Instruction() {
    override val opcode: Opcode = Opcode.Print
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.print() {
    +PrintInstruction
}

object EqualInstruction : Instruction() {
    override val opcode: Opcode = Opcode.Equal
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.equal() {
    +EqualInstruction
}

object AddInstruction : Instruction() {
    override val opcode: Opcode = Opcode.Add
    override val nArgumentBytes = 0
    override fun compileArguments(): List<Byte> = emptyList()
}

fun BytecodeBuilder.ChunkBuilder.add() {
    +AddInstruction
}

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

fun BytecodeBuilder.ChunkBuilder.jumpForward(destination: BytecodeBuilder.ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpForwardInstruction(destination, selfLabel)
    putLabel(selfLabel)
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

fun BytecodeBuilder.ChunkBuilder.jumpForwardIfFalse(destination: BytecodeBuilder.ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpForwardIfFalseInstruction(destination, selfLabel)
    putLabel(selfLabel)
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

fun BytecodeBuilder.ChunkBuilder.jumpBackward(destination: BytecodeBuilder.ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpBackwardInstruction(destination, selfLabel)
    putLabel(selfLabel)
}



