package com.jTest.main.level;

import junit.framework.Assert;

import org.blockworld.level.Chunk;
import org.blockworld.level.ChunkGenerator;
import org.junit.Test;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class LevelGeneratorTest {

	@Test
	public void testGenerateChunk() {
		final ChunkGenerator generator = new ChunkGenerator(123, new Vector3f(16, 16, 128));
		final Chunk levelChunk = generator.generateChunk(new Vector2f(0, 0));
		Assert.assertNotNull("LevelChunk should not be null.", levelChunk);
	}

}
