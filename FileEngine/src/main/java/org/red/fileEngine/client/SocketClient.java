package org.red.fileEngine.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SocketClient extends Client{

	//TODO: shareexecutors?
	private final static ExecutorService EXECUTOR = Executors.newWorkStealingPool();
	
	private final ServerSocket serverSocket; 
	SocketClient(String port, String path) throws IOException {
		//TODO:
		super(null, null);
		serverSocket = new ServerSocket(8089);
	}
	
	@Override
	void run() {
		System.out.println("started");
		while(true) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				Client client = new Client(out, in);
				EXECUTOR.execute(client::run);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
