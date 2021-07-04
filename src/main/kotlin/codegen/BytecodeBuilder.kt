package codegen


private typealias ChunkName = String

class BytecodeBuilder {
    private val chunks = mutableMapOf<ChunkName, StoredChunk>()

    fun main(code: Code): ChunkReference = addChunk("", code)

    fun addChunk(name: String, code: Code): ChunkReference {
        if (name in chunks) {
            error("Chunk with this name already exists")
        }
        val reference = ChunkReference(name)
        val chunkBuilder = ChunkBuilder(this, reference)
        chunks[name] = StoredChunk(null, chunkBuilder)
        chunkBuilder.apply(code)
        return reference
    }

    fun addFunction(name: String, arity: Int, code: Code) = addChunk(name) {
        storeConstant(name)
        storeConstant(arity)
        +code
    }

    fun bytecode(): Bytecode {
        val indexedChunks = mutableListOf<StoredChunk>()
        val mainChunk = chunks[""] ?: error("Main chunk not present")
        indexedChunks.add(mainChunk)
        indexedChunks.addAll(chunks.entries.filter { it.key != "" }.map { it.value })
        for ((index, chunk) in indexedChunks.withIndex()) {
            chunk.id = index.toByte()
        }
        return Bytecode(indexedChunks.map { it.chunk.chunk() })
    }

    fun printAssembly() {
        for (chunk in chunks.values) {
            chunk.chunk.printAssembly()
            println()
        }
    }

    private class StoredChunk(var id: ChunkId?, val chunk: ChunkBuilder)

    inner class ChunkReference(val chunkName: String) {
        val id: ChunkId
            get() = chunks[chunkName]?.id ?: error("Chunk not given an id")
    }
}
