//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 "domain.y"
	package br.pucrs.smart.validator;

	import java.util.ArrayList;
	import java.util.List;
	import java.io.*;
  /*import java.io.IOException;*/
//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short DEFINE=257;
public final static short DOMAIN=258;
public final static short REQUIREMENTS=259;
public final static short TYPES=260;
public final static short PREDICATES=261;
public final static short CONSTRAINTS=262;
public final static short ACTION=263;
public final static short PARAMETERS=264;
public final static short PRECONDITION=265;
public final static short EFFECT=266;
public final static short PROBLEM=267;
public final static short PDOMAIN=268;
public final static short INIT=269;
public final static short OBJECTS=270;
public final static short GOAL=271;
public final static short AND=272;
public final static short NOT=273;
public final static short FORALL=274;
public final static short EXISTS=275;
public final static short REQ=276;
public final static short OBJ=277;
public final static short STRING=278;
public final static short CSTAT=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    1,    1,    1,    1,    1,
    1,   30,   31,   31,   31,   31,   31,   32,   32,   32,
   33,   33,   33,   33,    2,    2,    3,    3,    4,    4,
    5,    5,    6,    6,    6,    6,    7,    7,   10,   10,
    9,    9,   11,   11,   12,   13,   13,   13,   13,   13,
   13,   14,   14,   15,   15,   16,   16,   34,   34,    8,
    8,   35,   17,   17,   17,   17,   17,   17,   18,   18,
   19,   19,   36,   36,   36,   20,   20,   21,   21,   22,
   22,   22,   22,   23,   23,   23,   23,   24,   24,   24,
   25,   25,   26,   26,   26,   26,   26,   27,   27,   27,
   27,   28,   28,   29,   29,
};
final static short yylen[] = {                            2,
    5,    5,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    4,    5,    5,    5,    5,    4,    3,    4,    4,
    1,    4,    2,    5,    3,    4,    1,    2,    1,    2,
    3,    4,    3,    4,    4,    5,    1,    2,    2,    0,
    3,    4,    1,    2,    4,    5,    5,    5,    5,    5,
    0,    1,    2,    1,    1,    1,    2,    3,    4,    1,
    2,    1,    3,    4,    4,    5,    4,    5,    1,    2,
    3,    4,    8,    8,    4,    1,    2,    3,    4,    2,
    5,    5,    0,    1,    4,    2,    5,    3,    4,    4,
    1,    2,    3,    4,    6,    7,    4,    1,    4,    2,
    5,    3,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,   10,   11,    5,    6,    7,
    8,    4,    9,    0,    0,   39,    0,    0,    0,   41,
    0,    0,    0,    0,    0,    0,    0,    0,   44,   42,
    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    2,   45,   12,   55,   54,    0,    0,    0,    0,
    0,    0,    0,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   53,   61,   57,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   30,    0,    0,    0,    0,   17,   46,
    0,   47,   48,    0,    0,    0,    0,   49,    0,    0,
   80,    0,   50,   13,    0,    0,    0,   14,    0,   15,
   16,    0,    0,    0,   18,    0,    0,    0,   59,    0,
    0,   70,   64,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   75,   34,   38,    0,    0,   32,    0,
    0,   23,   20,   28,   19,    0,   66,   68,    0,    0,
   77,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   88,    0,    0,    0,    0,   36,    0,    0,   25,    0,
    0,   72,    0,    0,    0,    0,   82,   86,   81,   92,
   90,   89,    0,    0,    0,    0,    0,   93,    0,    0,
   26,    0,   79,    0,    0,    0,    0,    0,   97,  100,
    0,  105,   94,   24,   73,   74,    0,    0,    0,  102,
    0,    0,    0,   87,    0,  103,   95,    0,  101,   96,
};
final static short yydgoto[] = {                          2,
  116,  113,  117,   84,   63,   60,  107,   50,    3,    4,
   22,   18,   26,   47,   48,   51,   54,  122,   97,  151,
  128,   77,  155,  156,  160,  134,  184,  185,  189,   19,
   28,   65,  114,   52,   55,   57,
};
final static short yysindex[] = {                       -13,
 -208,    0,    3,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,  -41, -190,    0, -246,   20,   21,    0,
 -190,  -10, -190, -190, -115,   14, -118,   22,    0,    0,
   31,   34, -244, -190,   29, -190,    0, -190,   36, -190,
   40,    0,    0,    0,    0,    0,   37, -244, -190,   17,
   45,   46, -190,    0,   50, -235,   52,   59, -190,   64,
   28,   65,   72, -177,   77,   20,    0,    0,    0, -190,
   20,   20,  -38,   20,   79,   80, -137,   20,   21,  -31,
   21, -190, -190,    0,   21,   21,   85,  -18,    0,    0,
 -190,    0,    0,   -9,   29,   89,   95,    0, -185,  -60,
    0,   99,    0,    0,   36, -190,  101,    0, -190,    0,
    0, -164,   85,  108,    0, -190,  113, -190,    0, -122,
 -190,    0,    0,   29,   29,   -3,  115,  116,  118,  119,
 -117,  -32, -151,    0,    0,    0,   36,  117,    0,  121,
   -8,    0,    0,    0,    0, -114,    0,    0, -113, -190,
    0, -100, -100,   38,  125,  118,   51,  126, -117,  127,
    0,  128,  130,  131,  -26,    0, -190,  132,    0,  133,
  134,    0, -105,  -91,  -90,  119,    0,    0,    0,    0,
    0,    0, -141,  136,  130, -190,  -96,    0,  137,   85,
    0,  135,    0,   99,   99,  141,  143,  -25,    0,    0,
  -24,    0,    0,    0,    0,    0,  118, -190,  144,    0,
  145,  146,  147,    0,  130,    0,    0,  148,    0,    0,
};
final static short yyrindex[] = {                       184,
    0,    0,  184,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  149,    0,    0,
   31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  150,  -21,    0,
    0,    0,    0,    0,    0,  -74,    0,    0,    0,    0,
  152,    0,    0,    0,    0,  149,    0,    0,    0,    0,
  149,  149,    0,  149,    0,    0,    0,  149,    0,    0,
    0,    0,  152,    0,    0,    0,    5,    0,    0,    0,
  153,    0,    0,  154,  155,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  156,   14,    0,    0,  158,    0,
    0,    0,  159,    0,    0,  160,    0,  157,    0,  154,
    0,    0,    0,  162,  163,  164,    0,    0,  -19,  -15,
    0,    0,    0,    0,    0,    0,  165,    0,    0,   15,
    0,    0,    0,    0,    0,   45,    0,    0,  164,    0,
    0,  -74,  -74,    0,    0,  167,    0,    0,  168,    0,
    0,    0,  -19,  -15,    0,    0,    0,    0,    0,    0,
    0,    0,   52,    0,    0,  -15,    0,    0,    0,    0,
    0,    0,    0,    0,  169,    0,  170,    0,    0,  175,
    0,    0,    0,    0,    0,    0,  -15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,    0,  179,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  313,   81,  -97,  182,  114,  -98,  122,  -44,    0,  221,
  204,    0,   69,  181,    0,  186,  -87,  173,   84,  176,
   96,  -99, -150,  -72, -119, -160, -174,   88, -173,    0,
  -34,    0, -111,  185,    0,    0,
};
final static int YYTABLESIZE=521;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
  131,  142,   95,  101,   68,  178,  135,  123,  161,  105,
  200,   23,  162,  202,  188,  210,  212,   45,  144,   56,
   24,    5,  115,   60,  211,    6,    1,  213,   75,   76,
   30,   46,  169,  205,  206,  121,  147,  148,  166,  180,
  219,  150,   15,  170,  104,    5,  108,   17,    5,    6,
  110,  111,  174,  175,   37,    6,  214,  158,    7,   25,
   27,   70,   42,    8,    9,   10,   11,    6,   53,   12,
   13,   43,   82,   68,   44,   59,    7,   66,  204,   64,
    6,    8,    9,   10,   11,   71,   72,   12,   13,    7,
   74,  126,   78,    6,   87,    9,   10,   11,  131,   79,
   12,   13,    7,  196,   81,   85,    6,    8,  140,   10,
   11,  131,   86,   12,   13,    7,    6,   89,   99,  100,
  163,  164,   10,   11,  112,    7,   12,   13,  102,  124,
    8,  197,   10,   11,   90,  125,   12,   13,  133,   92,
   93,  137,   98,   33,   34,   35,  103,   36,  143,   38,
   39,   40,   41,  145,  120,  152,  153,  154,  157,  159,
  167,   82,  171,  149,   76,  177,  179,  181,  182,  183,
  186,  192,  190,  191,  194,  195,  199,  203,  121,  150,
  187,  207,  208,   40,  215,  216,  217,  218,  220,   51,
   52,   83,   29,   58,   69,   63,   33,    6,   31,   21,
   27,   60,   65,   67,   76,   35,    7,   84,   91,   98,
  104,  129,  130,   10,   11,   22,    6,   12,   13,   99,
  168,   62,  139,   16,   29,    7,    6,  136,   67,  172,
    8,    9,   10,   11,   69,    7,   12,   13,   94,    6,
    8,    9,   10,   11,  159,   96,   12,   13,    7,    6,
  187,  187,  187,    8,    9,   10,   11,    5,    7,   12,
   13,    6,    5,    8,    9,   10,   11,  120,  193,   12,
   13,    5,    6,  149,  127,  119,    5,    5,    5,    5,
    0,    6,    5,    5,  209,    6,    6,    6,    6,    6,
    0,    0,    6,    6,    7,    6,    0,    0,    0,    8,
    9,   10,   11,    0,    7,   12,   13,    0,    6,    8,
  176,   10,   11,   14,    0,   12,   13,    7,    0,    0,
    0,    0,    8,    9,   10,   11,   21,   14,   12,   13,
    0,    0,    0,   21,    0,   31,   32,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   49,    0,   56,    0,
   58,    0,   61,    0,    0,    0,    0,    0,    0,    0,
    0,   49,    0,    0,    0,   73,    0,    0,    0,    0,
    0,   80,    0,   83,    0,    0,   88,    0,    0,    0,
    0,    0,   91,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  106,    0,  109,   83,    0,    0,    0,    0,
    0,    0,    0,  118,    0,    0,    0,    0,    0,    0,
    0,    0,  132,    0,    0,    0,    0,    0,  106,    0,
    0,  138,    0,    0,  141,    0,    0,    0,    0,    0,
  118,    0,    0,  146,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  165,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  173,    0,    0,    0,  132,    0,    0,  132,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  141,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  198,    0,    0,  201,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  198,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   61,  113,   41,   76,   49,  156,  105,   95,   41,   41,
  185,  258,  132,  187,   41,   41,   41,  262,  116,   41,
  267,   41,   41,   45,  198,   41,   40,  201,  264,  265,
   41,  276,   41,  194,  195,   45,  124,  125,  137,  159,
  215,   45,   40,  141,   79,   41,   81,   40,  257,  258,
   85,   86,  152,  153,   41,   41,  207,  130,  267,   40,
   40,   45,   41,  272,  273,  274,  275,  258,   40,  278,
  279,   41,   45,  118,   41,   40,  267,   41,  190,   40,
  258,  272,  273,  274,  275,   41,   41,  278,  279,  267,
   41,  277,   41,  258,  272,  273,  274,  275,   61,   41,
  278,  279,  267,  176,   41,   41,  258,  272,  273,  274,
  275,   61,   41,  278,  279,  267,  258,   41,   40,   40,
  272,  273,  274,  275,   40,  267,  278,  279,  266,   41,
  272,  273,  274,  275,   66,   41,  278,  279,   40,   71,
   72,   41,   74,  259,  260,  261,   78,  263,   41,  268,
  269,  270,  271,   41,  277,   41,   41,   40,   40,  277,
   40,   45,  277,  277,  265,   41,   41,   41,   41,   40,
   40,  277,   41,   41,  266,  266,   41,   41,   45,   45,
  277,   41,   40,    0,   41,   41,   41,   41,   41,   41,
   41,  266,   41,   41,   41,   41,   41,  258,   41,   41,
   41,   45,   41,   41,   41,   41,  267,   41,   41,   41,
   41,  272,  273,  274,  275,   41,  258,  278,  279,   41,
  140,   40,  109,    3,   21,  267,  258,  106,   48,  146,
  272,  273,  274,  275,   49,  267,  278,  279,  277,  258,
  272,  273,  274,  275,  277,   73,  278,  279,  267,  258,
  277,  277,  277,  272,  273,  274,  275,  277,  267,  278,
  279,  277,  258,  272,  273,  274,  275,  277,  173,  278,
  279,  267,  258,  277,   99,   91,  272,  273,  274,  275,
   -1,  267,  278,  279,  197,  258,  272,  273,  274,  275,
   -1,   -1,  278,  279,  267,  258,   -1,   -1,   -1,  272,
  273,  274,  275,   -1,  267,  278,  279,   -1,  258,  272,
  273,  274,  275,    1,   -1,  278,  279,  267,   -1,   -1,
   -1,   -1,  272,  273,  274,  275,   14,   15,  278,  279,
   -1,   -1,   -1,   21,   -1,   23,   24,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   34,   -1,   36,   -1,
   38,   -1,   40,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   49,   -1,   -1,   -1,   53,   -1,   -1,   -1,   -1,
   -1,   59,   -1,   61,   -1,   -1,   64,   -1,   -1,   -1,
   -1,   -1,   70,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   80,   -1,   82,   83,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  100,   -1,   -1,   -1,   -1,   -1,  106,   -1,
   -1,  109,   -1,   -1,  112,   -1,   -1,   -1,   -1,   -1,
  118,   -1,   -1,  121,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  133,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  150,   -1,   -1,   -1,  154,   -1,   -1,  157,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  167,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  183,   -1,   -1,  186,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  208,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,null,
