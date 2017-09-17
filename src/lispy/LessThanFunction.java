package lispy;

import java.util.List;

public class LessThanFunction implements Function {

	@SuppressWarnings("rawtypes")
	public Object apply(List args) {
		Object first = args.get(0);
		Object second = args.get(1);
		Float f1, f2;
		
		if (first instanceof Integer) {
			f1 = 1.0f * (Integer) first;
		} else {
			f1 = (Float) first;
		}

		if (second instanceof Integer) {
			f2 = 1.0f * (Integer) second;
		} else {
			f2 = (Float) second;
		}

		if (f1 < f2) {
			if (first instanceof Integer) return first;
			else return f1;
		} else {
			return Environment.FALSE;
		}
	}

}
