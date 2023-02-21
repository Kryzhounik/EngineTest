package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class FindEngineImpl implements FindEngine {
	private final static TreeFinder FINDER = new TreeFinder();
	private final static ExecutorService EXECUTOR = Executors.newWorkStealingPool();

	// TODO: what to do with excepti
	@Override
	public BlockerablePathQueue findAsync(Path rootPath, String mask, int depth) throws InterruptedException {
		LinkedBlockingQueue<Path> queue= new LinkedBlockingQueue<>();
		BlockerablePathQueue wrapper = new BlockerablePathQueue(queue);	
		EXECUTOR.execute(() -> {
			try {
				FINDER.find(rootPath, mask, depth, queue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				wrapper.close();
			}
		});
		
		return wrapper;
	}

	@Override
	public List<Path> findSync(Path rootPath, String mask, int depth) throws IOException {
		List<Path> list = new LinkedList<Path>();
		FINDER.find(rootPath, mask, depth, list);
		return list;
	}

}
