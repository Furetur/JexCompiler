package codegen.dsl

import codegen.BytecodeBuilder
import codegen.Code

fun BytecodeBuilder.ChunkBuilder.ifStatement(condition: Code, thenCode: Code) {
    val afterThen = looseLabel("after then")
    val afterIf = looseLabel("after if")

    // IF
    +condition // CONDITION
    jumpForwardIfFalse(afterThen)
    // THEN
    pop() // pop CONDITION
    +thenCode
    jumpTo(afterIf)
    // after THEN
    putLabel(afterThen)
    pop() // pop 0 == 0 from stack
    // END IF
    putLabel(afterIf)
}

fun BytecodeBuilder.ChunkBuilder.whileLoop(condition: Code, body: Code) {
    val beforeLoop = looseLabel("before loop")
    val afterBody = looseLabel("after body")

    putLabel(beforeLoop)
    // WHILE
    +condition
    jumpForwardIfFalse(afterBody)
    // BODY
    pop()
    +body
    jumpTo(beforeLoop)
    // AFTER BODY
    putLabel(afterBody)
    pop()
    // END WHILE
}
