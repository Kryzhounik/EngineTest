package org.red.fileEngine.client;

class ConsoleApplication implements Application{
	@Override
	public void start() {
		Client client = new Client(System.out, System.in);
		client.run();
	}
}
