package codegen.dsl

import codegen.BytecodeBuilder
import codegen.Code
import codegen.instructions.*


fun BytecodeBuilder.ChunkBuilder.print(code: Code) {
    addCode(code)
    +PrintInstruction
}

fun BytecodeBuilder.ChunkBuilder.equal(left: Code, right: Code) {
    +left
    +right
    +EqualInstruction
}

fun BytecodeBuilder.ChunkBuilder.greater(left: Code, right: Code) {
    +left
    +right
    +GreaterInstruction
}

fun BytecodeBuilder.ChunkBuilder.less(left: Code, right: Code) {
    +left
    +right
    +LessInstruction
}

fun BytecodeBuilder.ChunkBuilder.negate(code: Code) {
    +code
    +NegateInstruction
}

fun BytecodeBuilder.ChunkBuilder.add(left: Code, right: Code) {
    +left
    +right
    +AddInstruction
}

fun BytecodeBuilder.ChunkBuilder.subtract(left: Code, right: Code) {
    +left
    +right
    +SubtractInstruction
}

fun BytecodeBuilder.ChunkBuilder.multiply(left: Code, right: Code) {
    +left
    +right
    +MultiplyInstruction
}

fun BytecodeBuilder.ChunkBuilder.divide(left: Code, right: Code) {
    +left
    +right
    +DivideInstruction
}