"'-'",null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"DEFINE","DOMAIN","REQUIREMENTS","TYPES",
"PREDICATES","CONSTRAINTS","ACTION","PARAMETERS","PRECONDITION","EFFECT",
"PROBLEM","PDOMAIN","INIT","OBJECTS","GOAL","AND","NOT","FORALL","EXISTS","REQ",
"OBJ","STRING","CSTAT",
};
final static String yyrule[] = {
"$accept : pddl",
"pddl : '(' DEFINE domN domain ')'",
"pddl : '(' DEFINE probN problem ')'",
"pddl : plans",
"string : STRING",
"string : AND",
"string : NOT",
"string : FORALL",
"string : EXISTS",
"string : CSTAT",
"string : DOMAIN",
"string : PROBLEM",
"probN : '(' PROBLEM string ')'",
"problem : '(' PDOMAIN string ')' problem",
"problem : '(' INIT pPreds ')' problem",
"problem : '(' OBJECTS objs ')' problem",
"problem : '(' OBJECTS tObjs ')' problem",
"problem : '(' GOAL goal ')'",
"goal : '(' string ')'",
"goal : '(' string gObj ')'",
"goal : '(' AND mGoals ')'",
"mGoals : mGoal",
"mGoals : '(' NOT mGoal ')'",
"mGoals : mGoal mGoals",
"mGoals : '(' NOT mGoal ')' mGoals",
"mGoal : '(' string ')'",
"mGoal : '(' string gObj ')'",
"gObj : string",
"gObj : string gObj",
"objs : string",
"objs : string objs",
"tObjs : string '-' string",
"tObjs : string '-' string tObjs",
"pPreds : '(' string ')'",
"pPreds : '(' string ')' pPreds",
"pPreds : '(' string pPredO ')'",
"pPreds : '(' string pPredO ')' pPreds",
"pPredO : string",
"pPredO : string pPredO",
"plans : plan plans",
"plans :",
"plan : '(' string ')'",
"plan : '(' string planO ')'",
"planO : string",
"planO : string planO",
"domN : '(' DOMAIN string ')'",
"domain : '(' REQUIREMENTS reqs ')' domain",
"domain : '(' TYPES types ')' domain",
"domain : '(' TYPES sTypes ')' domain",
"domain : '(' PREDICATES dPred ')' domain",
"domain : '(' ACTION action ')' domain",
"domain :",
"reqs : req",
"reqs : req reqs",
"req : REQ",
"req : CONSTRAINTS",
"types : string",
"types : string types",
"sTypes : strings '-' string",
"sTypes : strings '-' string sTypes",
"strings : string",
"strings : string strings",
"dPred : preds",
"preds : '(' string ')'",
"preds : '(' string ')' preds",
"preds : '(' string predO ')'",
"preds : '(' string predO ')' preds",
"preds : '(' string predOT ')'",
"preds : '(' string predOT ')' preds",
"predO : OBJ",
"predO : OBJ predO",
"predOT : OBJ '-' string",
"predOT : OBJ '-' string predOT",
"action : string PARAMETERS '(' params ')' prec EFFECT eff",
"action : string PARAMETERS '(' paramsT ')' prec EFFECT eff",
"action : string prec EFFECT eff",
"params : OBJ",
"params : OBJ params",
"paramsT : OBJ '-' string",
"paramsT : OBJ '-' string paramsT",
"prec : PRECONDITION precs",
"prec : PRECONDITION '(' NOT precs ')'",
"prec : PRECONDITION '(' AND mPrecs ')'",
"prec :",
"mPrecs : precs",
"mPrecs : '(' NOT precs ')'",
"mPrecs : precs mPrecs",
"mPrecs : '(' NOT precs ')' mPrecs",
"precs : '(' string ')'",
"precs : '(' string preObj ')'",
"precs : '(' '=' preObj ')'",
"preObj : OBJ",
"preObj : OBJ preObj",
"eff : '(' string ')'",
"eff : '(' string effObj ')'",
"eff : '(' NOT '(' string ')' ')'",
"eff : '(' NOT '(' string effObj ')' ')'",
"eff : '(' AND mEffs ')'",
"mEffs : mEff",
"mEffs : '(' NOT mEff ')'",
"mEffs : mEff mEffs",
"mEffs : '(' NOT mEff ')' mEffs",
"mEff : '(' string ')'",
"mEff : '(' string effObj ')'",
"effObj : OBJ",
"effObj : OBJ effObj",
};

