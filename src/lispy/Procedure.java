package lispy;

import java.util.List;

public class Procedure implements Function, Emitter {

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
		String separator = "";
		StringBuilder builder = new StringBuilder();
		for (Object p: params) {
			builder.append(separator);
			builder.append(p.toString());
			separator = " ";
		}
		return String.format("(lambda (%s) ...)", builder.toString());
	}
	
	public String emit() {
		// JS version
		// TODO consider env
		StringBuilder paramsList = new StringBuilder();
		String separator = "";
		for (Object oParam : params) {
			Symbol param = (Symbol) oParam;
			paramsList.append(separator);
			paramsList.append(param.getName());
			separator = ", ";
		}

		// Create Symbols for all the params
		Environment scopedEnv = new Environment(this.env);
		for (Object oParam : params) {
			Symbol param = (Symbol) oParam;
			scopedEnv.update(param.name, param);
		}
		return String.format("function(%s){return %s;}",  paramsList.toString(), Compiler.emit(body, scopedEnv));
	}
}
