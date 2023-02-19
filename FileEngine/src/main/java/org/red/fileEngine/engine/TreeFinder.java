package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class TreeFinder {

	List<Path> find(Path rootPath, String mask) throws IOException {
		return find(rootPath, mask, Integer.MAX_VALUE);
	}

	// TODO: should i except the root?
	List<Path> find(Path rootPath, String mask, int depth) throws IOException {
		List<Path> files = new LinkedList<Path>();
		find(rootPath, mask, depth, files);
		return files;
	}

	void find(Path rootPath, String mask, int depth, Collection<Path> collection) throws IOException {
		Predicate<? super Path> predicate = new GlobPatternPredicate(mask);
		Files.walk(rootPath, depth).filter(predicate).forEach(collection::add);
	}

}
