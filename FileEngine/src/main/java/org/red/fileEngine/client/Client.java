package org.red.fileEngine.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

final class Client {

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


	final void run() {
		String notification = "enter parameters by space\r\n[core mask depth]:";
		if (strategy.equals(BehaviorStrategy.SYNC_CACHED)) {
			notification = notification.replace("core ", "");
		}
		
		Scanner scanner = new Scanner(in, "utf-8");
		out.println("started");
		while (true) {
			// hope there is no case for mask (and core path) with spaces. it could be extra
			// requirement but still
			out.println(notification);
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

	//slightly tricky according to nesesarity(?) validate different number of input params.
	private InputTouple validateInput(String input) throws IllegalInputException {
		int expectedCount = 3;
		int index = 0;
		if (strategy.equals(BehaviorStrategy.SYNC_CACHED)) {
			expectedCount--;
			index--;
		}
		
		
		String[] params = input.split(DELIMITER);
		if (params.length > expectedCount) {
			throw new IllegalInputException("exceed number of input parameters");
		}

		Path path = BehaviorStrategy.getPath();
		try {
			path = Path.of(params[index]);
		} catch (InvalidPathException e) {
			throw new IllegalInputException("invalid core path " + e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			//nothing. it means that path have already set up
		}
		if (!Files.exists(path)) {
			throw new IllegalInputException("root path [" +path+ "] don't exist");
		}

		
		String mask = null;
		if (params.length > 1) {
			mask = params[++index];
		}

		Integer depth = Integer.MAX_VALUE;
		if (params.length > expectedCount - 1) {
			try {
				depth = Integer.valueOf(params[++index]);
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
