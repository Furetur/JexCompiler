package codegen.dsl

import codegen.ChunkBuilder
import codegen.Code
import codegen.instructions.*


fun ChunkBuilder.print(code: Code) {
    +code
    +PrintInstruction
}

fun ChunkBuilder.equal(left: Code, right: Code) {
    +left
    +right
    +EqualInstruction
}

fun ChunkBuilder.greater(left: Code, right: Code) {
    +left
    +right
    +GreaterInstruction
}

fun ChunkBuilder.less(left: Code, right: Code) {
    +left
    +right
    +LessInstruction
}

fun ChunkBuilder.negate(code: Code) {
    +code
    +NegateInstruction
}

fun ChunkBuilder.add(left: Code, right: Code) {
    +left
    +right
    +AddInstruction
}

fun ChunkBuilder.subtract(left: Code, right: Code) {
    +left
    +right
    +SubtractInstruction
}

fun ChunkBuilder.multiply(left: Code, right: Code) {
    +left
    +right
    +MultiplyInstruction
}

fun ChunkBuilder.divide(left: Code, right: Code) {
    +left
    +right
    +DivideInstruction
}
