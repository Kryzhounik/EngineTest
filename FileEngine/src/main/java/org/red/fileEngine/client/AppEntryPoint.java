package org.red.fileEngine.client;

import java.io.IOException;

public class AppEntryPoint {
	// TODO: java.nio.file.AccessDeniedException for some directories

	public static void main(String[] args) throws IOException {
		System.out.println("starting...");
		Application app;
		if (args.length == 2) {
			app = new SocketApplication(args[0], args[1]);
		} else if (args.length == 1) {
			app = new ConsoleApplication(args[0]);
		} else {
			app = new ConsoleApplication();
		}
		app.start();
		System.out.println("stoped");
	}

}
