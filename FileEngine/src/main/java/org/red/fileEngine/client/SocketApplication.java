package org.red.fileEngine.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SocketApplication implements Application{

	//TODO: shareexecutors?
	private final static ExecutorService CLIENT_EXECUTOR = Executors.newWorkStealingPool();
	private final Path path;
	
	private final ServerSocket serverSocket; 
	SocketApplication(String port, String path) throws IOException {
		int portNum = Integer.valueOf(port);
		serverSocket = new ServerSocket(portNum);
		this.path = Path.of(path);
	}
	
	@Override
	public void start() {
		BehaviorStrategy.setPath(path);
		System.out.println("started");
		while(true) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				Client client = new Client(out, in, BehaviorStrategy.SYNC_CACHED);
				CLIENT_EXECUTOR.execute(client::run);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
