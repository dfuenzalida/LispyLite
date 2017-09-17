package lispy;

import java.util.List;

public class ApplyFunction implements Function {
	
	Environment env;
	
	public ApplyFunction(Environment e) {
		this.env = e;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object apply(List args) {
		Function f = (Function) args.get(0);
		return f.apply((List)args.get(1));
	}

}
