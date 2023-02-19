package org.red.fileEngine.engine;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.function.Predicate;

class GlobPatternPredicate implements Predicate<Path>{
	private final String mask;	
	
	private PathMatcher matcher;
	
	GlobPatternPredicate(String mask) {
		this.mask = mask;
	}

	@Override
	public boolean test(Path path) {
		
		// i think file system can't change walking throught filetree
		if (matcher == null) {
			FileSystem fs = path.getFileSystem();
			matcher = fs.getPathMatcher("glob:" + mask);
		}
		Path filename = path.getFileName();
		if (filename != null) {
			return matcher.matches(filename);
		} else {
			return false; // logical disk case
		}
		
	}
	
}