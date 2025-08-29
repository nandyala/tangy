grammar OzEl;

entry
  : ((LPAREN program RPAREN)|program) EOF
  ;

program
  : (expression|block|LCURLY programStatement (COMMA programStatement)* RCURLY)
  ;

programStatement
  :  LCURLY (expression|block) RCURLY
  ;

statement
  : blockStatement                                                                                  #StatementBlock
  | IF parExpression statement (ELSE statement)?                                                    #StatementIf
  | FOR LPAREN forControl RPAREN statement					                                        #StatementFor
  | FOREACH LPAREN foreachControl RPAREN statement                                                  #StatementForeach
  | WHILE parExpression statement							                                        #StatementWhile
  | DO statement WHILE parExpression SEMI					                                        #StatementDo
  | SWITCH parExpression switchBlock                                                                #StatementSwitch
  | statementExpression                                                                             #StatementExpr
  | returnStatement											                                        #StatementReturn
  | SEMI                                                                                            #StatementEmpty
  ;

blockStatement
  : LCURLY block RCURLY
  ;

block
  :  statement*
  ;

statementExpression
  : expression SEMI
  ;

constantExpression
  : expression
  ;

returnStatement
  : RETURN expression SEMI
  ;

parExpression
  : LPAREN expression RPAREN
  ;

expressionList
  : expression (COMMA expression)*
  ;

switchLabel
  : CASE constantExpression COLON                                                                   #SwitchCase
  | DEFAULT COLON                                                                                   #SwitchDefault
  ;

switchBlock
  : LCURLY switchBlockStatementGroup* switchLabel* RCURLY
  ;

switchBlockStatementGroup
  : switchLabel+ statement
  ;

foreachControl
  : variable COLON expression
  ;

forInit
  : expressionList
  ;

forUpdate
  : expressionList
  ;

forControl
  : forInit? SEMI expression? SEMI forUpdate?
  ;

expression
  : primary                                                                                         #PriExpr
  | NEW op=(LONG | INTEGER) LPAREN IntegerLiteral RPAREN                                            #NewExpr
  | expression DOT expression                                                                       #AccessProperty
  | expression LBRACK expression RBRACK                                                             #AccessElement
  | ID LPAREN expressionList? RPAREN                                                                #CallFunction
  | variable op=(PLUSPLUS | MINUSMINUS)                                                             #PostfixOp
  | op=(PLUSPLUS | MINUSMINUS) variable                                                             #PrefixOp
  | op=(PLUS | MINUS) expression                                                                    #UnaryOp
  | NOT expression                                                                                  #NotExpression
  | expression op=(STAR | SLASH | MOD) expression                                                   #MulDiv
  | expression op=(PLUS | MINUS) expression                                                         #AddSub
  | expression op=(LE | GE | GT | LT | IN) expression                                               #Compare
  | expression op=(EQUAL | NOT_EQUAL) expression                                                    #Equal
  | expression AND expression                                                                       #And
  | expression OR expression                                                                        #Or
  | expression QUESTION expression COLON expression                                                 #Select
  | variable op=(ASSIGN | PLUSEQUAL | MINUSEQUAL | STAREQUAL | SLASHEQUAL | MODEQUAL) expression	#AssignmentExpr
  ;

primary
  : LPAREN expression RPAREN                                                                        #ExprPrimary
  | LBRACK expressionList? RBRACK                                                                   #ArrayPrimary
  | literal                                                                                         #LiteralPrimary
  | ID                                                                                              #IdentPrimary
  ;

literal
  : IntegerLiteral                                                                                  #IntValue
  | RealLiteral                                                                                     #RealValue
  | StringLiteral                                                                                   #StringValue
  | booleanLiteral                                                                                  #BoolValue
  | NULL                                                                                            #NullValue
  ;

booleanLiteral
  : TRUE                                                                                            #TrueValue
  | FALSE                                                                                           #FalseValue
  ;

variable
  : ID
  ;

