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
		List<String> result = new LinkedList<>();
		for (int i = 0; i <= depth; i++) {
			Level lvl = map.get(i);
			List<String> lvlResult = lvl.getFilesBy(mask);
			result.addAll(lvlResult);
		}
		return null;
	}

	private final class Level{
		Collection<Path> paths;
		public List<String> getFilesBy(String mask) {
			Predicate<? super Path> predicate = new GlobPatternPredicate(mask);
			List<String> result = paths.stream()
				.filter(predicate)
				.map(Object::toString)
				.collect(Collectors.toList());
			return result;
		}
	}
}
