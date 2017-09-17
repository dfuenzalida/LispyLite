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

		
		env.put("pi", new Float(3.1415926f));
		env.put("list", new ListFunction());
		env.put("car", new CarFunction());
		env.put("cdr", new CdrFunction());
		env.put("first", new CarFunction());
		env.put("rest", new CdrFunction());
		env.put("cons", new ConsFunction());
		
		env.put("list?", new Function(){
			@Override
			public Object apply(List args) {
				if (args.get(0).getClass().getName().contains("List")) {
					return args.get(0);
				} else {
					return FALSE;
				}
			}
		});
		
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
	
	public Environment find(String var) {
		if (env.containsKey(var)) {
			return this;
		} else {
			return outer.find(var);
		}
	}
	
	public String toString(){
		return String.format("Env<%s>", env.toString());
	}
}
