// Goal is to build a compiler FPP + Proteus -> C++

# MyLang #
'''
var is a Variable
num is a Number
type ::= 'int' | 'bool'
vardec ::= '(' 'vardec' type var expression ')'
expression ::= num | 'true' | 'false' | var | '(' op expression expression ')'
loop ::= '(' '='  var expression ')'
assign ::= '(' '=' var expression ')'
statement ::= vardec | loop | assign
op ::= '+' | '-' | '&&' | '||' | '<'
program ::= statement*
'''

