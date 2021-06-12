package codegen

import java.io.File
import java.nio.ByteBuffer

typealias ChunkId = Byte
typealias ConstantId = Byte

data class Bytecode(val chunks: List<BytecodeChunk>) {
    fun compile(): List<Byte> {
        val result = mutableListOf<Byte>()
        for (chunk in chunks) {
            result.addAll(chunk.compile())
        }
        return result
    }
    fun write(file: File) {
        file.writeBytes(compile().toByteArray())
    }
}

data class BytecodeChunk(val constants: List<BytecodeConstant>, val code: List<Byte>) {
    fun compile(): List<Byte> {
        val result = mutableListOf<Byte>()
        result.addAll(constants.size.encodeAsU8()) // n_constants
        for (constant in constants) {
            result.addAll(constant.compile())
        }
        result.addAll(code.size.encodeAsU16()) // n_code_bytes
        result.addAll(code)
        return result
    }
}

sealed class BytecodeConstant {
    abstract val type: Byte
    protected abstract fun compileValue(): List<Byte>

    fun compile() = listOf(type) + compileValue()
}


data class IntConstant(val value: Int) : BytecodeConstant() {
    override val type: Byte = 0
    override fun compileValue(): List<Byte> = value.encodeAsI32()
}

data class StringConstant(val value: String) : BytecodeConstant() {
    override val type: Byte = 1
    override fun compileValue(): List<Byte> {
        val data = value.encodeToByteArray().asList()
        return data.size.encodeAsU16() + data
    }
}

data class FunctionConstant(val chunkId: ChunkId) : BytecodeConstant() {
    override val type: Byte = 2
    override fun compileValue(): List<Byte> = listOf(chunkId)
}
