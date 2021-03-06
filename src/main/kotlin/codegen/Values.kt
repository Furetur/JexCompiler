package codegen

import resolve.GettableValue
import resolve.SettableValue

fun ChunkBuilder.getValue(gettableValue: GettableValue) {
    +gettableValue.getValue()
}

fun ChunkBuilder.setValue(settableValue: SettableValue, value: Code) {
    +settableValue.setValue(value)
}

fun ChunkBuilder.declareValue(settableValue: SettableValue, value: Code) {
    +settableValue.declareValue(value)
}
