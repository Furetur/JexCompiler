package codegen

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.encodeAsU8(): List<Byte> {
    return if (this in 0..255) {
        listOf(toByte())
    } else {
        error("Int to big. Cannot be converted to u8")
    }
}

fun Int.encodeAsU16(): List<Byte> {
    return if (this in 0..65535) {
        val byteBuffer = ByteBuffer.allocate(2)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.putShort(this.and(0xffff).toShort())
        byteBuffer.array().asList()
    } else {
        error("Int to big. Cannot be converted to u16")
    }
}

fun Int.encodeAsI32(): List<Byte> {
    val byteBuffer = ByteBuffer.allocate(4)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putInt(this)
    return byteBuffer.array().asList()
}
