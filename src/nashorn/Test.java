package nashorn;

import lispy.Compiler;
import lispy.Environment;
import lispy.Eval;
import lispy.Parser;

public class Test {

	public static void main(String[] args) throws Exception {
		// For tests
		/*
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		System.out.println(engine.eval("var foo = {}"));
		System.out.println(engine.eval("foo.bar = {quuz: 41}"));
		System.out.println(engine.eval("foo.bar.quuz"));
		System.out.println(engine.eval("var adder = function(a, b){return (a + b)}"));
		System.out.println(engine.eval("adder(1, foo.bar.quuz)"));
		System.out.println(engine.eval("99"));
		*/

		// TODO move these into proper Nashorn-based tests
		/*
		System.out.println(((Procedure)Eval.eval("(lambda (x) (+ 1 x))")).emit());
		System.out.println(Eval.emit("(lambda (x) (+ 1 x))"));
		System.out.println(Eval.emit("(lambda (x) (+ (+ 0 1) x))"));
		System.out.println(Eval.emit("((lambda (x) (+ 1 x)) 41)"));
		System.out.println(Eval.emit("(((lambda (a) (lambda (b) (+ a b))) 1) 41)"));

		String incFn = "(define inc (lambda (x) (+ 1 x)))";
		Environment incEnv = (Environment) Eval.eval(incFn);
		String inc41Js = Eval.emit(new Parser().parse("(inc 41)"), incEnv);
		System.out.println(Eval.emit(incFn));
		System.out.println(inc41Js);
		System.out.println(Eval.emit(new Parser().parse("(define inc' inc)"), incEnv));

		System.out.println(Eval.emit("(if 1 2 3)"));
		*/

		// Prelude
		System.out.println("/* Prelude */");
		System.out.println(Compiler.prelude(Compiler.jsNamespace));

		// Fibonacci
		System.out.println("/* Fibonacci */");
		System.out.println(Eval.emit("(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))"));

		// Adder
		String adderFn = "(define adder (lambda (a) (lambda (b) (+ a b))))";
		Environment adderEnv;
		adderEnv = (Environment) Eval.eval(adderFn);
		String plus2Fn = "(define plus2 (adder 2))";
		adderEnv = (Environment) Eval.eval(plus2Fn, adderEnv);

		System.out.println("/* Adder */");
		System.out.println(Eval.emit(adderFn));
		System.out.println(Eval.emit(plus2Fn, adderEnv));
		System.out.println(Eval.emit("(plus2 3)", adderEnv));

		// Square roots
		Environment sqrtEnv = Environment.getGlobalEnvironment();
		String[] sqrtProgram = new String[]{
			"(define abs (lambda (x) (if (< x 0) (* -1 x) x)))",
			"(define square (lambda (x) (* x x)))",
			"(define good-enough? (lambda (guess x) (< (abs (- (square guess) x)) 0.001)))",
			"(define average (lambda (x y) (/ (+ x y) 2)))",
			"(define improve (lambda (guess x) (average guess (/ x guess))))",
			"(define sqrt-iter (lambda (guess x) (if (good-enough? guess x) guess (sqrt-iter (improve guess x) x))))",
			"(define sqrt (lambda (x) (sqrt-iter 1.0 x)))"
			};
		System.out.println("/* Square roots */");
		for (String line: sqrtProgram) {
			System.out.println(Eval.emit(line, sqrtEnv));
		}
		System.out.println(Eval.emit("(display (sqrt 256))", sqrtEnv));
	}

}
