package org.red.fileEngine.client;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.red.fileEngine.client.Client.InputTouple;
import org.red.fileEngine.engine.FindEngine;

enum BehaviorStrategy {
	SYNC {
		String process(InputTouple touple, PrintStream out) throws IOException {
			List<Path> result = ENGINE.findSync(touple.core, touple.mask, touple.depth);
			String resultStr = result.stream().map(Path::toString).collect(Collectors.joining("\n", "\n", "\n"));
			out.println(resultStr);
			return FINISH_MARKER;
		}
	},
	ASYNC {
		String process(InputTouple touple, PrintStream out) throws IOException, InterruptedException {
			// Unfortunately right now i see no way to iterate queue by stream with blocking
//			Queue<Path> queue = engine.findAsync(touple.core, touple.mask, touple.depth);
//			result.stream()
//			.dropWhile(p -> p == FindEngine.POISON_PILL)
//			.forEach(System.out::println);

			LinkedBlockingQueue<Path> queue = ENGINE.findAsync(touple.core, touple.mask, touple.depth);
			while (true) {
				Path p = queue.take();
				if (p == FindEngine.POISON_PILL) {
					break;
				}
				out.println(p);
			}
			return FINISH_MARKER;
		}
	};

	private final static FindEngine ENGINE = new FindEngine();
	
	final static String FINISH_MARKER = "finished";

	abstract String process(InputTouple touple, PrintStream out) throws IOException, InterruptedException;
}
