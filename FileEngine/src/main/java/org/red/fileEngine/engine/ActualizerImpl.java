package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;

class ActualizerImpl implements Actualizer {
	private final static TreeFinder FINDER = new TreeFinder();
	
	private final Path root;
	
	ActualizerImpl(Path root) {
		super();
		this.root = root;
	}

	@Override
	public Folder actualizeFolder() throws IOException {
		FINDER.find(root);
		return null;
	}
	
}
