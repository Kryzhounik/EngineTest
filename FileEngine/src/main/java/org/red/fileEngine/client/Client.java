package org.red.fileEngine.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

class Client {

	private final static String DELIMITER = " ";

	private final PrintStream out;
	private final InputStream in;
	private final BehaviorStrategy strategy;
	
	final static String EXIT_CODE = "exit";

	Client(OutputStream out, InputStream in) {
		this(out, in, BehaviorStrategy.ASYNC);
	}

	Client(OutputStream out, InputStream in, BehaviorStrategy strategy) {
		this.out = new PrintStream(out);
		this.in = in;
		this.strategy = strategy;
	}

	//TODO:final
	void run() {
		Scanner scanner = new Scanner(in, "utf-8");
		out.println("started");
		while (true) {
			// hope there is no case for mask (and core path) with spaces. it could be extra
			// requirement but still
			out.println("enter parameters by space\r\n[core mask depth]:");
			String input = scanner.nextLine();
			if (!EXIT_CODE.equals(input.toLowerCase())) {
				String responce = operate(input);
				out.println(responce);
			} else {
				break;
			}
		}
		scanner.close();
	}

	private String find(InputTouple touple) throws IOException, InterruptedException {
		return strategy.process(touple, out);
	}

	private String operate(String input) {
		out.println("operating...");
		try {
			InputTouple touple = validateInput(input);
			String result = find(touple);
			return result;
		} catch (IllegalInputException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "internal exception has occurred";
		}

	}

	private InputTouple validateInput(String input) throws IllegalInputException {
		String[] params = input.split(DELIMITER);
		if (params.length > 3) {
			throw new IllegalInputException("exceed number of input parameters");
		}

		final Path path;
		try {
			path = Path.of(params[0]);
		} catch (InvalidPathException e) {
			throw new IllegalInputException("invalid core path " + e.getMessage());
		}
		if (!Files.exists(path)) {
			throw new IllegalInputException("root path [" +path+ "] don't exist");
		}

		String mask = null;
		if (params.length > 1) {
			mask = params[1];
		}

		Integer depth = -1;
		if (params.length > 2) {
			try {
				depth = Integer.valueOf(params[2]);
			} catch (NumberFormatException e) {
				throw new IllegalInputException("invalid depth " + e.getMessage());
			}
		}

		InputTouple tuple = new InputTouple(path, mask, depth);
		return tuple;
	}

	static final class InputTouple {
		final Path core;
		final String mask;
		// i think byte or short would be enough, but int is more common used.
		int depth;

		private InputTouple(Path core, String mask, int depth) {
			super();
			this.core = core;
			this.mask = mask;
			this.depth = depth;
		}

	}

	private static final class IllegalInputException extends Exception {
		private IllegalInputException(String message) {
			super(message);
		}
	}
}
