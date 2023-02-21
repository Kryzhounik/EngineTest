package org.red.fileEngine.client;

import java.nio.file.Path;

class ConsoleApplication implements Application{
	private final Path path;
	ConsoleApplication(String strPath) {
		path = Path.of(strPath);
		BehaviorStrategy.setPath(path);
	}

	ConsoleApplication() {
		path = null;
	}

	@Override
	public void start() {
		Client client;
		if (path != null) {
			client = new Client(System.out, System.in, BehaviorStrategy.SYNC_CACHED);
		} else {
			client = new Client(System.out, System.in);
		}
		
		client.run();
	}
}
