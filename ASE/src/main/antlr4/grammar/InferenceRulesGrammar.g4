grammar InferenceRulesGrammar;

options {
    // Otuput Language
    language = Java;
}

parseInferenceRule: (start_rule);

start_rule: (prefix)?((new_rule)|(inconsistency)|(rule_graph))*;

new_rule: ((rule_information1) (premises) (filter)?(conclusions));

inconsistency: ((rule_information2)(premises) (filter)?);

rule_graph: ((rule_information3) (premises) (filter)? (conclusions));

prefix: 'prefix' (PNAME_NS URIREF)+; 

rule_information1 : TYPE1 RULE_NAME RULE_ID;

rule_information2 : TYPE2 RULE_NAME RULE_ID;

rule_information3 : TYPE3 RULE_NAME RULE_ID; 

premises: ((START_PREMISES) (triple)+);

triple: (subject) (predicate) (object);

subject: var|list|uri|blanknode|resourcelist;

predicate: uri|var;

object: var|list|uri|literal|blanknode|resourcelist;

var : VAR1;

list: VAR2; 

resourcelist: VAR3;

uri: URIREF|prefixedName;

prefixedName : PNAME_LN|PNAME_NS;

literal: language|type;

language: (STRING_LITERAL1|STRING_LITERAL2) (LANGTAGORANNOTATION)?;

type: (STRING_LITERAL1|STRING_LITERAL2) ('^^' uri)?;

blanknode: BLANK_NODE_LABEL;

filter: FILTER condition;

condition: (expression) | (complex_condition)* ;
complex_condition : ('('?complex_expression_left BOOLEAN complex_expression_right')'?);
complex_expression_left: '('complex_expression')';
complex_expression_right: '('complex_expression')';
complex_expression: expression || expression_left BOOLEAN expression_right;
expression_left : expression|complex_expression_left;
expression_right: expression|complex_expression_right;

expression: object LOGIC_OPERATOR object ;

conclusions: ((START_CONCLUSIONS) triple+); 

PNAME_NS : PN_PREFIX? ':';


PN_PREFIX : PN_CHARS_BASE ((PN_CHARS/*|'.'*/)* PN_CHARS)?; // Dot removed since it causes a bug in the generated Lexer;


PN_CHARS_BASE : 'A'..'Z' | 'a'..'z'| '\u00C0'..'\u00D6' | '\u00D8'..'\u00F6'| '\u00F8'..'\u02FF' | '\u0370'..'\u037D'|
                '\u037F'..'\u1FFF' | '\u200C'..'\u200D'| '\u2070'..'\u218F' | '\u2C00'..'\u2FEF' | '\u3001'..'\uD7FF' |
                '\uF900'..'\uFDCF' | '\uFDF0'..'\uFFFD'; //TODO: add the following | [#x10000-#xEFFFF];            


PN_CHARS : PN_CHARS_U | '-' | '0'..'9' | '\u00B7' | '\u0300'..'\u036F' | '\u203F'..'\u2040';


PN_CHARS_U : PN_CHARS_BASE | '_';


RULE_NAME :
('name : ' |'name: ') (Letter | Symbol)*Letter (Letter|Symbol)* ; 

fragment Letter: 'A'..'Z' | 'a'..'z';

fragment Symbol : '_'|'/'|'&'|'#'|'.'|':'|'0'..'9';

RULE_ID : ('id: ' | 'id : ') NUMBER;

TYPE1 : ('type: ' | 'type : ') 'rule';

TYPE2 : ('type: ' | 'type : ') 'inconsistency';

TYPE3: ('type: '| 'type : ') ('rulegraph'|'ruleGraph');
         
START_PREMISES : 'premises : ' | 'premises: ';

VAR1:'?' ('a'..'z'|'A'..'Z'|'_') (('a'..'z'|'A'..'Z'|'_')|'0'..'9')*;

VAR2:'$' ('a'..'z'|'A'..'Z'|'_') (('a'..'z'|'A'..'Z'|'_')|'0'..'9')*;

VAR3: '$$'('a'..'z'|'A'..'Z'|'_') (('a'..'z'|'A'..'Z'|'_')|'0'..'9')*;

STRING_LITERAL1 : '\'' ( (~('\u0027'|'\u005C'|'\u000A'|'\u000D')) | ECHAR )* '\'';

STRING_LITERAL2 : '"' ( (~('\u0022'|'\u005C'|'\u000A'|'\u000D')) | ECHAR )* '"';

ECHAR : '\\' ('t'|'b'|'n'|'r'|'f'|'\\'|'\''|'"');

LANGTAGORANNOTATION : AT JAVA_IDENTIFIER;

AT : '@';

JAVA_IDENTIFIER : ('a'..'z'|'A'..'Z'|'_') (('a'..'z'|'A'..'Z'|'_') | '0'..'9')*;
  
NUMBER: ('0'..'9')+;

BLANK_NODE_LABEL : '_:' (PN_CHARS_U | ':' | '0'..'9' | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;

PLX : PERCENT | PN_LOCAL_ESC;

PERCENT : '%' ('0'..'9' | 'A'..'F' | 'a'..'f') ('0'..'9' | 'A'..'F' | 'a'..'f');

PN_LOCAL_ESC : '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?'
               | '#' | '@' | '%' );

URIREF: '<' (~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '`' | '\u0000'..'\u0020'))* '>'; 

PNAME_LN : PNAME_NS (PN_CHARS_U | ':' | '0'..'9' | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;

FILTER: 'filter: ' | 'filter : ';

BOOLEAN: '&&' | '||';

LOGIC_OPERATOR: ' != ' | ' < ' | ' <= ' | ' > ' | ' >= ' | ' = ';

START_CONCLUSIONS: 'conclusions : '|'conclusions: ';

WS : [ \r\t\n]+ -> skip ; // skip spaces, tabs, newlines