package com.github.justinespinosa.intellicob.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.justinespinosa.intellicob.psi.CobolTokenTypeFactory;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;
import static com.intellij.psi.TokenType.*;

%%

%class CobolLexer
%implements FlexLexer
%unicode
%caseless
%function advance
%type IElementType
%eof{  return;
%eof}

//general
CRLF=\R
SPACE=[\ ]
EXT_SPACE=[\ \t]
EXT_SPACE_CRLF=[\ \t\n\r]
WHITE_SPACE=[\ \n\t\f,]
DOT=\.
CODE_INDICATOR={SPACE}
COMMENT_INDICATOR="*"
PREPROCESSOR_INDICATOR="?"
PAGE_INDICATOR="/"
COMMENT_ENTRY=.+
PREPROCESSOR=.+
COBOLWORD=[a-zA-Z0-9][a-zA-Z0-9-]*
INTEGER_=[\+\-]?[0-9]+
NUMBER_=[\+\-]?[0-9]+\.[0-9]+
DOUBLE_EQUAL="=="
EQUAL_SIGN_="="
PLUS_SIGN_="+"
MINUS_SIGN_="-"
TIMES_SIGN_="*"
DIVIDE_SIGN_="/"
POWER_SIGN_="**"
OPAREN_SIGN_="("
CPAREN_SIGN_=")"
GREATER_SIGN_=">"
LESS_SIGN_="<"
GREATER_EQUAL_SIGN_=">="
LESS_EQUAL_SIGN_="<="
COLON_SIGN_=":"
STRING_START=(H|X|N|n)?\"
GUARDIAN_FILE=(\${COBOLWORD})({DOT}\#?{COBOLWORD})?({DOT}{COBOLWORD})?
DEFINE=\={COBOLWORD}

%{
   private boolean returnTokens = true;
   private int previousState = YYINITIAL;
   private int codeState = CODE_STATE;

   private void codeState(int state, boolean retTokens){
        state(state);
        codeState = state;
        returnTokens = retTokens;
   }

   private void specialCode(int state){
       codeState(state, false);
   }

   private void normalCode(){
       codeState(CODE_STATE, true);
   }

   private IElementType cobolWordToken(){
       return CobolTokenTypeFactory.getToken(yytext());
   }

   private void rewind(){
       yypushback(yylength());
   }

   private void state(int state){
       previousState = yystate();
       yybegin(state);
   }

%}

%state COMMENT_STATE CODE_STATE STRING_STATE PSEUDOTEXT_STATE
%state PREPROCESSOR_STATE POSTPREPROCESSOR
%state PRECOMMENT COMMENT_ENTRY_STATE
%state PRECOMPUTERNAME COMPUTER_NAME_STATE POSTCOMPUTERNAME
%state PREOBJECTCOMPUTERNAME OBJECT_COMPUTER_NAME_STATE POSTOBJECTCOMPUTERNAME
%state PREPICTURESTRING PICTURE_STRING_STATE
%state EMBEDDED_SQL_STATE
//The strange case of COPY and REPLACE: instructions which are preprocessor directives...
%state COPY_STATE REPLACE_STATE

%%

<YYINITIAL>
{
 {CODE_INDICATOR}         { state(codeState); if(returnTokens){return CODE_INDICATOR;} }
 {COMMENT_INDICATOR}      { state(COMMENT_STATE); if(returnTokens){return COMMENT_INDICATOR;} }
 {PAGE_INDICATOR}         { state(COMMENT_STATE); if(returnTokens){return PAGE_INDICATOR;} }
 {PREPROCESSOR_INDICATOR} { specialCode(PREPROCESSOR_STATE); if(returnTokens){return PREPROCESSOR_INDICATOR;} }
 {CRLF}                   { if(returnTokens){return WHITE_SPACE;} }
 .                        { return BAD_CHARACTER; }
}

<PREPROCESSOR_STATE>
{
 {CRLF} { state(POSTPREPROCESSOR);  }
 . {}
}

<POSTPREPROCESSOR>
{
  {PREPROCESSOR_INDICATOR} { state(PREPROCESSOR_STATE); }
  .|{CRLF} {rewind(); normalCode(); state(YYINITIAL); return PREPROCESSOR; }
}

<COMMENT_STATE>
{
 {COMMENT_ENTRY} { if(returnTokens){return COMMENT;} }
 {CRLF}          { state(YYINITIAL); if(returnTokens){return WHITE_SPACE;}  }
}

<STRING_STATE>
{
 \"          { state(codeState); if(returnTokens){return STRING_LITERAL;} }
 .           { }
}

<PSEUDOTEXT_STATE>
{
 {DOUBLE_EQUAL} { state(codeState); }
 .              { }
}

<EMBEDDED_SQL_STATE>
{
   \"          { state(STRING_STATE); }
   "END-EXEC"  { normalCode(); return EMBEDDED_SQL; }
   .           {}
   {CRLF}      { state(YYINITIAL); }
}

<CODE_STATE>
{

 {STRING_START}             { state(STRING_STATE); }
 "EXEC"                     { specialCode(EMBEDDED_SQL_STATE); }

 //special rules needing extra states
 "AUTHOR"                   { state(PRECOMMENT); return cobolWordToken();}
 "INSTALLATION"             { state(PRECOMMENT); return cobolWordToken();}
 "DATE-WRITTEN"             { state(PRECOMMENT); return cobolWordToken();}
 "DATE-COMPILED"            { state(PRECOMMENT); return cobolWordToken();}
 "SECURITY"                 { state(PRECOMMENT); return cobolWordToken();}
 "SOURCE-COMPUTER"          { state(PRECOMPUTERNAME); return cobolWordToken();}
 "OBJECT-COMPUTER"          { state(PREOBJECTCOMPUTERNAME); return cobolWordToken();}
 "PIC"                      { state(PREPICTURESTRING); return cobolWordToken();}
 "PICTURE"                  { state(PREPICTURESTRING); return cobolWordToken();}

 //if you still don't know ... see the other comments about copy and replace
 "COPY"                     { specialCode(COPY_STATE); }
 "REPLACE"                  { specialCode(REPLACE_STATE); }

 //reserved word in grammar kit
 "EXTERNAL"                 { return W_EXTERNAL; }

 //end state / whitespace / generic
 {EQUAL_SIGN_}              { return EQUAL_SIGN_; }
 {PLUS_SIGN_}               { return PLUS_SIGN_; }
 {MINUS_SIGN_}              { return MINUS_SIGN_; }
 {TIMES_SIGN_}              { return TIMES_SIGN_; }
 {DIVIDE_SIGN_}             { return DIVIDE_SIGN_; }
 {POWER_SIGN_}              { return POWER_SIGN_; }
 {OPAREN_SIGN_}             { return OPAREN_SIGN_; }
 {CPAREN_SIGN_}             { return CPAREN_SIGN_; }
 {GREATER_SIGN_}            { return GREATER_SIGN_; }
 {LESS_SIGN_}               { return LESS_SIGN_; }
 {GREATER_EQUAL_SIGN_}      { return GREATER_EQUAL_SIGN_; }
 {LESS_EQUAL_SIGN_}         { return LESS_EQUAL_SIGN_; }
 {COLON_SIGN_}              { return COLON_SIGN_; }
 {DEFINE}                   { return DEFINE; }
 {GUARDIAN_FILE}            { return GUARDIAN_FILE; }
 {INTEGER_}                 { return INTEGER_; }
 {NUMBER_}                  { return NUMBER_; }
 {COBOLWORD}                { return cobolWordToken(); }
 {DOT}                      { return DOT; }
 {CRLF}                     { state(YYINITIAL); return WHITE_SPACE; }
 {WHITE_SPACE}              { return WHITE_SPACE;  }
}

// special states for complicated constructs

//For the special REPLACE statement that cannot be handled by GrammarKit (preprocessor)
<REPLACE_STATE>
{
  {DOUBLE_EQUAL} { state(PSEUDOTEXT_STATE); }
  {DOT}          { normalCode(); return REPLACE_PREPROCESSOR;}
  {CRLF}         { state(YYINITIAL); }
  .              { }
}
//For the special COPY statement that cannot be handled by GrammarKit (preprocessor)
<COPY_STATE>
{
  \"             { state(STRING_STATE); }
  {DOUBLE_EQUAL} { state(PSEUDOTEXT_STATE);  }
  {DOT}          { normalCode(); return COPY_PREPROCESSOR;}
  {CRLF}         { state(YYINITIAL); }
  .              { }
}


//For the comment entries in the procedure division
<PRECOMMENT>
{
 {DOT}         { state(COMMENT_ENTRY_STATE); return DOT; }
 {WHITE_SPACE} { return WHITE_SPACE;  }
}

<COMMENT_ENTRY_STATE>
{
 {COMMENT_ENTRY} { return COMMENT_ENTRY; }
 {CRLF}          { state(YYINITIAL); return WHITE_SPACE; }
}

// for source-computer
<PRECOMPUTERNAME>
{
 {DOT}         { state(COMPUTER_NAME_STATE); return DOT; }
 {WHITE_SPACE} { return WHITE_SPACE; }
}

<COMPUTER_NAME_STATE>
{
 {DOT}       { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "WITH"      { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "DEBUGGING" { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 .           { }
}
<POSTCOMPUTERNAME>
{
 {DOT}       { state(YYINITIAL); return DOT; }
 "WITH"      { state(YYINITIAL); return cobolWordToken(); }
 "DEBUGGING" { state(YYINITIAL); return cobolWordToken(); }
}

//for object-computer
<PREOBJECTCOMPUTERNAME>
{
 {DOT}       { state(OBJECT_COMPUTER_NAME_STATE); return DOT; }
 {EXT_SPACE} { return WHITE_SPACE; }
}

<OBJECT_COMPUTER_NAME_STATE>
{
 {DOT}           { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "MEMORY-SIZE"   { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "PROGRAM"       { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "PROGRAM"       { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "SEQUENCE"      { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "SEGMENT-LIMIT" { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 "CHARACTER-SET" { state(POSTCOMPUTERNAME); rewind(); return COMPUTER_NAME; }
 .               { }
}

<POSTOBJECTCOMPUTERNAME>
{
 {DOT}           { state(YYINITIAL); return DOT; }
 "MEMORY-SIZE"   { state(YYINITIAL); return cobolWordToken(); }
 "PROGRAM"       { state(YYINITIAL); return cobolWordToken(); }
 "PROGRAM"       { state(YYINITIAL); return cobolWordToken(); }
 "SEQUENCE"      { state(YYINITIAL); return cobolWordToken(); }
 "SEGMENT-LIMIT" { state(YYINITIAL); return cobolWordToken(); }
 "CHARACTER-SET" { state(YYINITIAL); return cobolWordToken(); }
}

//for picture strings
<PREPICTURESTRING>
{
 {EXT_SPACE} { return WHITE_SPACE; }
 .           { state(PICTURE_STRING_STATE); rewind(); }
}

<PICTURE_STRING_STATE>
{
  {EXT_SPACE_CRLF}       { state(CODE_STATE); rewind(); return PICTURE_STRING; }
  {DOT}/{EXT_SPACE_CRLF} { state(CODE_STATE); rewind(); return PICTURE_STRING; }
  .                      { }
}

// catchers

.      { return BAD_CHARACTER; }
{CRLF} { return BAD_CHARACTER; }
