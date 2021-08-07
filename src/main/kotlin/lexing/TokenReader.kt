package lexing

class TokenReader(private val tokens: List<Token>) {
    private var nextLexemeIndex = 0

    fun hasNext(): Boolean = nextLexemeIndex in tokens.indices

    fun advance(): Token? = tokens.getOrNull(nextLexemeIndex++)

    fun lookahead(i: Int): Token? = tokens.getOrNull(nextLexemeIndex + i - 1)
}
