package ast

private fun AstNode.prettyPrint(depth: Int) {
    println("\t".repeat(depth) + "${javaClass.simpleName} {")
    for (child in children) {
        child.prettyPrint(depth + 1)
    }
    println("\t".repeat(depth) + "}")
}

fun AstNode.prettyPrint() {
    prettyPrint(0)
}