//#line 222 "domain.y"

	private Yylex lexer;
	private int yylex () {
		int yyl_return = -1;
		try {
		  yylval = new ParserVal(0);
		  yyl_return = lexer.yylex();
		}
		catch (IOException e) {
		  System.err.println("IO error :"+e);
		}
		return yyl_return;
	}
	public Parser(Reader r) {
		lexer = new Yylex(r, this);
	}
	public void yyerror (String error) {
		System.err.println ("Error: " + error + "\nin file " + file + " line " + String.valueOf(Yylex.lineCount));
	}
	
	
	private static PDDL pddl; 
	private static String file;
	public static PDDL parseDomain(String filename){
		file = filename;
		try {
			Yylex.reset();
			Parser yyparser = new Parser(new FileReader(filename));
			yyparser.yyparse();
		}
		catch (IOException e) {
		  System.err.println("IO error :"+e);
		}
		pddl.domainFile = filename;
		return pddl;
	}
	public static void parseProblem(PDDL domain, String filename, String problem){
		file = filename;
		pddl = domain; 
		Yylex.reset();
		Parser yyparser = new Parser(new StringReader(problem));
		yyparser.yyparse();
		pddl.problemFile = filename;
	}
	public static void parsePlan(PDDL domain, String filename, String plan){
		file = filename;
		pddl = domain; 
		Yylex.reset();
		Parser yyparser = new Parser(new StringReader(plan));
		yyparser.yyparse();
		pddl.fixPlanCase();
		pddl.planFile = filename;
	}
	

	public static void main(String args[]) throws IOException {
		pddl = parseDomain("src\\resources\\domain.pddl");
		pddl.printDomain();
		
		parseProblem(pddl, "problem", args[0]);
		parsePlan(pddl, "plan", args[1]);
		if(!pddl.checkA()){
			System.out.println("__________________________");
			pddl.planTest();
			pddl.resetState();
			pddl.valOut("out");
		}
	}
