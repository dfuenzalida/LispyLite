package lispy;

import java.util.ArrayList;
import java.util.List;

public class ConsFunction implements Function {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object apply(List args) {
		List secondArg = (List) args.get(1);
		List result = new ArrayList();
		result.add(args.get(0));
		result.addAll(secondArg);
		return result;
	}

}
