package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class NewFindEngine {
	private Folder actualFolder;
	private Actualizer actualizer;
	
	public NewFindEngine() {
		Executors.newSingleThreadExecutor().execute(new DeamonActualizer());
	}
	
	public List<Path> find(String mask, int depth) throws IOException {
		//TODO: need logic for time until first folder got. but not so critical i think
		assert(actualFolder != null);
		
		List<Path> files = actualFolder.getFilesBy(mask, depth);
		return files;
	}
	
	
	private final class DeamonActualizer implements Runnable{
		private boolean interupted = false;
		@Override
		public void run() {
			while (!interupted) {
				try {
					actualFolder = actualizer.actualizeFolder();
				} catch (IOException e) {
					//TODO: should it shut down, or try one more time?
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	
	
	
	//TODO: move to parent file?
	// ---------------- Legacy
	
	public final static Path POISON_PILL = Path.of("UTIL", "POISON", "PILL", "PATH");

	private final static TreeFinder FINDER = new TreeFinder();
	private final static ExecutorService EXECUTOR = Executors.newWorkStealingPool();
	
	@Deprecated //legacy could be removed ar used for some testing purposes or updated in future
	public LinkedBlockingQueue<Path> findAsync(Path rootPath, String mask, int depth) throws InterruptedException {
		LinkedBlockingQueue<Path> queue = new LinkedBlockingQueue<Path>();
		EXECUTOR.execute(() -> {
			try {
				FINDER.find(rootPath, mask, depth, queue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// TODO: need poison not null. some oject
				queue.add(POISON_PILL); // so called poison pill
			}
		});
		return queue;
	}

	@Deprecated //legacy could be removed ar used for some testing purposes or updated in future
	public List<Path> findSync(Path rootPath, String mask, int depth) throws IOException {
		List<Path> list = new LinkedList<Path>();
		FINDER.find(rootPath, mask, depth, list);
		return list;
	}

}
