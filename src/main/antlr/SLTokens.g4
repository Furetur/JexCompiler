lexer grammar SLTokens;

// literals
STRINGLITERAL: '"' ~["\\\r\n]* '"';

// assignment
ASSIGN: '=';

// arithmetical operator
MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
MOD: 'mod';

// relations
EQ: '==';
NEQ: '!=';
GT: '>';
GTE: '>=';
LT: '<';
LTE: '<=';

IS: 'is';

// logic operators
AND: '&&';
OR: '||';
NOT: '!';

// other
DOT: '.';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
COMMA: ',';


// keywords
VAR: 'var';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
FN: 'fn';
RETURN: 'return';
CLASS: 'class';
THIS: 'this';
SUPER: 'super';

// whitespace and comments
WS: [ \t\r\n]+ -> skip;
SINGLE_LINE_COMMENT: '//' ~[\r\n]* -> skip;

// literals
ID: [a-zA-Z]+;
NUMBER: [0-9]+;
