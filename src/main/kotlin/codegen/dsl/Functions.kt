package codegen.dsl

import codegen.*
import codegen.instructions.*

fun ChunkBuilder.literal(value: Int) {
    +ConstantInstruction(storeConstant(value))
}

fun ChunkBuilder.literal(value: String) {
    +ConstantInstruction(storeConstant(value))
}

fun ChunkBuilder.literal(nullValue: Nothing?) {
    +NullInstruction
}

fun ChunkBuilder.literal(value: Boolean) {
    if (value) {
        +TrueInstruction
    } else {
        +FalseInstruction
    }
}

fun ChunkBuilder.function(chunkReference: BytecodeBuilder.ChunkReference) {
    +ConstantInstruction(storeConstant(chunkReference))
}

fun ChunkBuilder.pop() {
    +PopInstruction
}

fun ChunkBuilder.getLocalVariable(relativeSlot: Byte) {
    +GetLocalInstruction(relativeSlot)
}

fun ChunkBuilder.setLocalVariable(relativeSlot: Byte, value: Code) {
    +value
    +SetLocalInstruction(relativeSlot)
}

fun ChunkBuilder.getGlobalVariable(identifier: String) {
    +GetGlobalInstruction(storeConstant(identifier))
}

fun ChunkBuilder.setGlobalVariable(identifier: String, value: Code) {
    +value
    +SetGlobalInstruction(storeConstant(identifier))
}

fun ChunkBuilder.jumpTo(destination: ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpDirective(destination, selfLabel)
    putLabel(selfLabel)
}

fun ChunkBuilder.jumpForwardIfFalse(destination: ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpForwardIfFalseInstruction(destination, selfLabel)
    putLabel(selfLabel)
}

fun ChunkBuilder.self() {
    function(selfReference)
}

fun ChunkBuilder.call(arity: Byte) {
    +CallInstruction(arity)
}

fun ChunkBuilder.ret(value: Code) {
    +value
    +ReturnInstruction
}

