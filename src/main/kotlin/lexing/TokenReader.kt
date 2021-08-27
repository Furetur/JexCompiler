package lexing

class TokenReader(private val tokens: List<Token>) {
    private var nextTokenIndex = 0

    fun hasNext(): Boolean = nextTokenIndex in tokens.indices

    fun advance(): Token? = tokens.getOrNull(nextTokenIndex++)

    fun peek(i: Int = 1): Token? = tokens.getOrNull(nextTokenIndex + i)

    fun mark() = Mark(nextTokenIndex)

    inner class Mark(private val savedNextTokenIndex: Int) {
        fun jump() {
            nextTokenIndex = savedNextTokenIndex
        }
    }
}

inline fun TokenReader.until(type: TokenType, action: () -> Unit) {
    var nextToken = peek()
    while (nextToken?.type != type) {
        if (nextToken != null) {
            action()
            nextToken = peek()
        } else {
            throw IllegalArgumentException("Was reading until $type but reached unexpected end")
        }
    }
    require(type)
}

fun TokenReader.requireName(): NameToken {
    val token = advance()
    requireNotNull(token) { "Expected name but code ended unexpectedly" }
    require(token is NameToken) { "Expected name but received $token" }
    return token
}

fun TokenReader.require(type: TokenType): Token {
    val token = advance()
    requireNotNull(token) { "Expected $type but code ended unexpectedly" }
    require(token.type == type) { "Expected $type but received $token" }
    return token
}

fun TokenReader.skipOne(type: TokenType) {
    if (peek()?.type == type) {
        advance()
    }
}
