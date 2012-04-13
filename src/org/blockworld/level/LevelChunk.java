package org.blockworld.level;

import java.util.ArrayList;
import java.util.Collection;

import jme3tools.optimize.GeometryBatchFactory;

import org.apache.log4j.Logger;
import org.blockworld.level.neighbor.BackNeighbor;
import org.blockworld.level.neighbor.BottomNeighbor;
import org.blockworld.level.neighbor.FrontNeighbor;
import org.blockworld.level.neighbor.LeftNeighbor;
import org.blockworld.level.neighbor.Neighbor;
import org.blockworld.level.neighbor.RightNeighbor;
import org.blockworld.level.neighbor.TopNeighbor;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class LevelChunk {
	private final byte[] data;
	private final int sizeX, sizeY, sizeZ;
	private final Vector2f chunkPosition;
	private static final Logger log = Logger.getLogger(LevelChunk.class.getName());

	public LevelChunk(final int newSizeX, final int newSizeY, final int newSizeZ, final Vector2f newChunkPosition) {
		sizeX = newSizeX;
		sizeY = newSizeY;
		sizeZ = newSizeZ;
		chunkPosition = newChunkPosition;

		int dataSize = sizeX * sizeY * sizeZ;
		log.debug("Chunk data size: " + dataSize);
		data = new byte[dataSize];
	}

	public LevelChunk(final Vector3f chunkDimensions, final Vector2f newChunkPosition) {
		this((int) chunkDimensions.x, (int) chunkDimensions.y, (int) chunkDimensions.z, newChunkPosition);
	}

	public byte get(final int x, final int y, final int z) {
		final int pos = x + y * sizeX + z * sizeX * sizeY;
		return data[pos];
	}

	public void set(final short val, final int x, final int y, final int z) {
		final int pos = x + y * sizeX + z * sizeX * sizeY;
		data[pos] = (byte) val;
	}

	public int getSizeInBytes() {
		return sizeX * sizeY * sizeZ;
	}

	public Geometry createGeometry() {
		int levelSize = sizeX;
		Collection<Geometry> geometries = new ArrayList<Geometry>();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < levelSize; y++) {
				for (int z = 0; z < levelSize; z++) {
					// If the cell is empty
					if (get(x, y, z) == 0) {
						Collection<Neighbor> neighbors = getNeighbors(x, y, z);
						if (!neighbors.isEmpty()) {
							for (Neighbor neighbor : neighbors) {
								geometries.add(neighbor.getGeometry(x, y, z, chunkPosition, levelSize, 1));
							}
						}
					}
					// If the cell is on the edge and is not empty, fill in
					// whatever edge it is up against
					if (get(x, y, z) != 0) {
						// Left Neighbor
						if(x == 0) {	
							Neighbor leftNeighbor = new LeftNeighbor();
							geometries.add(leftNeighbor.getGeometry(x, y, z, chunkPosition, levelSize, -1));
						} else if (x == levelSize - 1) {
							geometries.add(new RightNeighbor().getGeometry(x, y, z, chunkPosition, levelSize, -1));
						}
						if (y == levelSize - 1) {
							geometries.add(new TopNeighbor().getGeometry(x, y, z, chunkPosition, levelSize, -1));
						}
						if(z == 0) {	
							geometries.add(new FrontNeighbor().getGeometry(x, y, z, chunkPosition, levelSize, -1));
						} else if (z == levelSize - 1) {
							geometries.add(new BackNeighbor().getGeometry(x, y, z, chunkPosition, levelSize, -1));
						}
					}
				}
			}
		}
		if (!geometries.isEmpty()) {
			Mesh mesh = new Mesh();
			GeometryBatchFactory.mergeGeometries(geometries, mesh);
			mesh.updateBound();
			Geometry g = new Geometry("World", mesh);
			return g;
		} else {
			return null;
		}
	}

	public Collection<Neighbor> getNeighbors(final int x, final int y, final int z) {
		Collection<Neighbor> neighbors = new ArrayList<Neighbor>();

		// TOP
		if ((y < sizeY - 1 && get(x, y + 1, z) != 0)) {
			neighbors.add(new TopNeighbor());
		}

		// BOTTOM
		if (y > 0 && get(x, y - 1, z) != 0) {
			neighbors.add(new BottomNeighbor());
		}

		// LEFT
		if (x > 0 && get(x - 1, y, z) != 0) {
			neighbors.add(new LeftNeighbor());
		}

		// RIGHT
		if (x < sizeX - 1 && get(x + 1, y, z) != 0) {
			neighbors.add(new RightNeighbor());
		}

		// FRONT
		if (z < sizeZ - 1 && get(x, y, z + 1) != 0) {
			neighbors.add(new FrontNeighbor());
		}

		// BACK
		if (z > 0 && get(x, y, z - 1) != 0) {
			neighbors.add(new BackNeighbor());
		}

		return neighbors;
	}
}
