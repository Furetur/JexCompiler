package ast

interface AstVisitor<T> {
    fun visit(astNode: AstNode): T = astNode.acceptVisitor(this)


    fun visitProgram(program: Program): T
    fun visitBlock(block: Block): T
    fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement): T
    fun visitAssignmentStatement(assignmentStatement: AssignmentStatement): T
    fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement): T
    fun visitIfStatement(ifStatement: IfStatement): T
    fun visitWhileStatement(whileStatement: WhileStatement): T
    fun visitReturnStatement(returnStatement: ReturnStatement): T
    fun visitPrintStatement(printStatement: PrintStatement): T
    fun visitExpressionStatement(expressionStatement: ExpressionStatement): T
    fun visitBlockStatement(blockStatement: BlockStatement): T
    fun visitCallExpression(callExpression: CallExpression): T
    fun visitUnaryOperatorExpression(unaryOperatorExpression: UnaryOperatorExpression): T
    fun visitBinaryOperatorExpression(binaryOperatorExpression: BinaryOperatorExpression): T
    fun visitStringLiteralExpression(stringLiteralExpression: StringLiteralExpression): T
    fun visitNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression): T
    fun visitBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression): T
    fun visitNullLiteralExpression(nullLiteralExpression: NullLiteralExpression): T
    fun visitIdentifierExpression(identifierExpression: IdentifierExpression): T

    fun visitIdentifier(identifier: Identifier): T
}

interface AstListener {
    fun onProgram(program: Program) {}
    fun onBlock(block: Block) {}
    fun onVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement) {}
    fun onAssignmentStatement(assignmentStatement: AssignmentStatement) {}
    fun onFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement) {}
    fun onIfStatement(ifStatement: IfStatement) {}
    fun onWhileStatement(whileStatement: WhileStatement) {}
    fun onReturnStatement(returnStatement: ReturnStatement) {}
    fun onPrintStatement(printStatement: PrintStatement) {}
    fun onExpressionStatement(expressionStatement: ExpressionStatement) {}
    fun onBlockStatement(blockStatement: BlockStatement) {}
    fun onCallExpression(callExpression: CallExpression) {}
    fun onUnaryOperatorExpression(unaryOperatorExpression: UnaryOperatorExpression) {}
    fun onBinaryOperatorExpression(binaryOperatorExpression: BinaryOperatorExpression) {}
    fun onStringLiteralExpression(stringLiteralExpression: StringLiteralExpression) {}
    fun onNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression) {}
    fun onBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression) {}
    fun onNullLiteralExpression(nullLiteralExpression: NullLiteralExpression) {}
    fun onIdentifierExpression(identifierExpression: IdentifierExpression) {}

    fun onIdentifier(identifier: Identifier) {}
}

class DfsWalker(private val node: AstNode) {
    fun walk(listener: AstListener) {
        DfsVisitor(listener).visit(node)
    }

    private class DfsVisitor(private val listener: AstListener) : AstVisitor<Unit> {
        private inline fun continueVisiting(astNode: AstNode, action: () -> Unit) {
            action()
            for (child in astNode.children) {
                visit(child)
            }
        }

        override fun visitProgram(program: Program) = continueVisiting(program) {
            listener.onProgram(program)
        }

        override fun visitBlock(block: Block) = continueVisiting(block) {
            listener.onBlock(block)
        }

        override fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement) =
            continueVisiting(variableDeclarationStatement) {
                listener.onVariableDeclarationStatement(variableDeclarationStatement)
            }

        override fun visitAssignmentStatement(assignmentStatement: AssignmentStatement) =
            continueVisiting(assignmentStatement) {
                listener.onAssignmentStatement(assignmentStatement)
            }

        override fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement) =
            continueVisiting(functionDeclarationStatement) {
                listener.onFunctionDeclarationStatement(functionDeclarationStatement)
            }

        override fun visitIfStatement(ifStatement: IfStatement) = continueVisiting(ifStatement) {
            listener.onIfStatement(ifStatement)
        }

        override fun visitWhileStatement(whileStatement: WhileStatement) = continueVisiting(whileStatement) {
            listener.onWhileStatement(whileStatement)
        }

        override fun visitReturnStatement(returnStatement: ReturnStatement) = continueVisiting(returnStatement) {
            listener.onReturnStatement(returnStatement)
        }

        override fun visitPrintStatement(printStatement: PrintStatement) = continueVisiting(printStatement) {
            listener.onPrintStatement(printStatement)
        }

        override fun visitExpressionStatement(expressionStatement: ExpressionStatement) =
            continueVisiting(expressionStatement) {
                listener.onExpressionStatement(expressionStatement)
            }

        override fun visitBlockStatement(blockStatement: BlockStatement) = continueVisiting(blockStatement) {
            listener.onBlockStatement(blockStatement)
        }

        override fun visitCallExpression(callExpression: CallExpression) = continueVisiting(callExpression) {
            listener.onCallExpression(callExpression)
        }

        override fun visitUnaryOperatorExpression(unaryOperatorExpression: UnaryOperatorExpression) =
            continueVisiting(unaryOperatorExpression) {
                listener.onUnaryOperatorExpression(unaryOperatorExpression)
            }

        override fun visitBinaryOperatorExpression(binaryOperatorExpression: BinaryOperatorExpression) =
            continueVisiting(binaryOperatorExpression) {
                listener.onBinaryOperatorExpression(binaryOperatorExpression)
            }

        override fun visitStringLiteralExpression(stringLiteralExpression: StringLiteralExpression) =
            continueVisiting(stringLiteralExpression) {
                listener.onStringLiteralExpression(stringLiteralExpression)
            }

        override fun visitNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression) =
            continueVisiting(numberLiteralExpression) {
                listener.onNumberLiteralExpression(numberLiteralExpression)
            }

        override fun visitBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression) =
            continueVisiting(booleanLiteralExpression) {
                listener.onBooleanLiteralExpression(booleanLiteralExpression)
            }

        override fun visitNullLiteralExpression(nullLiteralExpression: NullLiteralExpression) =
            continueVisiting(nullLiteralExpression) {
                listener.onNullLiteralExpression(nullLiteralExpression)
            }

        override fun visitIdentifierExpression(identifierExpression: IdentifierExpression) =
            continueVisiting(identifierExpression) {
                listener.onIdentifierExpression(identifierExpression)
            }

        override fun visitIdentifier(identifier: Identifier) = continueVisiting(identifier) {
            listener.onIdentifier(identifier)
        }
    }
}
