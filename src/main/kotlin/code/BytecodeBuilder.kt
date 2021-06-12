package code

private typealias ConstantId = Int

private typealias LabelId = Int

class BytecodeBuilder {
    private val chunks = mutableListOf<ChunkBuilder>()

    fun addChunk(): ChunkBuilder = ChunkBuilder().also { chunks.add(it) }

    fun addChunk(code: ChunkBuilder.() -> Unit) {
        addChunk().addCode(code)
    }

    fun compile() = Bytecode(chunks.map { it.compile() })

    inner class ChunkBuilder {
        private val constants = mutableListOf<BytecodeConstant>()
        private val instructions = mutableListOf<Instruction>()

        private var currentBytePosition: Int = 0

        private var nextLabelId = 0
        private val labelPositions = mutableMapOf<LabelId, Int>()

        fun putConstantIfNeeded(constant: BytecodeConstant): ConstantId {
            val index = constants.indexOf(constant)
            return if (index != -1) {
                index
            } else {
                constants.add(constant)
                constants.size - 1
            }
        }

        fun looseLabel(name: String = "") = Label(name, nextLabelId).also { nextLabelId += 1 }

        fun putLabel(label: Label) {
            if (label.id in labelPositions) {
                error("Label already put")
            } else {
                labelPositions[label.id] = currentBytePosition
            }
        }

        operator fun Instruction.unaryPlus() {
            instructions.add(this)
            currentBytePosition += nBytes
        }

        fun addCode(code: ChunkBuilder.() -> Unit) {
            apply(code)
        }

        fun compile(): BytecodeChunk {
            val code = instructions.flatMap { instruction ->
                val bytes = instruction.compile()
                check(bytes.size == instruction.nBytes) { "Instruction compiled to wrong nBytes" }
                bytes
            }
            return BytecodeChunk(constants, code)
        }

        inner class Label(val name: String, val id: LabelId) {
            val position: Int
                get() = labelPositions[id] ?: error("Label not put")
        }
    }
}
