package com.snowFallPeak.blockWorld.level;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.snowFallPeak.blockWorld.util.Noise2;

public class LevelGenerator {
	private final int seed;
	private final Noise2 noiseMaker;
	private final Vector3f chunkDimensions;

	public LevelGenerator(final int newSeed, final Vector3f newChunkDimensions) {
		seed = newSeed;
		noiseMaker = new Noise2(seed);
		chunkDimensions = newChunkDimensions;
	}

	public LevelChunk generateChunk(final Vector2f chunkPosition) {
		final LevelChunk chunk = new LevelChunk(chunkDimensions);
		final double xStart = chunkPosition.x * chunkDimensions.x;
		final double yStart = chunkPosition.y * chunkDimensions.y;
		int cx = 0;
		for (double x = xStart; x < xStart + chunkDimensions.x; x++) {
			int cy = 0;
			for (double y = yStart; y < yStart + chunkDimensions.y; y++) {
				int cz = 0;
				for (double z = 0; z < chunkDimensions.z; z++) {
					final double noise = noiseMaker.noise(x / 10, y / 10, z / 10);
					//System.out.println("noise(" + x / 10 + ", " + y / 10 + ", " + z / 10 + "): " + noise);
					final short val = (short) (noise > 0 ? 1 : 0);
					chunk.set(val, cx, cy, cz);
					cz++;
				}
				cy++;
			}
			cx++;
		}
		return chunk;
	}
}
