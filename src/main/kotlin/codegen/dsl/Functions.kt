package codegen.dsl

import codegen.BytecodeBuilder
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

fun BytecodeBuilder.ChunkBuilder.pop() {
    +PopInstruction
}

fun BytecodeBuilder.ChunkBuilder.print() {
    +PrintInstruction
}

fun BytecodeBuilder.ChunkBuilder.equal() {
    +EqualInstruction
}

fun BytecodeBuilder.ChunkBuilder.greater() {
    +GreaterInstruction
}

fun BytecodeBuilder.ChunkBuilder.less() {
    +LessInstruction
}

fun BytecodeBuilder.ChunkBuilder.negate() {
    +NegateInstruction
}

fun BytecodeBuilder.ChunkBuilder.add() {
    +AddInstruction
}

fun BytecodeBuilder.ChunkBuilder.subtract() {
    +SubtractInstruction
}

fun BytecodeBuilder.ChunkBuilder.multiply() {
    +MultiplyInstruction
}

fun BytecodeBuilder.ChunkBuilder.divide() {
    +DivideInstruction
}

fun BytecodeBuilder.ChunkBuilder.getLocalVariable(relativeSlot: Byte) {
    +GetLocalInstruction(relativeSlot)
}

fun BytecodeBuilder.ChunkBuilder.setLocalVariable(relativeSlot: Byte) {
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
