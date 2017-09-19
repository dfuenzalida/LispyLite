package lispy;

import java.util.List;

public class PlusFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		if (args.isEmpty()) return 0;

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
				total += oi;
			}
			return total;
		} else {
			Float total = 0f;
			for (Object o: args) {
				Float oi = Float.parseFloat(o.toString());
				total += oi;
			}
			return total;
		}
	}
}
