package org.red.fileEngine.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

class ActualizerImpl implements Actualizer {
	private final FindEngine findEngine;

	private final Path root;

	ActualizerImpl(Path root, FindEngine findEngine) {
		this.root = root;
		this.findEngine = findEngine;
	}

	@Override
	public Folder actualizeFolder() throws IOException, InterruptedException {
		BlockerablePathQueue queue = findEngine.findAsync(root, "*", Integer.MAX_VALUE);
		Analizer analizer = new Analizer();
		queue.blockingforEach(analizer);
		return analizer.folder;
	}

	private final class Analizer implements Consumer<Path> {
		private final Folder folder = new FolderImpl();

		@Override
		public void accept(Path path) {
			int level = determineLevel(path);
			folder.add(path, level);
		}
		
		// first idea. i'm not sure that the best one...
		private final int determineLevel(Path path) {
			Path relative = root.relativize(path);
			return relative.getNameCount();			
		}
	}
}
