package lispy;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface Function {
	Object apply(List args);
}
