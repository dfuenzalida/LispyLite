package lispy;

import java.util.List;

public class DivisionFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		Object first = args.get(0);
		Float total;
		if (first instanceof Integer) {
			total = 1.0f * (Integer) first;
		} else {
			total = 1.0f * (Float) first;
		}
		
		for(Object o: args.subList(1, args.size())) {
			if (o instanceof Integer) {
				total /= 1.0f * (Integer) o;
			} else {
				total /= 1.0f * (Float) o;
			}
		}
		
		return total;
	}

}
