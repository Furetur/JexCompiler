package examples

import code.*

fun main() {
    val builder = BytecodeBuilder()
    builder.addChunk {
        val afterThenLabel = looseLabel("after then")

        // IF 0 == 0
        literal(0)
        literal(0)
        equal()
        // jump over THEN body
        jumpForwardIfFalse(afterThenLabel)
        // THEN
        pop() // pop 0 == 0 from stack
        literal("0 is 0")
        print()
        // after THEN
        putLabel(afterThenLabel)
        pop() // pop 0 == 0 from stack
    }
    println(builder.compile())
}