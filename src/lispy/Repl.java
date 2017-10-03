package lispy;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Repl {

	public static void main(String[] args) {
		System.out.println("Welcome to the Lispy REPL");
		System.out.println("Enter '(quit)' or Ctrl-D to exit.");
		String line = null;
		Environment env = Environment.getGlobalEnvironment();
		while (true) {
			Parser parser = new Parser();
			try {
				Scanner sc = new Scanner(System.in);
				System.out.print("lispy> ");
				line = sc.nextLine();

				// Strip off comments
				if (line.contains(";")) {
					line = line.substring(0, line.indexOf(";"));
				}

				// Attempt parsing only non-whitespace lines
				if (!(line.matches("\\s*"))) {
					Object result = Eval.eval(parser.parse(line), env);
					if (!(result instanceof Environment)) {
						System.out.println(result);
					}
				}
			} catch (NoSuchElementException nse) {
				// Thrown when the Scanner can't read a line after a Ctrl-D
				System.out.println("\n");
				System.exit(0);
			} catch (Exception ex) {
				System.out.println(String.format("Error evaluating expression '%s': %s", line, ex.getMessage()));
			}
		}
	}
}
