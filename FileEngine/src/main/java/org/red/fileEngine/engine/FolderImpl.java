package org.red.fileEngine.engine;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FolderImpl implements Folder {
	private final Map<Integer, Level> map = new ConcurrentHashMap<>(124);

	@Override
	public List<Path> getFilesBy(String mask, int depth) {
		List<Path> result = new LinkedList<>();
		for (int i = 1; i <= depth; i++) {
			Level lvl = map.get(i);
			List<Path> lvlResult = lvl.getFilesBy(mask);
			result.addAll(lvlResult);
		}
		return result;
	}

	@Override
	public void add(Path path, int level) {
		Level lvl = map.computeIfAbsent(level, l -> new Level());
		lvl.add(path);
	}

	private final class Level{
		private Collection<Path> paths = new LinkedList<>();
		public List<Path> getFilesBy(String mask) {
			Predicate<? super Path> predicate = new GlobPatternPredicate(mask);
			List<Path> result = paths.stream()
				.filter(predicate)
				//.map(Object::toString)
				.collect(Collectors.toList());
			return result;
		}
		
		public void add(Path path) {
			paths.add(path);
			
		}
	}
}
