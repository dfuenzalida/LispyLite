package lispy;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EvalTest {
	
	Parser parser;
	Environment env;

	@Before
	public void setUp() throws Exception {
		parser = new Parser();
		env = Environment.getGlobalEnvironment();
	}
	
	@Test
	public void testParserTokenizer() {
		String program = "(+ 1 2.0)"; 
		List parsed = (List) parser.parse(program);
		assertEquals("correct length", 3, parsed.size());
		assertEquals("parse Symbols", Symbol.class, parsed.get(0).getClass());
		assertEquals("parse Integers", Integer.class, parsed.get(1).getClass());
		assertEquals("parse Floats", Float.class, parsed.get(2).getClass());
	}
	
	@Test
	public void testParser() {
		String program = "(+ 1 (+ 2 3))";
		List parsed = (List) parser.parse(program);
		assertEquals("correct length", 3, parsed.size());
		assertEquals("symbol in function position", Symbol.class, parsed.get(0).getClass());
		assertEquals("2nd arg is a list", true, parsed.get(2) instanceof List);
		assertEquals("symbol in nested list", Symbol.class, ((List)parsed.get(2)).get(0).getClass());
	}

	@Test
	public void testBuiltins() {
		assertEquals("+ int int...", new Integer(10), Eval.eval("(+ 1 2 3 4)"));
		assertEquals("- int int...", new Integer(1), Eval.eval("(- 10 4 3 2)"));
		assertEquals("+ float int", new Float(3.5f), Eval.eval("(+ 1.5 2)"));

		assertEquals("> int int", new Integer(10), Eval.eval("(> 10 8)"));
		assertEquals("> float int", new Float(2.0f), Eval.eval("(> 2.0 1)"));
		assertEquals("> int float", new Integer(36), Eval.eval("(> 36 12.0)"));
		assertEquals("> int int (false)", Environment.FALSE, Eval.eval("(> 12 36)"));
		assertEquals("<= int float", new Integer(12), Eval.eval("(<= 12 36.0)"));
		assertEquals("<= int int", new Integer(12), Eval.eval("(<= 12 12)"));
		assertEquals("<= int float", new Integer(12), Eval.eval("(<= 12 12.0)"));

		assertEquals("< int int", new Integer(8), Eval.eval("(< 8 10)"));
		assertEquals("< float int", new Integer(1), Eval.eval("(< 1 2.0)"));
		assertEquals("< int float", new Integer(12), Eval.eval("(< 12 36)"));		
		assertEquals("< int int (false)", Environment.FALSE, Eval.eval("(< 36 12)"));
		assertEquals(">= float int", new Float(36.0), Eval.eval("(>= 36.0 12)"));
		assertEquals(">= float int", new Float(36.0), Eval.eval("(>= 36.0 36)"));

		assertEquals("+ of sexps", new Float(15.0), Eval.eval("(+ (- 20 10) (+ 2.5 2.5))"));
		
		assertEquals("list", Eval.eval("(list 1 2 (+ 1 2) (list 4 5 6))"), Eval.eval("(list 1 2 3 (list 4 5 6)))"));
		assertEquals("cons'ing to create list", Eval.eval("(list 1 2 3)"), Eval.eval("(cons 1 (cons 2 (cons 3 (list))))"));
		assertEquals("cons", Eval.eval("(list 1 2 3))"), Eval.eval("(cons 1 (list 2 3)))"));
		
		assertEquals("apply", new Integer(3), Eval.eval("(apply + (list 1 2))"));
	}
	
	@Test
	public void testDefineAndEval() {
		Eval.eval("(define add +)", env);
		Eval.eval("(define square (lambda (x) (* x x)))", env);
		Object parsed = parser.parse("(apply add (list 1 2))");
		assertEquals("define and apply", new Integer(3), Eval.eval(parsed, env));
		assertEquals("define and apply a lambda", new Integer(9), Eval.eval(parser.parse("(square 3)"), env));
	}

	@Test
	public void testRecursiveFunctions() {
		Eval.eval("(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))", env);
		assertEquals("recursive fib", new Integer(1), Eval.eval("(fib 1)", env));
	}

}
