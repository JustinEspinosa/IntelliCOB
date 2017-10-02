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
COBOLWORD=[a-zA-Z][a-zA-Z0-9-]*
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
STRING_START=(H|X|N|n)?\"
GUARDIAN_FILE=(\${COBOLWORD}{DOT})?({COBOLWORD}{DOT})?{COBOLWORD}
DEFINE=\={COBOLWORD}

%{


   private int previousState = YYINITIAL;
   private int stateAfterPseudoText = YYINITIAL;
   private int stateAfterString = YYINITIAL;

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

%state COMMENT_STATE CODE_STATE STRING_STATE PSEUDOTEXT_STATE PREPROCESSOR_STATE
%state PRECOMMENT COMMENT_ENTRY_STATE
%state PRECOMPUTERNAME COMPUTER_NAME_STATE POSTCOMPUTERNAME
%state PREOBJECTCOMPUTERNAME PREOBJECTCOMPUTERNAME2 OBJECT_COMPUTER_NAME_STATE
%state PREPICTURESTRING PICTURE_STRING_STATE
//The strange case of COPY and REPLACE: instructions which are preprocessor directives...
%state COPY_STATE1 COPY_STATE2 COPY_STATE3 COPY_STATE4 COPY_STATE5 COPY_STATE6 COPY_STATE7 COPY_STATE8 COPY_STATE9
%state REPLACE_STATE1 REPLACE_STATE2 REPLACE_STATE3 REPLACE_STATE4 REPLACE_STATE5

%%

<YYINITIAL>
{
 {CODE_INDICATOR}         { state(CODE_STATE); return CODE_INDICATOR; }
 {COMMENT_INDICATOR}      { state(COMMENT_STATE); return COMMENT_INDICATOR; }
 {PAGE_INDICATOR}         { state(COMMENT_STATE); return PAGE_INDICATOR; }
 {PREPROCESSOR_INDICATOR} { state(PREPROCESSOR_STATE); return PREPROCESSOR_INDICATOR; }
 {CRLF}                   { return WHITE_SPACE; }
 .                        { return BAD_CHARACTER; }
}

<PREPROCESSOR_STATE>
{
 {PREPROCESSOR}  { return PREPROCESSOR; }
 {CRLF}          { state(YYINITIAL);  return WHITE_SPACE;  }
}

<COMMENT_STATE>
{
 {COMMENT_ENTRY} { return COMMENT; }
 {CRLF}          { state(YYINITIAL);  return WHITE_SPACE;  }
}

<STRING_STATE>
{
 \"          { state(stateAfterString); return STRING; }
 .           { }
}


<CODE_STATE>
{

 {STRING_START}             { state(STRING_STATE); stateAfterString=CODE_STATE;}

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
 "COPY"                     { state(COPY_STATE1); }
 "REPLACE"                  { state(REPLACE_STATE1); }

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
 {COBOLWORD}                { return cobolWordToken(); }
 {DEFINE}                   { return DEFINE; }
 {GUARDIAN_FILE}            { return GUARDIAN_FILE; }
 {INTEGER_}                 { return INTEGER_; }
 {NUMBER_}                  { return NUMBER_; }
 {DOT}                      { return DOT; }
 {CRLF}                     { state(YYINITIAL);  return WHITE_SPACE; }
 {WHITE_SPACE}              { return WHITE_SPACE;  }
}

// special states for complicated constructs

<PSEUDOTEXT_STATE>
{
 {DOUBLE_EQUAL} { state(stateAfterPseudoText); }
 .              { }
}
//For the special REPLACE statement that cannot be handled by GrammarKit (preprocessor)
<REPLACE_STATE1>
{
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=REPLACE_STATE2; }
  {EXT_SPACE_CRLF} {}
  "OFF"         {state(REPLACE_STATE5);}
}
<REPLACE_STATE2>
{
  "BY" {state(REPLACE_STATE3);}
  {EXT_SPACE_CRLF} {}
}
<REPLACE_STATE3>
{
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=REPLACE_STATE4; }
  {EXT_SPACE_CRLF} {}
}
<REPLACE_STATE4>
{
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=REPLACE_STATE1; }
  {DOT} {state(CODE_STATE); return REPLACE_PREPROCESSOR; }
  {EXT_SPACE_CRLF} {}
}
<REPLACE_STATE5>
{
  {DOT} {state(CODE_STATE); return REPLACE_PREPROCESSOR; }
  {EXT_SPACE_CRLF} {}
}

//For the special COPY statement that cannot be handled by GrammarKit (preprocessor)
<COPY_STATE1>
{
  {COBOLWORD} {state(COPY_STATE2);}
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE2>
{
  ("IN"|"OF") {state(COPY_STATE3);}
  "REPLACING" {state(COPY_STATE5);}
  {EXT_SPACE_CRLF} {}
  {DOT} { state(CODE_STATE); return COPY_PREPROCESSOR;}
}
<COPY_STATE3>
{
  \"?{GUARDIAN_FILE}\"? {state(COPY_STATE4);}
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE4>
{
  "REPLACING" {state(COPY_STATE5);}
  {EXT_SPACE_CRLF} {}
  {DOT} { state(CODE_STATE); return COPY_PREPROCESSOR;}
}
<COPY_STATE5>
{
  "OFF" {state(COPY_STATE9);}
  \" {state(STRING_STATE); stateAfterString=COPY_STATE6; }
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=COPY_STATE6; }
  {COBOLWORD}  {state(COPY_STATE6); }
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE6>
{
  "BY" {state(COPY_STATE7);}
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE7>
{
  \" {state(STRING_STATE); stateAfterString=COPY_STATE8; }
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=COPY_STATE8;}
  {COBOLWORD}  {state(COPY_STATE8); }
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE8>
{
  \" {state(STRING_STATE); stateAfterString=COPY_STATE5; }
  {DOUBLE_EQUAL} {state(PSEUDOTEXT_STATE); stateAfterPseudoText=COPY_STATE5; }
  {COBOLWORD}  {state(COPY_STATE5); }
  {DOT} { state(CODE_STATE); return COPY_PREPROCESSOR;}
  {EXT_SPACE_CRLF} {}
}
<COPY_STATE9>
{
  {DOT} { state(CODE_STATE); return COPY_PREPROCESSOR;}
  {EXT_SPACE_CRLF} {}
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
 "WITH"      { state(YYINITIAL); return WITH; }
 "DEBUGGING" { state(YYINITIAL); return DEBUGGING; }
}

//for object-computer
<PREOBJECTCOMPUTERNAME>
{
 {DOT}       { state(PREOBJECTCOMPUTERNAME2); return DOT; }
 {EXT_SPACE} { return WHITE_SPACE; }
}

<PREOBJECTCOMPUTERNAME2>
{
 {EXT_SPACE} { return WHITE_SPACE; }
 .           { state(OBJECT_COMPUTER_NAME_STATE); rewind(); }
}

<OBJECT_COMPUTER_NAME_STATE>
{
 {WHITE_SPACE} { state(CODE_STATE); rewind(); return OBJECT_COMPUTER_NAME; }
 {CRLF}        { state(YYINITIAL); rewind(); return OBJECT_COMPUTER_NAME; }
 .             { }
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
