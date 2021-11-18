grammar SimpleLanguage;

import SLTokens;

prog: statement+ EOF;

statement
    : variableDefinition ';' # variableDefStmt
    | variableAssignment ';' # variableAssignmentStmt
    | receiver=expression DOT ID ASSIGN value=expression ';' # propertyAssignment
    | block # blockStmt
    | expression ';' # expressionStmt
    | functionDefinition # functionDefinitionStmt
    | classDefinition # classDefinitionStmt
    | IF LPAREN expression RPAREN thenBlock=block (ELSE elseBlock=block)? # ifStmt
    | WHILE LPAREN expression RPAREN body=block # whileStmt
    | RETURN expression ';' # returnStatement
    ;

classDefinition: CLASS name=ID (LT superClass=ID)? classBody?;

classBody: LBRACE (functionDefinition)* RBRACE;

functionDefinition: FN ID LPAREN functionArguments? RPAREN functionBody;

block: LBRACE statement* RBRACE;

variableDefinition: VAR ID ASSIGN expression;
variableAssignment: ID ASSIGN expression;

commaSeparatedExpressions: expression (COMMA expression)*;

expression
    : LPAREN expression RPAREN # ParenExpr
    | receiver=expression DOT ID # propertyAccessExpression
    | SUPER DOT ID # superPropertyAccessExpression
    | expression LPAREN commaSeparatedExpressions? RPAREN # expressionCallExpr
    | op=(NOT|SUB) expression # UnaryOperatorExpr
    | left=expression IS ID # isExpr
    | left=expression op=(MUL|DIV|MOD|AND) right=expression # OperatorExpr
    | left=expression op=(ADD|SUB|OR) right=expression # OperatorExpr
    | left=expression rel=(GT|GTE|LT|LTE|EQ|NEQ) right=expression # RelExpr
    | STRINGLITERAL # StringLiteralExpr
    | number=NUMBER # NumberLiteralExpr
    | booleanLiteral # BooleanLiteralExpr
    | ID # IdLiteralExpr
    | THIS # ThisExpr
    | NULL # NullExpr
    ;

functionArguments: ID (COMMA ID)*;

functionBody: LBRACE statement* RBRACE;

booleanLiteral: booleanValue=(TRUE | FALSE);
