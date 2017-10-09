package lispy;

public class Symbol implements Emitter {
	String name;

	Symbol(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	public String toString() {
		return String.format("%s", name);
	}

	public String emit() {
		return name;
	}
}
