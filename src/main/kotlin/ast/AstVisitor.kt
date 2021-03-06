package ast

interface AstVisitor<T> {
    val default: T

    fun visit(astNode: AstNode): T = astNode.acceptVisitor(this)

    private fun visitNonTerminalNode(node: AstNode): T = node.children.map { visit(it) }.lastOrNull() ?: default

    fun visitProgram(program: Program): T = visitNonTerminalNode(program)
    fun visitBlock(block: Block): T = visitNonTerminalNode(block)
    fun visitFieldAccess(fieldAccess: FieldAccess) = visitNonTerminalNode(fieldAccess)

    fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement): T =
        visitNonTerminalNode(variableDeclarationStatement)

    fun visitAssignmentStatement(assignmentStatement: AssignmentStatement): T =
        visitNonTerminalNode(assignmentStatement)

    fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement): T =
        visitNonTerminalNode(functionDeclarationStatement)

    fun visitIfStatement(ifStatement: IfStatement): T = visitNonTerminalNode(ifStatement)
    fun visitWhileStatement(whileStatement: WhileStatement): T = visitNonTerminalNode(whileStatement)
    fun visitReturnStatement(returnStatement: ReturnStatement): T = visitNonTerminalNode(returnStatement)
    fun visitExpressionStatement(expressionStatement: ExpressionStatement): T =
        visitNonTerminalNode(expressionStatement)

    fun visitBlockStatement(blockStatement: BlockStatement): T = visitNonTerminalNode(blockStatement)
    fun visitCallExpression(callExpression: CallExpression): T = visitNonTerminalNode(callExpression)
    fun visitUnaryOperatorExpression(unaryOperatorExpression: UnaryOperatorExpression): T =
        visitNonTerminalNode(unaryOperatorExpression)

    fun visitBinaryOperatorExpression(binaryOperatorExpression: BinaryOperatorExpression): T =
        visitNonTerminalNode(binaryOperatorExpression)

    fun visitStringLiteralExpression(stringLiteralExpression: StringLiteralExpression): T
    fun visitNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression): T
    fun visitBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression): T
    fun visitNullLiteralExpression(nullLiteralExpression: NullLiteralExpression): T
    fun visitIdentifierExpression(identifierExpression: IdentifierExpression): T =
        visitNonTerminalNode(identifierExpression)

    fun visitFieldAccessExpression(fieldAccessExpression: FieldAccessExpression): T =
        visitNonTerminalNode(fieldAccessExpression)

    fun visitIdentifier(identifier: Identifier): T
}
