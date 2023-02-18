package org.red.fileEngine.client;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.red.fileEngine.engine.TreeFinder;

public class ConsoleClient {
	private final static String DELIMITER = " ";
	private final static String EXIT_CODE = "exit";
	
	private final static TreeFinder engine = new TreeFinder();
	
	public static void main(String[] args) {
		System.out.println("starting...");
		run();
	}
	
	private static void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("started");
		while (true) {
			//hope there is no case for mask (and core path) with spaces. it could be extra requirement but still
			System.out.println("enter parameters by space\r\n[core mask depth]:");
			String input = scanner.nextLine();
			if (!EXIT_CODE.equals(input.toLowerCase())){
				String responce = operate(input);
				System.out.println(responce);
			} else {
				break;
			}
			
		}
		scanner.close();
		System.out.println("stoped");
	}
	
	private static String operate(String input) {
		System.out.println("operating...");
		try {
			InputTouple touple = validateInput(input);
			List<Path> result = engine.find(touple.core, touple.mask, touple.depth);
		    String resultStr = result.stream()
		    	      .map(Path::toString)
		    	      .collect(Collectors.joining("\n","\n","\n"));
		    return resultStr;
		} catch (IllegalInputException e) {
			return e.getMessage();
		} catch (Exception e) {
			return "internal exception has occurred";
		}
		
		
		
	}

	private static InputTouple validateInput(String input) throws IllegalInputException{
		String[] params = input.split(DELIMITER);
		if (params.length > 3){
			throw new IllegalInputException("exceed number of input parameters");
		}
		
		final Path path;
		try {
			path = Path.of(params[0]);
		} catch (InvalidPathException e){
			throw new IllegalInputException("invalid core path "+ e.getMessage());
		}
		if (!Files.exists(path)) {
			throw new IllegalInputException("root path don't exist");
		}
		
		String mask = null;
		if (params.length > 1) {
			mask = params[1];
		}
		
		Integer depth =-1;
		if (params.length > 2) {
			try {
				depth = Integer.valueOf(params[2]);
			} catch (NumberFormatException e) {
				throw new IllegalInputException("invalid depth "+ e.getMessage());
			}
		}
		
		InputTouple tuple = new InputTouple(path, mask, depth);
		return tuple;
	}
	
	private static final class InputTouple{
		private final Path core;
		private final String mask;
		// i think byte or short would be enough, but int is more common used.
		private int depth;
		private InputTouple(Path core, String mask, int depth) {
			super();
			this.core = core;
			this.mask = mask;
			this.depth = depth;
		}
		
	}
	
	private static final class IllegalInputException extends Exception{
		private IllegalInputException(String message) {
			// TODO Auto-generated constructor stub
		}
	}

}

