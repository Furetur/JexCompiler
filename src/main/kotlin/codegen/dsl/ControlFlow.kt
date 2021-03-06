package codegen.dsl

import codegen.ChunkBuilder
import codegen.Code

fun ChunkBuilder.ifStatement(condition: Code, thenCode: Code, elseCode: Code = {}) {
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
    // ELSE
    +elseCode
    // END IF
    putLabel(afterIf)
}

fun ChunkBuilder.whileLoop(condition: Code, body: Code) {
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

fun ChunkBuilder.and(left: Code, right: Code) {
    val afterAnd = looseLabel("after and")

    +left
    jumpForwardIfFalse(afterAnd)
    pop()
    +right
    putLabel(afterAnd)
}

fun ChunkBuilder.or(left: Code, right: Code) {
    val afterOr = looseLabel("after or")
    val beforeRight = looseLabel("before right")

    +left
    jumpForwardIfFalse(beforeRight)
    jumpTo(afterOr)
    putLabel(beforeRight)
    pop()
    +right
    putLabel(afterOr)
}
