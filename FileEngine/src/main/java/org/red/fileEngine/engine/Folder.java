package org.red.fileEngine.engine;

import java.nio.file.Path;
import java.util.List;

interface Folder{
	List<Path> getFilesBy(String mask, int depth);

	void add(Path path, int level);
}