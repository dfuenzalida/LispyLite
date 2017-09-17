package lispy;


public class Lispy {

	public static void main(String[] args) {
		// TODO Add REPL
		Parser parser = new Parser();
//		String program = "(+ 1 (+ 2 3))";
//		System.out.println(parser.parse(program));
//		parser = new Parser();
//		System.out.println(Eval.eval("(+ 1.5 2)"));
//		System.out.println(Eval.eval("(- 10 4 3 2)"));
		System.out.println(Eval.eval("(+ (- 20 10) (+ 2.5 2.5))"));
		System.out.println(Eval.eval("(* 2 pi)"));
		System.out.println(Eval.eval("(list 1 2 (+ 1 2) (list 4 5 6))"));
		System.out.println(Eval.eval("(car (list 1 2 3))"));
		System.out.println(Eval.eval("(cdr (list 1 (+ 1 1) 3))"));
		System.out.println(Eval.eval("(cons 1 (list 2 3))"));
		
		System.out.println(Eval.eval("(list? (list 1 2 3))"));
		System.out.println(Eval.eval("(list? (cons 1 (list 2 3)))"));
		System.out.println(Eval.eval("(list? (car (list 1 2 3)))"));

		System.out.println(Eval.eval("(if (list? 1) 101 102)"));
		System.out.println(Eval.eval("(if (list? (list 1 2)) 201 202)"));
		
		// quote
		System.out.println(Eval.eval("(if (list? (quote (1 2 3))) 1 0)")); // expected: 1

		// define
		Environment env = Environment.getGlobalEnvironment();
		// Environment env = (Environment) Eval.eval("(define a (+ 2 2))"); // a = 2 + 2
		Eval.eval(parser.parse("(define a (+ 2 2))"), env); // a = 2 + 2
		Eval.eval(parser.parse("(define b (+ a a))"), env); // b = a + a = 4 + 4
		System.out.println("define1 " + Eval.eval(parser.parse("(+ 1 a)"), env)); // expected: 5
		System.out.println("define2 " + Eval.eval(parser.parse("(+ b b)"), env)); // expected: 16
		
		// lambda
		Environment env2 = (Environment) Eval.eval("(define square (lambda (x) (* x x)))");
		System.out.println(Eval.eval(parser.parse("(square 3)"), env2)); // expected: 9
		System.out.println(Eval.eval(parser.parse("(square (+ 2 3))"), env2)); // expected: 25
	}
}
