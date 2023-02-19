package org.red.fileEngine.client;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class SyncClient extends ConsoleClient{
	String find(InputTouple touple) throws IOException {
		List<Path> result = ENGINE.findSync(touple.core, touple.mask, touple.depth);
		String resultStr = result.stream().map(Path::toString).collect(Collectors.joining("\n", "\n", "\n"));
		return resultStr;
	}
}
