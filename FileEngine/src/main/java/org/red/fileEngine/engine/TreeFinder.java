package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeFinder {

	List<Path> find(Path rootPath, String mask) throws IOException {
		return find(rootPath, mask, Integer.MAX_VALUE);
	}
	//TODO: should i except the root?
	public List<Path> find(Path rootPath, String mask, int depth) throws IOException {
		Predicate<? super Path> predicate = new GlobPatternPredicate(mask);
		List<Path> files = Files.walk(rootPath, depth)
				//.peek(System.out::println)
				.filter(predicate)
				.collect(Collectors.toList());
		return files;
	}
}

