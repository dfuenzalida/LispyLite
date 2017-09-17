package lispy;

import java.util.List;

public class CarFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		List firstArg = (List) args.get(0);
		return firstArg.get(0);
	}

}
