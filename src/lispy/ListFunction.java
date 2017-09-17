package lispy;

import java.util.List;

public class ListFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		Object retval = args;
		return retval;
	}

}
