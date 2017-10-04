package lispy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
		builder.append("'pi' : Math.PI,");
		builder.append("'list' : Array.of,");
		builder.append("'first' : function(a){ return a[0]; },");
		builder.append("'rest' : function(a){ return a.slice(1); },");
		builder.append("'cons' : function(a, b){ return b.slice(0).unshift(a); },");
		builder.append("'eq?' : function(a,b){ return a === b; },");
		builder.append("'equal?' : function(a,b){ return a == b; },");
		builder.append("'apply' : function(f,args){ return f.apply(this, args); },");
		builder.append("'list?' : function(a){ return (Object.prototype.toString.call(a) === '[object Array]'); },");
		builder.append("'number?' : function(a){ return (Object.prototype.toString.call(a) === '[object Number]'); },");
		builder.append("'symbol?' : function(a){ return (['[object Number]','[object Array]'].indexOf(a) < 0); },");
		builder.append("'null?' : function(a){ return ((a === null) || (a === [])); },");
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
			return String.format("%s={};", namespace);
		}
	}

	/*
	 * Emit a JavaScript string for the given object and environment
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String emit(Object o, Environment env) {

		if (o instanceof Symbol) {
			Symbol s = (Symbol) o;
			Environment e = env.find(s.getName());
			Object found = e.get(s.getName());
			if (found instanceof Emitter) {
				return ((Emitter) found).emit();
			} else {
				return String.format("%s['%s']", Compiler.jsNamespace, s.name);
			}
		} else if (!(o instanceof List)) {
			return o.toString(); // TODO Review
		} else {
			List args = (List) o;
			String form = null;
			Object firstArg = args.get(0);
			if (firstArg instanceof Symbol) {
				form = ((Symbol)firstArg).getName();
			}

			if ("quote".equals(form)) {
				return args.get(1).toString(); // TODO Review
			} else if ("if".equals(form)) {
				Object test = args.get(1);
				Object conseq = args.get(2);
				Object alt = args.get(3);
				// TODO actually emit 'if (true? %s)' where 'true?' has the Scheme semantics
				return String.format(
						"(function(){if (%s){return %s;} else {return %s;}})()",
						emit(test, env),
						emit(conseq, env),
						emit(alt, env));
			} else if ("define".equals(form)) {
				Symbol var = (Symbol) args.get(1);
				Object exp = args.get(2);
				Object evaled = Eval.eval(exp, env);
				env.update(var.getName(), evaled);
				return String.format("%s['%s'] = %s;",
						Compiler.jsNamespace,
						var.name,
						emit(exp, env)
						);
			} else if ("set!".equals(form)) {
				Symbol var = (Symbol) args.get(1);
				Object exp = args.get(2);
				Environment e = env.find(var.getName());
				e.update(var.getName(), Eval.eval(exp, env));
				//return e;
				return "TODO: implement set!";
			} else if ("lambda".equals(form)) {
				List params = (List) args.get(1);
				Object body = args.get(2);
				return new Procedure(params, body, env).emit();
			} else {
				// *** Function application ***
				// Look for Symbol in environment

				// Evaluate the arguments
				List evaluated = new ArrayList();
				for (Object arg: args.subList(1, args.size())) {
					evaluated.add( emit(arg, env) );
				}

				if (args.get(0) instanceof Symbol) {
					Symbol s = (Symbol) args.get(0);
					String functionName = s.getName();

					// Emit JS for a Function call with a known symbol
					return String.format("%s['%s'].apply(this, %s)",
							Compiler.jsNamespace,
							functionName,
							evaluated);
				} else {
					return String.format("(%s).apply(this, %s)",
							emit(args.get(0), env),
							args.subList(1, args.size())
							);
				}

			}
		}
	}

	public static String emit(Object x) {
		return emit(x, Environment.getGlobalEnvironment());
	}

	public static String emit(String program) {
		Parser parser = new Parser();
		return emit(parser.parse(program));
	}

	public static String emit(String program, Environment env) {
		Parser parser = new Parser();
		return emit(parser.parse(program), env);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java lispy.Compiler path/to/file.scm\nOutputs 'file.js' in the current directory.");
			return;
		}

		String sourceFile = args[0];
		String namespace = sourceFile.substring(0, sourceFile.lastIndexOf("."));
		if (namespace.contains(File.separator)) {
			namespace = namespace.substring(1 + namespace.lastIndexOf(File.separator));
		}
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

			// Strip off comments
			if (line.contains(";")) {
				line = line.substring(0, line.indexOf(";"));
			}

			// Attempt parsing only non-whitespace lines
			if (!(line.matches("\\s*"))) {
				try {
					lineNum++;
					String output = Compiler.emit(line, env);
					writer.write(output + "\n");
					writer.flush();
				} catch (Exception ex) {
					throw new RuntimeException(String.format("Error when compilining line %d: %s", lineNum, line), ex);
				}
			}
		}
		reader.close();
		writer.close();
	}
}
