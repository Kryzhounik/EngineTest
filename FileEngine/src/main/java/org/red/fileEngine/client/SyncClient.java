package org.red.fileEngine.client;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.red.fileEngine.engine.TreeFinder;

class SyncClient extends ConsoleClient{
	private final static TreeFinder engine = new TreeFinder();
	String find(InputTouple touple) throws IOException {
		List<Path> result = engine.find(touple.core, touple.mask, touple.depth);
		String resultStr = result.stream().map(Path::toString).collect(Collectors.joining("\n", "\n", "\n"));
		return resultStr;
	}
}
