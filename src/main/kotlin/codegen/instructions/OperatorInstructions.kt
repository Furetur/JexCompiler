package codegen.instructions

import codegen.NullaryInstruction

object PopInstruction : NullaryInstruction(Opcode.Pop)

object PrintInstruction : NullaryInstruction(Opcode.Print)

object NotInstruction : NullaryInstruction(Opcode.Not)

object EqualInstruction : NullaryInstruction(Opcode.Equal)

object GreaterInstruction : NullaryInstruction(Opcode.Greater)

object LessInstruction : NullaryInstruction(Opcode.Less)

object NegateInstruction : NullaryInstruction(Opcode.Negate)

object AddInstruction : NullaryInstruction(Opcode.Add)

object SubtractInstruction : NullaryInstruction(Opcode.Subtract)

object MultiplyInstruction : NullaryInstruction(Opcode.Multiply)

object DivideInstruction : NullaryInstruction(Opcode.Divide)

object ReadLineInstruction : NullaryInstruction(Opcode.ReadLine)

object ParseIntInstruction : NullaryInstruction(Opcode.ParseInt)

object ToStringInstruction : NullaryInstruction(Opcode.ToString)
