package resolve

import ast.*
import stdlib.BuiltInFunction

fun resolveIdentifiers(buildInFunctions: List<BuiltInFunction>, program: Program): ResolutionResult =
    IdentifierResolverVisitor(buildInFunctions).resolve(program)

data class ResolutionResult(val resolvedIdentifiers: Map<Identifier, Value>, val blockSizes: Map<BlockId, Int>)

private class IdentifierResolverVisitor(buildInFunctions: List<BuiltInFunction>) : AstVisitor<Unit> {
    private val resolvedIdentifiers = mutableMapOf<Identifier, Value>()
    private val blockSizes = mutableMapOf<BlockId, Int>()

    private val globalScope = Scope.Global(buildInFunctions)
    private var currentScope: Scope = globalScope

    fun resolve(program: Program): ResolutionResult {
        visit(program)
        return ResolutionResult(resolvedIdentifiers, blockSizes)
    }

    override fun visitBlock(block: Block) {
        val previousScope = currentScope
        val newScope = Scope.Block(currentScope)
        currentScope = newScope
        super.visitBlock(block)
        blockSizes[block.blockId] = newScope.nDeclaredLocalVariables
        currentScope = previousScope
    }

    override fun visitStringLiteralExpression(stringLiteralExpression: StringLiteralExpression) {}

    override fun visitNumberLiteralExpression(numberLiteralExpression: NumberLiteralExpression) {}

    override fun visitBooleanLiteralExpression(booleanLiteralExpression: BooleanLiteralExpression) {}

    override fun visitNullLiteralExpression(nullLiteralExpression: NullLiteralExpression) {}

    override fun visitVariableDeclarationStatement(variableDeclarationStatement: VariableDeclarationStatement) {
        visit(variableDeclarationStatement.value)
        val identifier = variableDeclarationStatement.variableName
        val value = currentScope.declareValue(identifier.text)
        resolvedIdentifiers[identifier] = value
    }

    override fun visitIdentifier(identifier: Identifier) {
        val value = currentScope[identifier.text] ?: error("Not declared identifier ${identifier.text}")
        if (identifier in resolvedIdentifiers) {
            error("Internal error: identifier ${identifier.token} resolved twice")
        } else {
            resolvedIdentifiers[identifier] = value
        }
    }
}

private sealed class Scope {
    protected val values = mutableMapOf<String, Value>()

    abstract val nextStackSlot: Int

    abstract fun declareValue(identifier: String): Value
    abstract operator fun get(identifier: String): Value?
    abstract operator fun contains(identifier: String): Boolean

    class Global(buildInFunctions: List<BuiltInFunction>) : Scope() {
        init {
            for (builtInFunction in buildInFunctions) {
                values[builtInFunction.name] = builtInFunction
            }
        }

        override val nextStackSlot = 1

        override fun declareValue(identifier: String): Value {
            if (identifier in values) {
                error("Variable $identifier already declared")
            } else {
                return UserGlobalVariable(identifier).also { values[identifier] = it }
            }
        }

        override operator fun contains(identifier: String): Boolean = identifier in values
        override operator fun get(identifier: String): Value? = values[identifier]
    }

    class Block(val parentScope: Scope) : Scope() {
        override val nextStackSlot: Int
            get() = parentScope.nextStackSlot + values.size

        val nDeclaredLocalVariables: Int
            get() = values.size

        override fun declareValue(identifier: String): Value {
            if (identifier in values) {
                error("Variable $identifier already declared")
            } else {
                return UserLocalVariable(identifier, nextStackSlot.toByte()).also { values[identifier] = it }
            }
        }

        override operator fun contains(identifier: String): Boolean = identifier in values
        override operator fun get(identifier: String): Value? = values[identifier] ?: parentScope[identifier]
    }
}
