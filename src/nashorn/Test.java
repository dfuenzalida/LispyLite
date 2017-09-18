package nashorn;

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
		
		//System.out.println(((Procedure)Eval.eval("(lambda (x) (+ 1 x))")).emit());
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
	}

}
