package org.red.fileEngine.client;

public class AppEntryPoint {
	//TODO: java.nio.file.AccessDeniedException for some directories
	
	public static void main(String[] args) {
		System.out.println("starting...");
		
		//could be added some other way to switch between options
		ConsoleClient client = new SyncClient();
		//ConsoleClient client = new AsyncClient();
		client.run();
		System.out.println("stoped");
	}

}
