# JexCompiler

> A compiler for the simple programming language that I've been working on.

I wanted to learn about compilers and ended up reading [Crafting Interpreters](https://craftinginterpreters.com/) and writing this compiler
and a [virtual machine](https://github.com/Furetur/jex_vm).

It supports
- [x] ints, booleans
- [x] strings
- [x] addition, multiplication, division, subtraction, concatenation
- [x] logical operations
- [x] local and global variables
- [x] if, if-else, while loops
- [x] functions
- [x] build-in functions: `input`, `readLine`, `int`, `fact`
- [x] objects and `object()` function
- [ ] linked lists with normal API
- [ ] closures

## How to run Jex

To run Jex code you need to run

1. Download JexCompiler-\<version\>.jar from the latest release
2. Run
  ```shell
  java -jar JexCompiler-<version>.jar path/to/code.txt path/to/output.bytecode
  ```
3. Run `path/to/output.bytecode` with [jex_vm](https://github.com/Furetur/jex_vm).


## Examples

**WARNING**: Be very careful with newline characters, the parser is not finished yet 😎.

### Ints, booleans, strings

```
1 + 2 * 5 - (10 / 2)
true && false
true || false
"a" + "b"
```

### Variables

```
var globalVariable = 1
{
  var localVariable = 2 
}
```

### Read and write

```
var readFromTerminal = readLine()
println("Hello World")

var readInt = int(readLine())

if (readInt == null) {
    println("Failed to parse int")
} else {
    println("Success")
}
```

### Ifs and whiles

```
var name = readLine()
if (name == "Emir") {
    println("Hehe")
} else {
    println("Hey")
}

if (name == "emir") {
    println("You should capitalize your name, emir")
}
```

```
while(true) {
    println("Working")
}
```

### Functions

```
fn greet(name) {
    println("Hello, " + name)
}

println("Whats your name?")
greet(readLine())
```

Functions are just regular values

```
var a = println
println(a)
```

### Objects

Firstly, you have to create an empty object and then add some properties.

```
var person = object()
person.name = "Emir"

println(person.name)
```

Linked list [example](src/test/resources/linked_list.txt).

You can find some examples of Jex code in the [resources directory](src/test/resources).
