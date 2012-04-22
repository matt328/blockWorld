/**
 * PerlinNoiseChunkLoader
 * Author: Matt Teeter
 * Apr 17, 2012
 */
package org.blockworld.level;

import org.blockworld.math.Noise2;

import com.jme3.math.Vector2f;

/**
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseChunkLoader implements ChunkLoader {

	final private Noise2 noiseGenerator;

	public PerlinNoiseChunkLoader(int seed) {
		noiseGenerator = new Noise2(123);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.level.ChunkLoader#getCellAt(int, int, int,
	 * com.jme3.math.Vector2f)
	 */
	@Override
	public byte getCellAt(int x, int y, int z, Vector2f chunkCoords, int cellsPerChunk) {
		long worldX = (long) (x + chunkCoords.x * cellsPerChunk);
		long worldZ = (long) (z + chunkCoords.y * cellsPerChunk);
		double noiseX = (((double) worldX + (500)) / 10);
		double noiseY = y / 10;
		double noiseZ = (((double) worldZ + (500)) / 10);
		double noiseValue = noiseGenerator.noise(noiseX, noiseY, noiseZ);
		return (byte) (noiseValue > 0 ? 1 : 0);
	}

}
