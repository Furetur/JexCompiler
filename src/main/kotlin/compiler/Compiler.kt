package compiler

import ast.*
import codegen.*
import codegen.dsl.*
import resolve.GettableValue
import resolve.ResolutionResult
import resolve.SettableValue
import stdlib.BuiltInFunction
import stdlib.addBuiltInFunction

fun compile(
    builtInFunctions: List<BuiltInFunction>,
    resolutionResult: ResolutionResult,
    program: Program,
    printAssembly: Boolean
): Bytecode {
    val compilerVisitor = CompilerVisitor(builtInFunctions, resolutionResult)
    val bytecodeBuilder = compilerVisitor.compile(program)
    return bytecodeBuilder.bytecode().also {
        if (printAssembly) bytecodeBuilder.printAssembly()
    }
}

private class CompilerVisitor(builtInFunctions: List<BuiltInFunction>, private val resolutionResult: ResolutionResult) :
    AstVisitor<Code> {
    private val bytecodeBuilder = BytecodeBuilder()
    private val builtInFunctionReferences = mutableMapOf<String, BytecodeBuilder.ChunkReference>()

    init {
        for (builtInFunction in builtInFunctions) {
            val functionReference = bytecodeBuilder.addBuiltInFunction(builtInFunction)
            builtInFunctionReferences[builtInFunction.name] = functionReference
        }
    }

    fun compile(program: Program): BytecodeBuilder {
        val code = visitProgram(program)
        bytecodeBuilder.main {
            +code
        }
        return bytecodeBuilder
    }

    override val default: Code = {}

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

    override fun visitFunctionDeclarationStatement(functionDeclarationStatement: FunctionDeclarationStatement): Code = {
        val (name, formalArguments, statements) = functionDeclarationStatement
        val functionReference = bytecodeBuilder.addFunction(name.text, formalArguments.size) {
            for (statement in statements) {
                +visit(statement)
            }
            ret { literal(null) }
        }
        val resolvedFunctionName = resolutionResult.resolvedIdentifiers[name] ?: error("Function name not resolved :(")
        require(resolvedFunctionName is SettableValue) { "Cannot declare a function with name $name" }
        declareValue(resolvedFunctionName) { function(functionReference) }
    }

    override fun visitReturnStatement(returnStatement: ReturnStatement): Code = {
        ret(visit(returnStatement.value))
    }

    override fun visitCallExpression(callExpression: CallExpression): Code = {
        +visit(callExpression.callee)
        for (argument in callExpression.arguments) {
            +visit(argument)
        }
        call(callExpression.arguments.size.toByte())
    }

    override fun visitIfStatement(ifStatement: IfStatement): Code = {
        val condition = visit(ifStatement.condition)
        val thenCode = visit(ifStatement.thenBlock)
        val elseCode = ifStatement.elseBlock?.let { visit(it) } ?: {}
        ifStatement(condition, thenCode, elseCode)
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
            "&&" -> and(left, right)
            "||" -> or(left, right)
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
            if (value is BuiltInFunction) {
                val functionReference =
                    builtInFunctionReferences[value.name] ?: error("Built in function reference not found")
                function(functionReference)
            } else {
                getValue(value)
            }
        } else {
            error("Identifier $identifier cannot be used as an expression")
        }
    }

    override fun visitIdentifier(identifier: Identifier): Code = {}
}
