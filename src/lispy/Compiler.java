package lispy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

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
		builder.append("'and' : function(a,b){ return (a && b); },");
		builder.append("'or' : function(a,b){ return (a || b); },");
		// TODO add the rest of the built-in functions
		builder.append("'display': function(s){ if (console){ console.log(s); }; return null; }");
		builder.append("};");
		return builder.toString();
	};

	// Produce nested Javascript objects to simulate a namespace for the given name
	public static String getNamespaceDefinition(String namespace) {
		if (namespace.contains(".")) {
			String[] components = namespace.split("\\.");
			String result = components[0] + "={";
			for (int i = 1; i < components.length; i++) {
				result += components[i] + ":{";
			}

			for (int i = 1; i < components.length; i++) {
				result += "}";
			}

			result += "};";
			return result;
		} else {
			return String.format("%s ||= {};", namespace);
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java lispy.Compiler path/to/file.scm\nOutputs 'file.js' in the current directory.");
			return;
		}

		String sourceFile = args[0];
		String namespace = sourceFile.substring(0, sourceFile.lastIndexOf("."));
		if (namespace.contains("/")) { namespace = namespace.substring(1 + namespace.lastIndexOf("/")); }
		Compiler.jsNamespace = namespace;
		String targetFile = String.format("%s.js", namespace);
		System.out.println(String.format("Output: %s", targetFile));

		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, false));
		writer.write(getNamespaceDefinition(namespace) + "\n");
		writer.write(prelude(namespace) + "\n");
		String line;
		Environment env = Environment.getGlobalEnvironment();
		int lineNum = 0;
		while ((line = reader.readLine()) != null) {
			try {
				lineNum++;
				String output = Eval.emit(line, env);
				writer.write(output + "\n");
			} catch (Exception ex) {
				throw new RuntimeException(String.format("Error when compilining line %d: %s", lineNum, line), ex);
			}
		}
		reader.close();
		writer.flush();
		writer.close();
	}
}
