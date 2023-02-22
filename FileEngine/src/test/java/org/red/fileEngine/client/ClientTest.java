package org.red.fileEngine.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.red.fileEngine.Resources;

public class ClientTest {
	@Test
	// i simple copied one of the TreeFinder tests. it possible to implement it by running all cases from it thought Client
	public void asycRun() throws IOException, InterruptedException{
		
		
		PipedOutputStream out = new PipedOutputStream();
		InputStream testIn = new PipedInputStream(out);
		
		PipedInputStream in = new PipedInputStream();
		OutputStream testOut = new PipedOutputStream(in);	
	
		Client client = new Client(testOut, testIn, BehaviorStrategy.ASYNC);
		Executors.newSingleThreadExecutor().execute(client::run); 

		PrintWriter writer = new PrintWriter(out);
		String cmd = Resources.CORE + " *one* 2";
		
		writer.println(cmd);
		writer.flush();
		
		Thread.sleep(100);
		
		List<String> lines = new LinkedList<>();
		Scanner scanner = new Scanner(in);
		while (true) {
			String line = scanner.nextLine();
			if (!BehaviorStrategy.FINISH_MARKER.equals(line)) {
				lines.add(line);
			} else {
				break;
			}
		}
		scanner.close();
		
		writer.println(Client.EXIT_CODE);
		writer.flush();
		
		long count = lines.stream().
			filter(s -> s.contains("One"))
			.count();
		
		Assert.assertEquals(4, count);	
	}
}
