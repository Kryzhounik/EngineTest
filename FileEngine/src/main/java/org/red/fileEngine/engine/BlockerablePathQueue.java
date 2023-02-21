package org.red.fileEngine.engine;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

//TODO: isn't best implementation.
// it's better to implement by some "closed" flag, but it will be more complex implementation, so for my purposes this is enough
public class BlockerablePathQueue {
	private final static Path POISON_PILL = Path.of("UTIL", "POISON", "PILL", "PATH");

	private final BlockingQueue<Path> delegate;;


	BlockerablePathQueue(LinkedBlockingQueue<Path> delegate) {
		super();
		this.delegate = delegate;
	}


	//TODO: multithread
	public void blockingforEach(Consumer<Path> consumer) throws InterruptedException {
		while (true) {
			Path elem = delegate.take();
			if (elem == POISON_PILL) {
				break;
			}
			consumer.accept(elem);
		}
	}


	void close(){
		delegate.add(POISON_PILL);
	}

}
