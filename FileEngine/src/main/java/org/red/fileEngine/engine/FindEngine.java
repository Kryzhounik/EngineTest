package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FindEngine {

	BlockerablePathQueue findAsync(Path rootPath, String mask, int depth) throws InterruptedException;

	List<Path> findSync(Path rootPath, String mask, int depth) throws IOException;

}