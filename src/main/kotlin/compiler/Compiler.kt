package compiler

import ast.*
import codegen.*
import codegen.dsl.*
import resolve.GettableValue
import resolve.ResolutionResult
import resolve.SettableValue

fun compile(resolutionResult: ResolutionResult, program: Program): Bytecode {
    val compilerVisitor = CompilerVisitor(resolutionResult)

    val bytecodeBuilder = BytecodeBuilder()
    bytecodeBuilder.main {
        +compilerVisitor.visit(program)
    }
    return bytecodeBuilder.bytecode().also { bytecodeBuilder.printAssembly() }
}

private class CompilerVisitor(private val resolutionResult: ResolutionResult) : AstVisitor<Code> {
    override fun visitProgram(program: Program): Code = {
        for (statement in program.statements) {
            +visit(statement)
        }
    }

    override fun visitBlock(block: Block): Code = {
        for (statement in block.statements) {
            +visit(statement)
        }
        val localVariablesInBlock = resolutionResult.blockSizes[block.blockId] ?: error("Block size was not calculated")
        repeat(localVariablesInBlock) {
            pop()
        }
    }

    override fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement): Code = {
        val identifier = variableDeclarationStatement.variableName
        val value = resolutionResult.resolvedIdentifiers[identifier] ?: error("Identifier $identifier not resolved")
        require(value is SettableValue) { "Cannot declare variable with identifier $identifier" }
        +value.declareValue(visit(variableDeclarationStatement.value))
    }

    override fun visitAssignmentStatement(assignmentStatement: AssignmentStatement): Code = {
        val assignee = assignmentStatement.assignee
        require(assignee is Identifier) { "Can assign a value only to identifier" }
        val value = resolutionResult.resolvedIdentifiers[assignee] ?: error("Identifier $assignee is not resolved")
        require(value is SettableValue) { "Cannot set value to $assignee" }
        setValue(value, visit(assignmentStatement.value))
    }

    override fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement): Code {
        TODO("Implement functions")
    }

    override fun visitReturnStatement(returnStatement: ReturnStatement): Code {
        TODO("Implement functions")
    }

    override fun visitCallExpression(callExpression: CallExpression): Code {
        TODO("Implement functions")
    }

    // TODO: remove this atrocity
    override fun visitPrintStatement(printStatement: PrintStatement): Code = {
        TODO("remove this")
    }

    override fun visitIfStatement(ifStatement: IfStatement): Code = {
        require(ifStatement.elseBlock == null) { "Else blocks are not supported yet" }
        ifStatement(condition = visit(ifStatement.condition), thenCode = visit(ifStatement.thenBlock))
    }
    override fun visitWhileStatement(whileStatement: WhileStatement): Code = {
        whileLoop(condition = visit(whileStatement.condition), body = visit(whileStatement.body))
    }

    override fun visitExpressionStatement(expressionStatement: ExpressionStatement): Code = {
        +visit(expressionStatement.expression)
        pop()
    }

    override fun visitBlockStatement(blockStatement: BlockStatement): Code = {
        +visit(blockStatement.block)
    }

    override fun visitBinaryOperatorExpression(binaryOperatorExpression: BinaryOperatorExpression): Code = {
        val left = visit(binaryOperatorExpression.left)
        val right = visit(binaryOperatorExpression.right)
        when (binaryOperatorExpression.operator.text) {
            "+" -> add(left, right)
            "*" -> multiply(left, right)
            "/" -> divide(left, right)
            "-" -> subtract(left, right)
            "&&" -> TODO("Binary && not supported")
            "||" -> TODO("Binary || not supported")
            ">" -> greater(left, right)
            "<" -> less(left, right)
            ">=" -> not { less(left, right) }
            "<=" -> not { greater(left, right) }
            "==" -> equal(left, right)
            "!=" -> not { equal(left, right) }
        }
    }

    override fun visitUnaryOperatorExpression(unaryOperatorExpression: UnaryOperatorExpression): Code = {
        val value = visit(unaryOperatorExpression.operand)
        when (unaryOperatorExpression.operator.text) {
            "!" -> not { +value }
            "-" -> negate { +value }
        }
    }

    override fun visitStringLiteralExpression(stringLiteralExpression: StringLiteralExpression): Code = {
        literal(stringLiteralExpression.value)
    }

    override fun visitNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression): Code = {
        literal(numberLiteralExpression.value)
    }

    override fun visitBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression): Code = {
        literal(booleanLiteralExpression.value)
    }

    override fun visitNullLiteralExpression(nullLiteralExpression: NullLiteralExpression): Code = {
        literal(null)
    }

    override fun visitIdentifierExpression(identifierExpression: IdentifierExpression): Code = {
        val identifier = identifierExpression.identifier
        val value = resolutionResult.resolvedIdentifiers[identifier] ?: error("Resolved identifier not found")
        if (value is GettableValue) {
            getValue(value)
        } else {
            error("Identifier $identifier cannot be used as an expression")
        }
    }

    override fun visitIdentifier(identifier: Identifier): Code = {}
}
