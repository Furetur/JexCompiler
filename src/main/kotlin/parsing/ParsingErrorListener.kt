package parsing

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.misc.ParseCancellationException
import java.util.*


object ParsingErrorListener : BaseErrorListener() {
    private val errorStack = Stack<String>()

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        errorStack.add("line$line:$charPositionInLine -> $msg")
    }

    fun throwErrorsStackIfNotEmpty() {
        if (errorStack.isNotEmpty()) {
            throw ParseCancellationException(errorStack.joinToString("\n"))
        }
    }
}