package com.github.justinespinosa.intellicob.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.github.justinespinosa.intellicob.psi.sqlmp.SqlMpTokenTypeFactory.getToken;
import static com.github.justinespinosa.intellicob.psi.sqlmp.SqlMpTokenTypeFactory.getTokenHost;
import static com.github.justinespinosa.intellicob.psi.sqlmp.SqlMpTypes.*;
import static com.intellij.psi.TokenType.*;

%%

%class SqlMpLexer
%implements MultiStreamFlexLexer
%unicode
%caseless
%function advance
%type IElementType
%eof{  return;
%eof}

//general

WHITE_SPACE=[\ \n\t\f]
WORD=[a-zA-Z0-9][a-zA-Z0-9_]*
HOST_WORD=[a-zA-Z0-9][a-zA-Z0-9-]*
HOST_VAR=":"{HOST_WORD}
FILE_PART=[a-zA-Z][a-zA-Z0-9]*
DEFINE="="{FILE_PART}
GUARDIAN_FILENAME=("$"{FILE_PART}".")?{FILE_PART}("."{FILE_PART})?
COMMA=","
NUMBER=[0-9]+(.[0-9]*)?
OP="("
CP=")"



%{
   private IElementType wordToken(){
       return getToken(yytext());
   }
   private IElementType hostWordToken(){
       return getTokenHost(yytext());
   }

%}

%state STRING_STATE

%%


<STRING_STATE>
{
 \"          { yybegin(YYINITIAL); return STRING_LITERAL;}
 .           { }
}


<YYINITIAL>
{

 \"         { yybegin(STRING_STATE); }


{WHITE_SPACE}   { return WHITE_SPACE; }
{WORD}           {return wordToken();}
{HOST_WORD} {return hostWordToken();}
{HOST_VAR} {return HOST_VAR;}
{DEFINE} {return DEFINE;}
{NUMBER} { return NUMBER;}
{COMMA} {return COMMA;}
{OP} {return OP;}
{CP} {return CP;}
"=" { return EQUAL;}
"<>" { return NOT_EQUAL;}
"<" { return LESS_THAN;}
">" { return GREATER_THAN;}
"<=" { return LASS_THAN_OR_EQUAL_TO;}
">=" { return GREATER_THAN_OR_EQUAL_TO;}

{GUARDIAN_FILENAME} { return GUARDIAN_FILENAME;}

}
