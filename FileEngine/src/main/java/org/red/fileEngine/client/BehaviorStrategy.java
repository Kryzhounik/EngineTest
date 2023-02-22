package org.red.fileEngine.client;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.red.fileEngine.client.Client.InputTouple;
import org.red.fileEngine.engine.BlockerablePathQueue;
import org.red.fileEngine.engine.CachedFindEngine;

enum BehaviorStrategy {
	
	/** IMPORTANT!!!
	 * could be used only in case corePath was set by method BehaviorStrategy.setPath
	 *
	 */
	SYNC_CACHED {
		String process(InputTouple touple, PrintStream out) throws IOException {
			//actually it's impossible but just in case...
			assert(touple.core == null || touple.core.equals(ENGINE.getPath()));
			
			List<Path> result = ENGINE.find(touple.mask, touple.depth);
			printSyncResult(result, out);
			return FINISH_MARKER;
		}
	},
	
	@Deprecated
	SYNC {
		String process(InputTouple touple, PrintStream out) throws IOException {
			List<Path> result = ENGINE.findSync(touple.core, touple.mask, touple.depth);
			printSyncResult(result, out);
			return FINISH_MARKER;
		}
	},
	
	@Deprecated
	ASYNC {
		String process(InputTouple touple, PrintStream out) throws IOException, InterruptedException {
			BlockerablePathQueue queue = ENGINE.findAsync(touple.core, touple.mask, touple.depth);
			queue.blockingforEach(out::println);
			return FINISH_MARKER;
		}
	};

	private final static CachedFindEngine ENGINE = new CachedFindEngine();

	final static String FINISH_MARKER = "finished";
	

	static void setPath(Path path) {
		ENGINE.setPath(path);
	}
	
	static Path getPath() {
		return ENGINE.getPath();
	}
	
	private final static void printSyncResult(List<Path> result, PrintStream out) {
		result.stream().forEach(out::println);
	}
	
	abstract String process(InputTouple touple, PrintStream out) throws IOException, InterruptedException;
}