//#line 557 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 27 "domain.y"
{}
break;
case 2:
//#line 28 "domain.y"
{}
break;
case 3:
//#line 29 "domain.y"
{}
break;
case 4:
//#line 32 "domain.y"
{yyval.obj = val_peek(0).sval;}
break;
case 5:
//#line 33 "domain.y"
{yyval.obj = "and";}
break;
case 6:
//#line 34 "domain.y"
{yyval.obj = "not";}
break;
case 7:
//#line 35 "domain.y"
{yyval.obj = "forall";}
break;
case 8:
//#line 36 "domain.y"
{yyval.obj = "exists";}
break;
case 9:
//#line 37 "domain.y"
{yyval.obj = val_peek(0).sval;}
break;
case 10:
//#line 38 "domain.y"
{yyval.obj = "domain";}
break;
case 11:
//#line 39 "domain.y"
{yyval.obj = "problem";}
break;
case 12:
//#line 46 "domain.y"
{pddl.problem((String)val_peek(1).obj);}
break;
case 13:
//#line 49 "domain.y"
{pddl.pDomain((String)val_peek(2).obj);}
break;
case 14:
//#line 50 "domain.y"
{pddl.iniState((ArrayList<String[]>)val_peek(2).obj);}
break;
case 15:
//#line 51 "domain.y"
{pddl.addObjs((List<String>)val_peek(2).obj);}
break;
case 16:
//#line 52 "domain.y"
{pddl.addObjs((List)((Object[])val_peek(2).obj)[0], (List)((Object[])val_peek(2).obj)[1]);}
break;
case 17:
//#line 53 "domain.y"
{}
break;
case 18:
//#line 57 "domain.y"
{String[] aux = new String[1]; aux[0] = (String)val_peek(1).obj; pddl.addPosGoal(aux);}
break;
case 19:
//#line 58 "domain.y"
{pddl.addPosGoal(((String)val_peek(2).obj + " " + ((String)val_peek(1).obj)).split(" "));}
break;
case 20:
//#line 59 "domain.y"
{}
break;
case 21:
//#line 61 "domain.y"
{pddl.addPosGoal((String[])val_peek(0).obj);}
break;
case 22:
//#line 62 "domain.y"
{pddl.addNegGoal((String[])val_peek(1).obj);}
break;
case 23:
//#line 63 "domain.y"
{pddl.addPosGoal((String[])val_peek(1).obj);}
break;
case 24:
//#line 64 "domain.y"
{pddl.addNegGoal((String[])val_peek(2).obj);}
break;
case 25:
//#line 66 "domain.y"
{yyval.obj = new String[1]; ((String[])yyval.obj)[0] = (String)val_peek(1).obj;}
break;
case 26:
//#line 67 "domain.y"
{yyval.obj = ((String)val_peek(2).obj + " " + ((String)val_peek(1).obj)).split(" ");}
break;
case 27:
//#line 69 "domain.y"
{yyval.obj = val_peek(0).obj;}
break;
case 28:
//#line 70 "domain.y"
{yyval.obj = val_peek(1).obj + " " +((String)val_peek(0).obj);}
break;
case 29:
//#line 74 "domain.y"
{yyval.obj = new ArrayList<String>(); ((List)yyval.obj).add(val_peek(0).obj);}
break;
case 30:
//#line 75 "domain.y"
{yyval.obj = val_peek(0).obj; ((List)yyval.obj).add(val_peek(1).obj);}
break;
case 31:
//#line 78 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String>(); ((Object[])yyval.obj)[1] = new ArrayList<String>(); ((List)((Object[])yyval.obj)[0]).add(val_peek(2).obj); ((List)((Object[])yyval.obj)[1]).add(val_peek(0).obj);}
break;
case 32:
//#line 79 "domain.y"
{yyval.obj = val_peek(0).obj; ((List)((Object[])yyval.obj)[0]).add(val_peek(3).obj); ((List)((Object[])yyval.obj)[1]).add(val_peek(1).obj);}
break;
case 33:
//#line 82 "domain.y"
{yyval.obj = new ArrayList<String[]>(); String[] aux = new String[1]; aux[0] = (String)val_peek(1).obj; ((ArrayList)yyval.obj).add(aux);}
break;
case 34:
//#line 83 "domain.y"
{String[] aux = new String[1]; aux[0] = (String)val_peek(2).obj; yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add(aux);}
break;
case 35:
//#line 84 "domain.y"
{yyval.obj = new ArrayList<String[]>();  ((ArrayList)yyval.obj).add((val_peek(2).obj + " " + (String)val_peek(1).obj).split(" "));}
break;
case 36:
//#line 85 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add((val_peek(3).obj + " " + (String)val_peek(2).obj).split(" "));}
break;
case 37:
//#line 87 "domain.y"
{yyval.obj = val_peek(0).obj;}
break;
case 38:
//#line 88 "domain.y"
{yyval.obj = val_peek(1).obj + " " + (String)val_peek(0).obj;}
break;
case 39:
//#line 94 "domain.y"
{}
break;
case 40:
//#line 95 "domain.y"
{}
break;
case 41:
//#line 97 "domain.y"
{String[] aux = new String[1]; aux[0] = (String)val_peek(1).obj; pddl.addPAct(aux);}
break;
case 42:
//#line 98 "domain.y"
{pddl.addPAct((val_peek(2).obj + " " + val_peek(1).obj).split(" "));}
break;
case 43:
//#line 100 "domain.y"
{yyval.obj = val_peek(0).obj;}
break;
case 44:
//#line 101 "domain.y"
{yyval.obj = val_peek(1).obj + " " + (String)val_peek(0).obj;}
break;
case 45:
//#line 108 "domain.y"
{pddl = new PDDL((String)val_peek(1).obj);}
break;
case 46:
//#line 110 "domain.y"
{}
break;
case 47:
//#line 111 "domain.y"
{}
break;
case 48:
//#line 112 "domain.y"
{}
break;
case 49:
//#line 113 "domain.y"
{}
break;
case 50:
//#line 114 "domain.y"
{}
break;
case 51:
//#line 115 "domain.y"
{}
break;
case 52:
//#line 120 "domain.y"
{pddl.addReq((String)val_peek(0).obj);}
break;
case 53:
//#line 121 "domain.y"
{pddl.addReq((String)val_peek(1).obj);}
break;
case 54:
//#line 123 "domain.y"
{yyval.obj = val_peek(0).sval;}
break;
case 55:
//#line 124 "domain.y"
{yyval.obj = ":constraints";}
break;
case 56:
//#line 129 "domain.y"
{pddl.addType((String)val_peek(0).obj);}
break;
case 57:
//#line 130 "domain.y"
{pddl.addType((String)val_peek(1).obj);}
break;
case 58:
//#line 132 "domain.y"
{pddl.addType(((String)val_peek(2).obj).split(" "), (String)val_peek(0).obj);}
break;
case 59:
//#line 133 "domain.y"
{pddl.addType(((String)val_peek(3).obj).split(" "), (String)val_peek(1).obj);}
break;
case 60:
//#line 135 "domain.y"
{yyval.obj = val_peek(0).obj;}
break;
case 61:
//#line 136 "domain.y"
{yyval.obj = (String)val_peek(1).obj + " " + (String)val_peek(0).obj;}
break;
case 62:
//#line 141 "domain.y"
{pddl.addPredicates((ArrayList<Pred>)val_peek(0).obj);}
break;
case 63:
//#line 143 "domain.y"
{yyval.obj = new ArrayList<Pred>(); ((ArrayList)yyval.obj).add(new Pred((String)val_peek(1).obj));}
break;
case 64:
//#line 144 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add(new Pred((String)val_peek(2).obj));}
break;
case 65:
//#line 145 "domain.y"
{yyval.obj = new ArrayList<Pred>(); ((ArrayList)yyval.obj).add(new Pred((String)val_peek(2).obj, (ArrayList<String>)val_peek(1).obj));}
break;
case 66:
//#line 146 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add(new Pred((String)val_peek(3).obj, (ArrayList<String>)val_peek(2).obj));}
break;
case 67:
//#line 147 "domain.y"
{yyval.obj = new ArrayList<Pred>(); ((ArrayList)yyval.obj).add(new Pred((String)val_peek(2).obj, (ArrayList<String>)((ArrayList[])val_peek(1).obj)[0],(ArrayList<Integer>)((ArrayList[])val_peek(1).obj)[1]));}
break;
case 68:
//#line 148 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add(new Pred((String)val_peek(3).obj, (ArrayList<String>)((ArrayList[])val_peek(2).obj)[0],(ArrayList<Integer>)((ArrayList[])val_peek(2).obj)[1]));}
break;
case 69:
//#line 152 "domain.y"
{yyval.obj = new ArrayList<String>(); ((ArrayList)yyval.obj).add(val_peek(0).sval);}
break;
case 70:
//#line 153 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList<String>)val_peek(0).obj).add(val_peek(1).sval);}
break;
case 71:
//#line 155 "domain.y"
{yyval.obj = new ArrayList[2]; ((ArrayList[])yyval.obj)[0] = new ArrayList<String>(); ((ArrayList[])yyval.obj)[1] = new ArrayList<Integer>(); ((ArrayList[])yyval.obj)[0].add(val_peek(2).sval);
								((ArrayList[])yyval.obj)[1].add(new Integer(pddl.getTypeId((String)val_peek(0).obj)));}
