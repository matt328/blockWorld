/**
 * 
 */
package org.blockworld.level;

import java.util.concurrent.Callable;

import org.blockworld.math.Noise2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Vector2f;

/**
 * @author matt
 * 
 */
public class ChunkLoaderTask implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ChunkLoaderTask.class);
	private final ChunkSet chunkSet;
	private final Vector2f worldGridCoordinates;
	private final Noise2 noiseGenerator;
	private final int cellsPerChunk;

	public ChunkLoaderTask(ChunkSet chunkSet, Vector2f worldCoordinates, Noise2 noiseGenerator, int cellsPerChunk) {
		this.chunkSet = chunkSet;
		this.worldGridCoordinates = worldCoordinates;
		this.noiseGenerator = noiseGenerator;
		this.cellsPerChunk = cellsPerChunk;
	}

	private byte getNoise(int x, int y, int z) {
		long worldX = (long) (x + worldGridCoordinates.x * cellsPerChunk);
		long worldZ = (long) (z + worldGridCoordinates.y * cellsPerChunk);
		double noiseX = (((double) worldX + (500)) / 10);
		double noiseY = y / 10;
		double noiseZ = (((double) worldZ + (500)) / 10);
		double noiseValue = noiseGenerator.noise(noiseX, noiseY, noiseZ);
		return (byte)(noiseValue > 0 ? 1 : 0);
	}

	@Override
	public void run() {
		final Chunk chunk = new Chunk(cellsPerChunk, worldGridCoordinates);
		for (int x = 0; x < cellsPerChunk; x++) {
			for (int y = 0; y < cellsPerChunk; y++) {
				for (int z = 0; z < cellsPerChunk; z++) {
					chunk.set(getNoise(x, y, z), x, y, z);
				}
			}
		}
		LOG.debug("Loading chunk: " + chunk.getName() + " size in bytes: " + chunk.getSizeInBytes());
		long start = System.currentTimeMillis();
		chunk.generateMesh();
		long stop = System.currentTimeMillis();
		LOG.debug("Chunk Generated in " + (stop - start) + "ms");
		chunk.setMaterial(chunkSet.getMaterial());
		LOG.debug("Generated Mesh");
		chunkSet.getControl(ChunkSetControl.class).enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				chunkSet.attachChild(chunk);
				return null;
			}
		});
	}
}
