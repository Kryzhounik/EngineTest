package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;

//decorator
public class CachedFindEngine implements FindEngine {
	private final FindEngine findEngine;
	
	private Path core;
	private Actualizer actualizer;
	
	private Folder actualFolder;

	public CachedFindEngine(Path path) {
		this();
		setPath(path);
	}
	
	public CachedFindEngine() {
		this.findEngine = new FindEngineImpl();
	}
	
	public List<Path> find(String mask, int depth) throws IOException {
		//TODO: need logic for time until first folder got. but not so critical i think
		assert(actualFolder != null);
		
		List<Path> files = actualFolder.getFilesBy(mask, depth);
		return files;
	}
	
	public Path getPath() {
		return core;
	}
	
	//at now could be set only once
	public void setPath(Path path) {
		assert(core == null);
		core = path;
		actualizer = new ActualizerImpl(path, findEngine);
		Executors.newSingleThreadExecutor().execute(new DeamonActualizer());
	}
	
	
	@Override
	@Deprecated
	public BlockerablePathQueue findAsync(Path rootPath, String mask, int depth) throws InterruptedException {
		// TODO Auto-generated method stub
		return findEngine.findAsync(rootPath, mask, depth);
	}

	@Override
	@Deprecated
	public List<Path> findSync(Path rootPath, String mask, int depth) throws IOException {
		// TODO Auto-generated method stub
		return findEngine.findSync(rootPath, mask, depth);
	}


	private final class DeamonActualizer implements Runnable{
		private boolean interupted = false;
		@Override
		public void run() {
			while (!interupted) {
				try {
					actualFolder = actualizer.actualizeFolder();
				} catch (IOException | InterruptedException e) {
					//TODO: should it shut down, or try one more time?
					e.printStackTrace();
				}
			}
		}
	}
}
