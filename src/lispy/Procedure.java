package lispy;

import java.util.List;

public class Procedure implements Function {

	List params;
	Object body;
	Environment env;
	
	public Procedure(List params, Object body, Environment env) {
		this.params = params;
		this.body = body;
		this.env = env;
	}
	
	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		Environment newEnv = new Environment(params, args, env);
		return Eval.eval(body, newEnv);
	}

	@Override
	public String toString() {
		return String.format("Proc<params=%s>", params);
	}
}
