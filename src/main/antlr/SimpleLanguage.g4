grammar SimpleLanguage;

import SLTokens;

prog: statement+ EOF;

statement
    : variableDefinition NEWLINE # variableDefStmt
    | variableAssignment NEWLINE # variableAssignmentStmt
    | receiver=expression DOT ID ASSIGN value=expression NEWLINE # propertyAssignment
    | block NEWLINE # blockStmt
    | expression NEWLINE # expressionStmt
    | functionDefinition NEWLINE # functionDefinitionStmt
    | classDefinition NEWLINE # classDefinitionStmt
    | IF LPAREN expression RPAREN thenBlock=block (ELSE elseBlock=block)? NEWLINE # ifStmt
    | WHILE LPAREN expression RPAREN body=block NEWLINE # whileStmt
    | RETURN expression NEWLINE # returnStatement
    | NEWLINE # newlineStmt
    ;

classDefinition: CLASS name=ID (LT superClass=ID)? classBody?;

classBody: LBRACE NEWLINE* (functionDefinition NEWLINE+)* NEWLINE* RBRACE;

functionDefinition: FN ID LPAREN functionArguments? RPAREN functionBody;

block: LBRACE NEWLINE? statement* NEWLINE? RBRACE;

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

functionBody: LBRACE NEWLINE? statement* NEWLINE? RBRACE;

booleanLiteral: booleanValue=(TRUE | FALSE);
