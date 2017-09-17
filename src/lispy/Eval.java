package lispy;

import java.util.ArrayList;
import java.util.List;

public class Eval {

	// Convenience method, eg.: Eval.eval("(+ 1 2 3)");
	public static Object eval(String program) {
		Parser parser = new Parser();
		return eval(parser.parse(program));
	}
	
	public static Object eval(Object x) {
		return eval(x, Environment.getGlobalEnvironment());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object eval(Object o, Environment env) {

		if (o instanceof Symbol) {
			Symbol s = (Symbol) o;
			Environment e = env.find(s.getName());
			return e.get(s.getName());
		} else if (!(o instanceof List)) {
			return o;
		} else {
			List args = (List) o;
			
			String form = null;
			Object firstArg = args.get(0);
			if (firstArg instanceof Symbol) {
				form = ((Symbol)firstArg).getName();
			}			

			if ("quote".equals(form)) {
				return args.get(1);
			} else if ("if".equals(form)) {
				Object test = args.get(1);
				Object conseq = args.get(2);
				Object alt = args.get(3);
				Object expr;
				Object testEvaled = eval(test, env);
				if (testEvaled instanceof List && ((List)testEvaled).isEmpty()) {
					expr = alt;
				} else {
					expr = conseq;
				}
				return eval(expr, env);
			} else if ("define".equals(form)) {
				Symbol var = (Symbol) args.get(1);
				Object exp = args.get(2);
				Object evaled = eval(exp, env);
				env.update(var.getName(), evaled);
				return evaled;
			} else if ("set!".equals(form)) {
				Symbol var = (Symbol) args.get(1);
				Object exp = args.get(2);
				Environment e = env.find(var.getName());
				e.update(var.getName(), eval(exp, env));
				return e;
			} else if ("lambda".equals(form)) {
				List params = (List) args.get(1);
				Object body = args.get(2);
				return new Procedure(params, body, env);
			} else {
				// *** Function application ***
				// Look for Symbol in environment
				
				Symbol s;
				Environment e;
				Function f;

				if (args.get(0) instanceof Symbol) {
					s = (Symbol) args.get(0);
					e = env.find(s.getName());
					f = (Function) e.get(s.getName());
				} else {
					e = env;
					f = (Function) eval(args.get(0), env);
				}

				// Evaluate the arguments
				List evaluated = new ArrayList();
				for (Object arg: args.subList(1, args.size())) {
					evaluated.add( eval(arg, env) );
				}
				
				// Function call
				return f.apply(evaluated);
			}
		}
	}
}
