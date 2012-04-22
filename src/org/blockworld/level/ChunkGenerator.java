package org.blockworld.level;

import java.util.logging.Logger;

import org.blockworld.math.Noise2;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class ChunkGenerator {
	Logger log = Logger.getLogger(getClass().getName());
	private final int seed;
	private final Noise2 noiseMaker;
	private final Vector3f chunkDimensions;

	public ChunkGenerator(final int newSeed, final Vector3f newChunkDimensions) {
		seed = newSeed;
		noiseMaker = new Noise2(seed);
		chunkDimensions = newChunkDimensions;
	}

	public Chunk generateChunk(final Vector2f worldCoordinates) {
		final Chunk chunk = new Chunk((int) chunkDimensions.x, worldCoordinates);
		final double xStart = worldCoordinates.x * chunkDimensions.x;
		final double zStart = worldCoordinates.y * chunkDimensions.z;
		
		//log.log(Level.FINEST, "Creating Chunk xStart={0}, yStart={1}", new Object[] {xStart, yStart});
		
		int cx = 0;
		for (double x = xStart; x < xStart + chunkDimensions.x; x++) {
			int cy = 0;
			for (double y = 0; y < chunkDimensions.y; y++) {
				int cz = 0;
				for (double z = zStart; z < zStart + chunkDimensions.z; z++) {
					final double noise = noiseMaker.noise(x / 10, y / 10, z / 10);
					final short val = (short) (noise > 0 ? 1 : 0);
					chunk.set((byte)val, cx, cy, cz);
					cz++;
				}
				cy++;
			}
			cx++;
		}
		return chunk;
	}
}
