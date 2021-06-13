package codegen


typealias Code = BytecodeBuilder.ChunkBuilder.() -> Unit
private typealias LabelId = Int

class BytecodeBuilder {
    private val chunks = mutableListOf<ChunkBuilder>()

    fun addChunk(code: Code): ChunkBuilder {
        val chunkBuilder = ChunkBuilder(chunks.size.toByte())
        chunks.add(chunkBuilder)
        chunkBuilder.apply(code)
        return chunkBuilder
    }

    fun addFunction(name: String, arity: Int, code: Code): ChunkBuilder {
        val chunkBuilder = addChunk {}
        chunkBuilder.storeConstant(name)
        chunkBuilder.storeConstant(arity)
        chunkBuilder.addCode(code)
        return chunkBuilder
    }

    fun bytecode() = Bytecode(chunks.map { it.chunk() })

    fun printAssembly() {
        for (chunk in chunks) {
            chunk.printAssembly()
            println()
        }
    }

    inner class ChunkBuilder(val id: ChunkId) {
        private val constants = mutableListOf<BytecodeConstant>()
        private val instructions = mutableListOf<Directive>()

        private var currentBytePosition: Int = 0

        private val labels = mutableListOf<Label>()
        private val labelPositions = mutableMapOf<LabelId, Int>()

        fun storeConstant(string: String): ConstantId = storeConstant(StringConstant(string))

        fun storeConstant(int: Int): ConstantId = storeConstant(IntConstant(int))


        fun storeConstant(constant: BytecodeConstant): ConstantId {
            val index = constants.indexOf(constant)
            return if (index != -1) {
                index
            } else {
                constants.add(constant)
                constants.size - 1
            }.toByte()
        }

        fun looseLabel(name: String? = null) = Label(name, labels.size).also { labels.add(it) }

        fun putLabel(label: Label) {
            if (label.id in labelPositions) {
                error("Label already put")
            } else {
                labelPositions[label.id] = currentBytePosition
            }
        }

        operator fun Directive.unaryPlus() {
            instructions.add(this)
            currentBytePosition += nBytes
        }

        fun addCode(code: Code) {
            apply(code)
        }

        operator fun Code.unaryPlus() {
            this@ChunkBuilder.addCode(this)
        }

        fun chunk(): BytecodeChunk {
            val code = instructions.flatMap { instruction ->
                val bytes = instruction.compile()
                check(bytes.size == instruction.nBytes) { "Instruction compiled to wrong nBytes" }
                bytes
            }
            return BytecodeChunk(constants, code)
        }

        inner class Label(val name: String?, val id: LabelId) {
            val position: Int
                get() = labelPositions[id] ?: error("Label not put")

            val assemblyName = "$name $id"
        }

        fun printAssembly() {
            println("Chunk #$id")
            for ((index, constant) in constants.withIndex()) {
                println("@$index: $constant")
            }
            var position = 0
            for (instruction in instructions) {
                val labels = labelPositions.entries.filter { it.value == position }.map { labels[it.key] }
                for (label in labels) {
                    if (label.name is String) {
                        println("${label.assemblyName}:")
                    }
                }
                println("\t" + instruction.toAssemblyString())
                position += instruction.nBytes
            }
        }
    }
}
