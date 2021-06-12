package code

data class Bytecode(val chunks: List<BytecodeChunk>) {
    fun compile(): List<Byte> {
        val result = mutableListOf<Byte>()
        for (chunk in chunks) {
            result.addAll(chunk.compile())
        }
        return result
    }
}

data class BytecodeChunk(val constants: List<BytecodeConstant>, val code: List<Byte>) {
    fun compile(): List<Byte> {
        val result = mutableListOf<Byte>()
        result.add(constants.size.toByte())
        for (constant in constants) {
            result.addAll(constant.compile())
        }
        result.add(code.size.toByte())
        result.addAll(code)
        return result
    }
}

sealed class BytecodeConstant {
    abstract val type: Byte
    protected abstract fun compileValue(): List<Byte>

    fun compile() = listOf(type) + compileValue()

    data class JexInt(val value: Int) : BytecodeConstant() {
        override val type: Byte = 0
        override fun compileValue(): List<Byte> {
            TODO()
        }
    }

    data class JexString(val value: String) : BytecodeConstant() {
        override val type: Byte = 1
        override fun compileValue(): List<Byte> {
            val data = value.encodeToByteArray().asList()
            return listOf(data.size.toByte()) + data
        }
    }

    data class JexFunction(val chunkId: Int) : BytecodeConstant() {
        override val type: Byte = 2
        override fun compileValue(): List<Byte> = listOf(chunkId.toByte())
    }
}
