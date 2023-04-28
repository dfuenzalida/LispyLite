package lispy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {

	Environment outer = null;
	Map<String, Object> env = new HashMap<String, Object>();
	public static final List FALSE = Collections.EMPTY_LIST;

	public Environment() {
		
		// Support for addition
		env.put("+", new PlusFunction());
		env.put("-", new MinusFunction());
		env.put("*", new ProductFunction());
		env.put("/", new DivisionFunction());
		
		env.put("display", new Function() {
			@Override
			public Object apply(List args) {
				System.out.println(args.get(0));
				return null;
			}
		});

		env.put("and", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0).equals(Environment.FALSE) || args.get(1).equals(Environment.FALSE)) {
					return Environment.FALSE;
				} else {
					return args.get(0);
				}
			}
		});

		env.put("or", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0).equals(Environment.FALSE) && args.get(1).equals(Environment.FALSE)) {
					return Environment.FALSE;
				} else {
					if (args.get(0).equals(Environment.FALSE)) {
						return args.get(1);
					} else {
						return args.get(0);
					}
				}
			}
		});

		env.put(">", new GreaterThanFunction());
		env.put("<=", new Function() {
			GreaterThanFunction gtf = new GreaterThanFunction();
			@Override
			public Object apply(List args) {
				if (gtf.apply(args) == Environment.FALSE) {
					return args.get(0);
				} else {
					return Environment.FALSE;
				}
			}
		});

		env.put("<", new LessThanFunction());
		env.put(">=", new Function() {
			LessThanFunction ltf = new LessThanFunction();
			@Override
			public Object apply(List args) {
				if (ltf.apply(args) == Environment.FALSE) {
					return args.get(0);
				} else {
					return Environment.FALSE;
				}
			}
		});

		
		env.put("pi", 3.1415926f);
		env.put("list", new ListFunction());
		env.put("car", new CarFunction());
		env.put("cdr", new CdrFunction());
		env.put("first", new CarFunction());
		env.put("rest", new CdrFunction());
		env.put("cons", new ConsFunction());
		
		// eq? tests for same object reference
		env.put("eq?", new Function(){
			@Override
			public Object apply(List args) {
				if (args.get(0) == args.get(1)) {
					return args.get(0);
				} else {
					return FALSE;
				}
			}
		});
		
		env.put("equal?", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0).equals(args.get(1))) {
					return args.get(0);
				} else {
					return FALSE;
				}
			}
		});
		
		env.put("apply", new ApplyFunction(this));
		
		env.put("quit", new Function() {
			@Override
			public Object apply(List args) {
				System.exit(0);
				return null;
			}
		});

		env.put("list?", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0) instanceof List) {
					return args.get(0);
				} else {
					return Environment.FALSE;
				}
			}
		});

		env.put("symbol?", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0) instanceof Symbol) {
					return args.get(0);
				} else {
					return Environment.FALSE;
				}
			}
		});

		env.put("number?", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0) instanceof Integer || args.get(0) instanceof Float) {
					return args.get(0);
				} else {
					return Environment.FALSE;
				}
			}
		});

		env.put("null?", new Function() {
			@Override
			public Object apply(List args) {
				if (args.get(0) instanceof List && ((List)args.get(0)).size() == 0) {
					return 1;
				} else {
					return Environment.FALSE;
				}
			}
		});


		// TODO begin, length, map, not, null?, procedure?
	}
	
	public Environment(List params, List args, Environment outer) {
		super();
		for(int i = 0; params != null && i < params.size(); i++) {
			Symbol s = (Symbol) params.get(i);
			env.put(s.getName(), args.get(i));
		}
		this.outer = outer;
	}
	
	public Environment(Environment outer) {
		super();
		this.outer = outer;
	}
	
	public Object get(String name) {
		return env.get(name);
	}
	
	public void update(String name, Object o) {
		env.put(name, o);
	}
	
	public static Environment getGlobalEnvironment() {
		return new Environment();
	}

	public static String[] getBuiltins() {
		return new String[] {
				"(define foldr (lambda (f z xs) (if xs (f (first xs) (foldr f z (rest xs))) z)))",
				"(define concat (lambda (xs ys) (foldr cons ys xs)))",
				"(define inc (lambda (x) (+ x 1)))",
				"(define dec (lambda (x) (- x 1)))",
				"(define count (lambda (xs) (foldr (lambda (_ x) (inc x)) 0 xs)))",
				"(define map (lambda (f xs) (foldr (lambda (y ys) (cons (f y) ys)) (list) xs)))",
				"(define filter (lambda (f xs) (foldr (lambda (y ys) (if (f y) (cons y ys) ys)) (list) xs)))",
				"(define reduce (lambda (f val coll) (foldr f val coll)))",
				"(define #f (list))",
				"(define range (lambda (from to) (if (< from to) (cons from (range (inc from) to)) (list))))"
		};
	}
	
	public Environment find(String var) {
		try {
			if (env.containsKey(var)) {
				return this;
			} else {
				return outer.find(var);
			}
		} catch (Exception ex) {
			throw new RuntimeException(String.format("Value '%s' is not defined", var), ex);
		}
	}
	
	public String toString(){
		return String.format("Env<%s>", env.toString());
	}
}