break;
case 72:
//#line 157 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList[])yyval.obj)[0].add(val_peek(3).sval); ((ArrayList[])yyval.obj)[1].add(new Integer(pddl.getTypeId((String)val_peek(1).obj)));}
break;
case 73:
//#line 162 "domain.y"
{pddl.addAction(new Act((String)val_peek(7).obj,(ArrayList<String>)val_peek(4).obj, (ArrayList<String[]>)((Object[])val_peek(2).obj)[0],
																			(ArrayList<String[]>)((Object[])val_peek(2).obj)[1], (ArrayList<String[]>)((Object[])val_peek(0).obj)[0], (ArrayList<String[]>)((Object[])val_peek(0).obj)[1]));}
break;
case 74:
//#line 164 "domain.y"
{pddl.addAction(new Act((String)val_peek(7).obj, (ArrayList<String>)((Object[])val_peek(4).obj)[0], (ArrayList<String>)((Object[])val_peek(4).obj)[1], 
																			(ArrayList<String[]>)((Object[])val_peek(2).obj)[0], (ArrayList<String[]>)((Object[])val_peek(2).obj)[1], (ArrayList<String[]>)((Object[])val_peek(0).obj)[0],
																			(ArrayList<String[]>)((Object[])val_peek(0).obj)[1], pddl.types));}
