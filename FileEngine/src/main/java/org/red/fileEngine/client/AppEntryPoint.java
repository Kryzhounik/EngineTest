package org.red.fileEngine.client;

import java.io.IOException;

public class AppEntryPoint {
	//TODO: java.nio.file.AccessDeniedException for some directories
	
	public static void main(String[] args) throws IOException {
		System.out.println("starting...");
		Client client;
		if (args.length == 2) {
			client = new SocketClient(args[0], args[1]);
		} else {
			client = new ConsoleClient();
		}
		client.run();
		System.out.println("stoped");
	}

}
