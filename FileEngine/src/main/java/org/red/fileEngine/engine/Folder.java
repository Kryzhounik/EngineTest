package org.red.fileEngine.engine;

import java.nio.file.Path;
import java.util.List;

public interface Folder{
	List<Path> getFilesBy(String mask, int depth);
}