break;
case 75:
//#line 167 "domain.y"
{pddl.addAction(new Act((String)val_peek(3).obj, new ArrayList<String>(), (ArrayList<String[]>)((Object[])val_peek(2).obj)[0],
																			(ArrayList<String[]>)((Object[])val_peek(2).obj)[1], (ArrayList<String[]>)((Object[])val_peek(0).obj)[0], (ArrayList<String[]>)((Object[])val_peek(0).obj)[1]));}
break;
case 76:
//#line 170 "domain.y"
{yyval.obj = new ArrayList<String>(); ((ArrayList)yyval.obj).add(val_peek(0).sval);}
break;
case 77:
//#line 171 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList)yyval.obj).add(0,val_peek(1).sval);}
break;
case 78:
//#line 173 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String>(); ((Object[])yyval.obj)[1] = new ArrayList<String>(); ((ArrayList)((Object[])yyval.obj)[0]).add(val_peek(2).sval); ((ArrayList)((Object[])yyval.obj)[1]).add(val_peek(0).obj);}
break;
case 79:
//#line 174 "domain.y"
{yyval.obj = val_peek(0).obj	; ((ArrayList)((Object[])yyval.obj)[0]).add(0,val_peek(3).sval); ((ArrayList)((Object[])yyval.obj)[1]).add(0,val_peek(1).obj);}
break;
case 80:
//#line 178 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[0]).add((String[])val_peek(0).obj);}
break;
case 81:
//#line 179 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[1]).add((String[])val_peek(1).obj);}
break;
case 82:
//#line 180 "domain.y"
{yyval.obj = val_peek(1).obj;}
break;
case 83:
//#line 181 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>();}
break;
case 84:
//#line 183 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[0]).add((String[])val_peek(0).obj);}
break;
case 85:
//#line 184 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[1]).add((String[])val_peek(1).obj);}
break;
case 86:
//#line 185 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList<String[]>)((Object[])val_peek(0).obj)[0]).add((String[])val_peek(1).obj);}
break;
case 87:
//#line 186 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList<String[]>)((Object[])val_peek(0).obj)[1]).add((String[])val_peek(2).obj);}
break;
case 88:
//#line 188 "domain.y"
{yyval.obj = new String[1]; ((String[])yyval.obj)[0] = (String)val_peek(1).obj;}
break;
case 89:
//#line 189 "domain.y"
{yyval.obj = (val_peek(2).obj + " " + (String)val_peek(1).obj).split(" ");}
break;
case 90:
//#line 190 "domain.y"
{yyval.obj = ("= " + (String)val_peek(1).obj).split(" ");}
break;
case 91:
//#line 192 "domain.y"
{yyval.obj = val_peek(0).sval;}
break;
case 92:
//#line 193 "domain.y"
{yyval.obj = val_peek(1).sval + " " + ((String)val_peek(0).obj);}
break;
case 93:
//#line 197 "domain.y"
{String[] aux = new String[1]; aux[0] = (String)val_peek(1).obj; yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); 
											((ArrayList<String[]>)((Object[])yyval.obj)[0]).add(aux); ((Object[])yyval.obj)[1] = new ArrayList<String[]>();}
