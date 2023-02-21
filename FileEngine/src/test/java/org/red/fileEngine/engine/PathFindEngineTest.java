package org.red.fileEngine.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.red.fileEngine.Resources;

public class PathFindEngineTest {
	@Test
	public void test() throws IOException, InterruptedException {
		CachedFindEngine engine = new CachedFindEngine(Resources.CORE);

		//untill some block on engine start isn't implemented
		Thread.sleep(1000);
		
		List<Path> files = engine.find("*one*", 2);
		Assert.assertEquals(4, files.size());
		files.stream().forEach(p -> Assert.assertTrue(p.getFileName().toString().equals("One")));
	}
}
