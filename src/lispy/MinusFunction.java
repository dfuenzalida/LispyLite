package lispy;

import java.util.List;

public class MinusFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		boolean allInts = true;
		for (Object o: args) {
			if (o.getClass().getSimpleName().contains("Float")) {
				allInts = false;
			}
		}
		
		if (allInts) {
			Integer total = 0;
			for (Object o: args) {
				Integer oi = (Integer) o;
				total -= oi;
			}
			if (args.size() > 1) {
				total += 2 * (Integer) args.get(0);
			}
			return total;
		} else {
			Float total = 0f;
			for (Object o: args) {
				Float oi = Float.parseFloat(o.toString());
				total -= oi;
			}
			if (args.size() > 1) {
				total += 2 * Float.parseFloat(args.get(0).toString());
			}
			return total;
		}
	}

}
