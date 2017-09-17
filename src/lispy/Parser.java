package lispy;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

	List<String> tokens;

	Object parse(String program) {
		return readFromTokens(tokenize(program));
	}

	List<String> tokenize(String str) {
		List<String> splitted = Arrays.asList(str.replaceAll("\\(", " \\( ").replaceAll("\\)", " \\) ").split("\\s+"));
		if (splitted.get(0).equals("")) return splitted.subList(1, splitted.size());
		else return splitted;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	Object readFromTokens(List<String> tokens) {
		this.tokens = tokens;
		if (this.tokens.isEmpty()) {
			throw new RuntimeException("Unexpected EOF while reading");
		}
		String token = this.tokens.get(0);
		this.tokens = tokens.subList(1, tokens.size());
		if ("(".equals(token)) {
			List nestedList = new ArrayList();
			while (!(")".equals(this.tokens.get(0)))) {
				nestedList.add(readFromTokens(this.tokens));
			}
			this.tokens = this.tokens.subList(1, this.tokens.size());
			return nestedList;
		} else if (")".equals(token)) {
			throw new RuntimeException("Unexpected ')'");
		} else {
			return atomFromToken(token);
		}
	}

	Object atomFromToken(String token) {
		try {
			return Integer.valueOf(token);
		} catch (Exception ex) {
			try {
				return Float.valueOf(token);
			} catch (Exception ex2) {
				return new Symbol(token);
			}
		}
	}

}
