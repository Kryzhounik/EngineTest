package org.red.fileEngine.engine;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.red.fileEngine.Resources;

public class TreeFinderTest {
	//TODO: move to globals
	
	
	private final static TreeFinder engine = new TreeFinder();
	
	@BeforeAll
	public static void createDefaultTestStructure() throws IOException {
		Assert.assertTrue(Files.exists(Resources.RESOURCE_DIR));
	}
		
	@Test
	public void findTwoInDepth() throws IOException {
		List<Path> files = engine.find(Resources.CORE, "*one*", 2);
		Assert.assertEquals(4, files.size());	
		files.stream()
			.forEach(p -> Assert.assertTrue(p.getFileName().toString().equals("One")));
		
	}
	
	@Test
	public void findOneInDepth() throws IOException {
		List<Path> files = engine.find(Resources.CORE, "*one*", 1);
		Assert.assertEquals(1, files.size());	
		files.stream()
			.forEach(p -> Assert.assertTrue(p.getFileName().toString().equals("One")));
	}
	
	@Test
	public void findFullDepth() throws IOException {
		List<Path> files = engine.find(Resources.CORE, "*one*");
		Assert.assertEquals(13, files.size());	
		files.stream()
			.forEach(p -> Assert.assertTrue(p.getFileName().toString().equals("One")));
	}
	
	@Test
	public void findNothing() throws IOException {
		List<Path> files = engine.find(Resources.CORE, "*x*");
		Assert.assertEquals(0, files.size());	
	}
	
	@Test
	// i'm not sure that root should being considered, but i'm not sure in opposite as well...
	public void findTwoCases() throws IOException {
		List<Path> files = engine.find(Resources.CORE, "*r*", 2);
		Assert.assertEquals(9, files.size(), 2);	
		files.stream()
			.peek(System.out::println)
			.map(Path::getFileName)
			.map(Path::toString)
			.forEach(s -> Assert.assertTrue(s.equals("Three") || s.equals("Four") || s.equals("TestFileTree")));
	}

}
