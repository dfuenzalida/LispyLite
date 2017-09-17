package lispy;

public class Symbol {
	String name;

	Symbol(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	public String toString() {
		return String.format("Symbol<%s>", name);
	}
}
