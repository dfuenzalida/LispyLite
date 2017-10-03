package lispy;

import java.util.List;

public class CdrFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		if (args.size() == 1) {
			List firstArg = (List) args.get(0);
			return firstArg.subList(1, firstArg.size());
		} else {
			return Environment.FALSE;
		}

	}

}
