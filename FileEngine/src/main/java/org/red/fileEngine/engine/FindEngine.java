package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class FindEngine {
	public final static Path POISON_PILL = Path.of("UTIL", "POISON", "PILL", "PATH");
	
	private final static TreeFinder FINDER = new TreeFinder();
	private final static ExecutorService EXECUTOR = Executors.newWorkStealingPool();

	//TODO: what to do with excepti
	public LinkedBlockingQueue<Path> findAsync(Path rootPath, String mask, int depth) throws InterruptedException {
		LinkedBlockingQueue<Path> queue = new LinkedBlockingQueue<Path>();
		EXECUTOR.execute(() -> {
			try {
				FINDER.find(rootPath, mask, depth, queue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				//TODO: need poison not null. some oject
				queue.add(POISON_PILL); // so called poison pill
			}
		});
		
		//EXECUTOR.
		
		return queue;
	}

}
