package codegen.dsl

import codegen.BytecodeBuilder
import codegen.ChunkId
import codegen.Code
import codegen.FunctionConstant
import codegen.instructions.*

fun BytecodeBuilder.ChunkBuilder.literal(value: Int) {
    +ConstantInstruction(storeConstant(value))
}

fun BytecodeBuilder.ChunkBuilder.literal(value: String) {
    +ConstantInstruction(storeConstant(value))
}

fun BytecodeBuilder.ChunkBuilder.literal(nullValue: Nothing?) {
    +NullInstruction
}

fun BytecodeBuilder.ChunkBuilder.literal(value: Boolean) {
    if (value) {
        +TrueInstruction
    } else {
        +FalseInstruction
    }
}

fun BytecodeBuilder.ChunkBuilder.function(chunkId: ChunkId) {
    val constantId = storeConstant(FunctionConstant(chunkId))
    +ConstantInstruction(constantId)
}

fun BytecodeBuilder.ChunkBuilder.pop() {
    +PopInstruction
}

fun BytecodeBuilder.ChunkBuilder.getLocalVariable(relativeSlot: Byte) {
    +GetLocalInstruction(relativeSlot)
}

fun BytecodeBuilder.ChunkBuilder.setLocalVariable(relativeSlot: Byte, value: Code) {
    +value
    +SetLocalInstruction(relativeSlot)
}

fun BytecodeBuilder.ChunkBuilder.getGlobalVariable(identifier: String) {
    +GetGlobalInstruction(storeConstant(identifier))
}

fun BytecodeBuilder.ChunkBuilder.setGlobalVariable(identifier: String) {
    +SetGlobalInstruction(storeConstant(identifier))
}

fun BytecodeBuilder.ChunkBuilder.defineGlobalVariable(identifier: String) {
    +DefineGlobalInstruction(storeConstant(identifier))
}

fun BytecodeBuilder.ChunkBuilder.jumpTo(destination: BytecodeBuilder.ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpDirective(destination, selfLabel)
    putLabel(selfLabel)
}

fun BytecodeBuilder.ChunkBuilder.jumpForwardIfFalse(destination: BytecodeBuilder.ChunkBuilder.Label) {
    val selfLabel = looseLabel()
    +JumpForwardIfFalseInstruction(destination, selfLabel)
    putLabel(selfLabel)
}

fun BytecodeBuilder.ChunkBuilder.self() {
    function(id)
}

fun BytecodeBuilder.ChunkBuilder.call(arity: Byte) {
    +CallInstruction(arity)
}

fun BytecodeBuilder.ChunkBuilder.ret(value: Code) {
    +value
    +ReturnInstruction
}

