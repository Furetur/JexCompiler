package ast

interface AstVisitor<T> {
    fun visit(astNode: AstNode): T = astNode.acceptVisitor(this)

    private fun visitNonTerminalNode(node: AstNode): T = node.children.map { visit(it) }.last()

    fun visitProgram(program: Program): T = visitNonTerminalNode(program)
    fun visitBlock(block: Block): T = visitNonTerminalNode(block)
    fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement): T =
        visitNonTerminalNode(variableDeclarationStatement)

    fun visitAssignmentStatement(assignmentStatement: AssignmentStatement): T =
        visitNonTerminalNode(assignmentStatement)

    fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement): T =
        visitNonTerminalNode(functionDeclarationStatement)

    fun visitIfStatement(ifStatement: IfStatement): T = visitNonTerminalNode(ifStatement)
    fun visitWhileStatement(whileStatement: WhileStatement): T = visitNonTerminalNode(whileStatement)
    fun visitReturnStatement(returnStatement: ReturnStatement): T = visitNonTerminalNode(returnStatement)
    fun visitPrintStatement(printStatement: PrintStatement): T = visitNonTerminalNode(printStatement)
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

    fun visitIdentifier(identifier: Identifier): T
}
