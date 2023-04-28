package lispy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;


public class CompilerTest {

	Parser parser;
	Environment env;
	ScriptEngine engine;

	@Before
	public void setUp() throws Exception {
		parser = new Parser();
		env = Environment.getGlobalEnvironment();
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		Compiler.jsNamespace = "lispy";
		engine.eval(Compiler.prelude(Compiler.jsNamespace));
	}

	@Test
	public void testDefine() throws Exception {
		engine.eval(Compiler.emit("(define foo 42)", env));
		assertEquals("define", engine.eval(Compiler.emit("foo", env)), 42);
		assertEquals("define", engine.eval("lispy['foo']"), 42);
	}

	@Test
	public void testAdder() throws Exception {
		engine.eval(Compiler.emit("(define adder (lambda (a) (lambda (b) (+ a b))))", env));
		engine.eval(Compiler.emit("(define plus2 (adder 2))", env));
		assertEquals("plus2", engine.eval(Compiler.emit("(plus2 40)", env)), 42.0);
	}

	@Test
	public void testRecursive() throws Exception {
		engine.eval(Compiler.emit("(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))", env));
		assertEquals("recursive", engine.eval(Compiler.emit("(fib 10)", env)), 89.0);
	}

	@Test
	public void testSquareRoots() throws Exception {
		String[] sqrtProgram = new String[]{
			"(define abs (lambda (x) (if (< x 0) (* -1 x) x)))",
			"(define square (lambda (x) (* x x)))",
			"(define good-enough? (lambda (guess x) (< (abs (- (square guess) x)) 0.001)))",
			"(define average (lambda (x y) (/ (+ x y) 2)))",
			"(define improve (lambda (guess x) (average guess (/ x guess))))",
			"(define sqrt-iter (lambda (guess x) (if (good-enough? guess x) guess (sqrt-iter (improve guess x) x))))",
			"(define sqrt (lambda (x) (sqrt-iter 1.0 x)))"
			};
		for (String line: sqrtProgram) {
			engine.eval(Compiler.emit(line, env));
		}
		assertTrue("square roots", Math.abs(16 - (Double) engine.eval(Compiler.emit("(sqrt 256)", env))) < 0.001 );
	}

}
