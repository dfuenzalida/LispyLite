package lispy;

public class Compiler {

	public static String jsNamespace = "lispy";

	public static String prelude(String namespace) {
		StringBuilder builder = new StringBuilder();
		builder.append(jsNamespace + "={");
		builder.append("'=' : function(a,b){ return a == b; },");
		builder.append("'+' : function(a,b){ return a + b; },");
		builder.append("'-' : function(a,b){ return a - b; },");
		builder.append("'*' : function(a,b){ return a * b; },");
		builder.append("'/' : function(a,b){ return a / b; },");
		builder.append("'<' : function(a,b){ return (a < b); },");
		builder.append("'<=': function(a,b){ return (a <= b); },");
		builder.append("'>=': function(a,b){ return (a >= b); },");
		builder.append("'>' : function(a,b){ return (a > b); },");
		builder.append("'display': function(s){ if (console){ console.log(s); }; return null; }");
		builder.append("};");
		return builder.toString();
	};

	public static void main(String[] args) {
		// TODO implement
	}
}
