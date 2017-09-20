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
		// TODO add the rest of the built-in functions
		builder.append("'display': function(s){ if (console){ console.log(s); }; return null; }");
		builder.append("};");
		return builder.toString();
	};

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
		writer.write(prelude(namespace) + "\n");
		String line;
		Environment env = Environment.getGlobalEnvironment();
		while ((line = reader.readLine()) != null) {
			String output = Eval.emit(line, env);
			writer.write(output + "\n");
		}
		reader.close();
		writer.flush();
		writer.close();
	}
}
