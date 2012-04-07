package com.jTest.main.level;

import junit.framework.Assert;

import org.junit.Test;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.snowFallPeak.blockWorld.level.LevelChunk;
import com.snowFallPeak.blockWorld.level.LevelGenerator;

public class LevelGeneratorTest {

	@Test
	public void testGenerateChunk() {
		final LevelGenerator generator = new LevelGenerator(123, new Vector3f(16, 16, 128));
		final LevelChunk levelChunk = generator.generateChunk(new Vector2f(0, 0));
		Assert.assertNotNull("LevelChunk should not be null.", levelChunk);
	}

}
