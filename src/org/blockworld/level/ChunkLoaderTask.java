/**
 * 
 */
package org.blockworld.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import jme3tools.optimize.GeometryBatchFactory;

import org.blockworld.level.neighbor.BackNeighbor;
import org.blockworld.level.neighbor.BottomNeighbor;
import org.blockworld.level.neighbor.FrontNeighbor;
import org.blockworld.level.neighbor.LeftNeighbor;
import org.blockworld.level.neighbor.Neighbor;
import org.blockworld.level.neighbor.RightNeighbor;
import org.blockworld.level.neighbor.TopNeighbor;
import org.blockworld.util.Noise2;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 * @author matt
 * 
 */
public class ChunkLoaderTask implements Runnable {

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

	@Override
	public void run() {
		// Generate a byte[][][] to hold this chunk's world data.
		byte[][][] data = new byte[cellsPerChunk][cellsPerChunk][cellsPerChunk];
		for (int x = 0; x < cellsPerChunk; x++) {
			long worldX = (long) (x + worldGridCoordinates.x * cellsPerChunk);
			for (int y = 0; y < cellsPerChunk; y++) {
				for (int z = 0; z < cellsPerChunk; z++) {
					long worldZ = (long) (z + worldGridCoordinates.y * cellsPerChunk);
					double noiseX = (((double)worldX + (500)) / 10);
					double noiseY = y / 10;
					double noiseZ = (((double)worldZ + (500)) / 10);
					
					double noiseValue = noiseGenerator.noise(noiseX, noiseY, noiseZ);
					data[x][y][z] = (byte) (noiseValue > 0 ? 1 : 0);
				}
			}
		}
		// Generate Geometries from the world data
		final Collection<Geometry> geometries = new ArrayList<Geometry>();
		for (int x = 0; x < cellsPerChunk; x++) {
			for (int y = 0; y < cellsPerChunk; y++) {
				for (int z = 0; z < cellsPerChunk; z++) {
					if (data[x][y][z] == 0) {
						Collection<Neighbor> neighbors = getNeighbors(data, x, y, z);
						if (!neighbors.isEmpty()) {
							for (Neighbor neighbor : neighbors) {
								geometries.add(neighbor.getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, 1));
							}
						}
					}
					if (data[x][y][z] != 0) {
						if (x == 0) {
							Neighbor leftNeighbor = new LeftNeighbor();
							geometries.add(leftNeighbor.getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, -1));
						} else if (x == cellsPerChunk - 1) {
							geometries.add(new RightNeighbor().getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, -1));
						}
						if (y == cellsPerChunk - 1) {
							geometries.add(new TopNeighbor().getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, -1));
						}
						if (z == 0) {
							geometries.add(new FrontNeighbor().getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, -1));
						} else if (z == cellsPerChunk - 1) {
							geometries.add(new BackNeighbor().getGeometry(x, y, z, worldGridCoordinates, cellsPerChunk, -1));
						}
					}
				}
			}
		}
		data = null;
		if (!geometries.isEmpty()) {
			Mesh mesh = new Mesh();
			GeometryBatchFactory.mergeGeometries(geometries, mesh);
			mesh.updateBound();
			final Geometry chunkGeometry = new Geometry("Chunk(" + worldGridCoordinates.x + "," + worldGridCoordinates.y + ")", mesh);
			chunkGeometry.setMaterial(chunkSet.getMaterial());
			chunkSet.getControl(ChunkSetControl.class).enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					//chunkSet.attachChild(chunkGeometry);
					return null;
				}
			});
		}
	}

	public Collection<Neighbor> getNeighbors(final byte[][][] data, final int x, final int y, final int z) {
		Collection<Neighbor> neighbors = new ArrayList<Neighbor>();

		// TOP
		if ((y < cellsPerChunk - 1 && data[x][y + 1][z] != 0)) {
			neighbors.add(new TopNeighbor());
		}

		// BOTTOM
		if (y > 0 && data[x][y - 1][z] != 0) {
			neighbors.add(new BottomNeighbor());
		}

		// LEFT
		if (x > 0 && data[x - 1][y][z] != 0) {
			neighbors.add(new LeftNeighbor());
		}

		// RIGHT
		if (x < cellsPerChunk - 1 && data[x + 1][y][z] != 0) {
			neighbors.add(new RightNeighbor());
		}

		// FRONT
		if (z < cellsPerChunk - 1 && data[x][y][z + 1] != 0) {
			neighbors.add(new FrontNeighbor());
		}

		// BACK
		if (z > 0 && data[x][y][z - 1] != 0) {
			neighbors.add(new BackNeighbor());
		}

		return neighbors;
	}

}