break;
case 94:
//#line 199 "domain.y"
{String[] aux = ((String)val_peek(2).obj + " " + ((String)val_peek(1).obj)).split(" "); yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); 
											((ArrayList<String[]>)((Object[])yyval.obj)[0]).add(aux); ((Object[])yyval.obj)[1] = new ArrayList<String[]>();}
break;
case 95:
//#line 201 "domain.y"
{String[] aux = new String[1]; aux[0] = (String)val_peek(2).obj; yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); 
											((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[1]).add(aux);}
break;
case 96:
//#line 203 "domain.y"
{String[] aux = ((String)val_peek(3).obj + " " + ((String)val_peek(2).obj)).split(" "); yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); 
											((Object[])yyval.obj)[1] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[1]).add(aux);}
break;
case 97:
//#line 205 "domain.y"
{yyval.obj = val_peek(1).obj;}
break;
case 98:
//#line 207 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((ArrayList<String[]>)((Object[])yyval.obj)[0]).add((String[])val_peek(0).obj);
									((Object[])yyval.obj)[1] = new ArrayList<String[]>();}
break;
case 99:
//#line 209 "domain.y"
{yyval.obj = new Object[2]; ((Object[])yyval.obj)[0] = new ArrayList<String[]>(); ((Object[])yyval.obj)[1] = new ArrayList<String[]>();
									((ArrayList<String[]>)((Object[])yyval.obj)[1]).add((String[])val_peek(1).obj);}
break;
case 100:
//#line 211 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList<String[]>)((Object[])val_peek(0).obj)[0]).add((String[])val_peek(1).obj);}
break;
case 101:
//#line 212 "domain.y"
{yyval.obj = val_peek(0).obj; ((ArrayList<String[]>)((Object[])val_peek(0).obj)[1]).add((String[])val_peek(2).obj);}
break;
case 102:
//#line 214 "domain.y"
{yyval.obj = new String[1]; ((String[])yyval.obj)[0] = (String)val_peek(1).obj;}
break;
case 103:
//#line 215 "domain.y"
{yyval.obj = ((String)val_peek(2).obj + " " + ((String)val_peek(1).obj)).split(" ");}
break;
case 104:
//#line 217 "domain.y"
{yyval.obj = val_peek(0).sval;}
break;
case 105:
//#line 218 "domain.y"
{yyval.obj = val_peek(1).sval + " " +((String)val_peek(0).obj);}
break;
//#line 1137 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
