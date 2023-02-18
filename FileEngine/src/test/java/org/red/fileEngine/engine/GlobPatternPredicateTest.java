package org.red.fileEngine.engine;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import org.junit.Assert;

//TODO: could be extended. now there only is the simplest cases
public class GlobPatternPredicateTest {
	@Test
	public void match(){
		GlobPatternPredicate predicate = new GlobPatternPredicate("*at*");
		Path path = Path.of("matter");
		boolean match = predicate.test(path);
		Assert.assertTrue(match);
	}
	
	@Test
	public void CapitalMatch(){
		GlobPatternPredicate predicate = new GlobPatternPredicate("*at*");
		Path path = Path.of("mAtter");
		boolean match = predicate.test(path);
		Assert.assertTrue(match);
	}
	
	@Test
	public void endMatch(){
		GlobPatternPredicate predicate = new GlobPatternPredicate("*at*");
		Path path = Path.of("mat");
		boolean match = predicate.test(path);
		Assert.assertTrue(match);
	}
	
	@Test
	public void noMatch(){
		GlobPatternPredicate predicate = new GlobPatternPredicate("*at*");
		Path path = Path.of("mttaer");
		boolean match = predicate.test(path);
		Assert.assertFalse(match);
	}
}
