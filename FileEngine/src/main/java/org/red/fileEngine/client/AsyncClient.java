package org.red.fileEngine.client;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.red.fileEngine.client.ConsoleClient.InputTouple;
import org.red.fileEngine.engine.FindEngine;
import org.red.fileEngine.engine.TreeFinder;

public class AsyncClient extends ConsoleClient {

	private final static FindEngine engine = new FindEngine();

	String find(InputTouple touple) throws IOException, InterruptedException {
		//Unfortunately right now i see no way to iterate queue by stream with blocking
//		Queue<Path> queue = engine.findAsync(touple.core, touple.mask, touple.depth);
//		result.stream()
//		.dropWhile(p -> p == FindEngine.POISON_PILL)
//		.forEach(System.out::println);

		LinkedBlockingQueue<Path> queue = engine.findAsync(touple.core, touple.mask, touple.depth);
		while(true) {
			Path p = queue.take();
			if (p.equals(FindEngine.POISON_PILL)){
				break;
			}
			System.out.println(p);
		}
		return "finished";
	}
}