//----------------------------------------------------------------------------
// KEYWORDS
//----------------------------------------------------------------------------
AND            : (('a'|'A')('n'|'N')('d'|'D'))| '&&' | '&';
BREAK          : ('b'|'B')('r'|'R')('e'|'E')('a'|'A')|('k'|'K');
CASE           : ('c'|'C')('a'|'A')('s'|'S')('e'|'E');
CHAR           : ('c'|'C')('h'|'H')('a'|'A')('r'|'R');
CONTINUE       : ('c'|'C')('o'|'O')('n'|'N')('t'|'T')('i'|'I')('n'|'N')('u'|'U')('e'|'E');
DEFAULT        : ('d'|'D')('e'|'E')('f'|'F')('a'|'A')('u'|'U')('l'|'L')('t'|'T');
DO		       : ('d'|'D')('o'|'O');
ELSE           : ('e'|'E')('l'|'L')('s'|'S')('e'|'E');
FOR            : ('f'|'F')('o'|'O')('r'|'R');
FOREACH        : ('f'|'F')('o'|'O')('r'|'R')('e'|'E')('a'|'A')('c'|'C')('h'|'H');
FALSE          : ('f'|'F')('a'|'A')('l'|'L')('s'|'S')('e'|'E');
IF             : ('i'|'I')('f'|'F');
IN             : ('i'|'I')('n'|'N');
NULL           : ('n'|'N')('u'|'U')('l'|'L')('l'|'L');
NOT            : (('n'|'N')('o'|'O')('t'|'T'))| '!';
OR             : (('o'|'O')('r'|'R'))| '||' | '|';
SWITCH         : ('s'|'S')('w'|'W')('i'|'I')('t'|'T')('c'|'C')('h'|'H');
RETURN         : ('r'|'R')('e'|'E')('t'|'T')('u'|'U')('r'|'R')('n'|'N');
THEN           : ('t'|'T')('h'|'H')('e'|'E')('n'|'N');
TRUE           : ('t'|'T')('r'|'R')('u'|'U')('e'|'E');
WHILE		   : ('w'|'W')('h'|'H')('i'|'I')('l'|'L')('e'|'E');

NEW            : ('n'|'N')('e'|'E')('w'|'W');
LONG           : ('l'|'L')('o'|'O')('n'|'N')('g'|'G');
INTEGER        : ('i'|'I')('n'|'N')('t'|'T')('e'|'E')('g'|'G')('e'|'E')('r'|'R');


//----------------------------------------------------------------------------
// OPERATORS
//----------------------------------------------------------------------------
PLUS           : '+'			;
PLUSPLUS       : '+''+'			;
MINUS          : '-'			;
MINUSMINUS     : '-''-'			;
STAR           : '*'			;
SLASH          : '/'			;
MOD            : '%'			;
ASSIGN         : ':='			;
PLUSEQUAL      : '+='			;
MINUSEQUAL     : '-='			;
STAREQUAL      : '*='			;
SLASHEQUAL     : '/='			;
MODEQUAL       : '%='			;
COMMA          : ','			;
SEMI           : ';'			;
COLON          : ':'			;
EQUAL          : '='			;
NOT_EQUAL      : '!=' | '<>'	;
LT             : '<'			;
LE             : '<='			;
GE             : '>='			;
GT             : '>'			;
LPAREN         : '('			;
RPAREN         : ')'			;
LBRACK         : '['			;
RBRACK         : ']'			;
AT             : '@'			;
DOT            : '.'			;
LCURLY         : '{'			;
RCURLY         : '}'			;
QUESTION       : '?'			;


ID : LETTER (LETTER | DIGIT)* ;

IntegerLiteral : DIGIT+ ;

RealLiteral
  :   DIGIT+ '.' DIGIT* EXPONENT?
  |   '.' DIGIT+ EXPONENT?
  |   DIGIT+ EXPONENT?
  ;

StringLiteral
//  :   ('"' StringCharacters? '"') | ('\'' StringCharacters? '\'')
  : ('"' (~["\\]|EscapeSequence)* '"') | ('\'' (~[\'\\]|EscapeSequence)* '\'')
  ;


fragment DIGIT : [0-9] ; // match single digit

fragment LETTER : 'a'..'z'|'A'..'Z'|'_' ;


fragment
StringCharacters
  :   StringCharacter+
  ;

fragment
StringCharacter
  :   ~["\\]
  |   EscapeSequence
  ;

// §3.10.6 Escape Sequences for Character and String Literals

fragment
EscapeSequence
  :   '\\' [btnfr"'\\]
  |   UnicodeEscape
  ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
HexDigits
  :   HexDigit (HexDigitOrUnderscore* HexDigit)?
  ;

fragment
HexDigit
  :   [0-9a-fA-F]
  ;

fragment
HexDigitOrUnderscore
  :   HexDigit
  |   '_'
  ;

// a couple protected methods to assist in matching floating point numbers
fragment
EXPONENT
  : ('e'|'E') ('+'|'-')? ('0'..'9')+
  ;

NEWLINE:'\r'? '\n' -> skip;     // return newlines to parser (is end-statement signal)
WS : [ \t\r\n\u000C]+ -> skip ; // match 1-or-more whitespace but discard

LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // Match "//" stuff '\n'
COMMENT : '/*' .*? '*/' -> skip ; // Match "/*" stuff "*/"
