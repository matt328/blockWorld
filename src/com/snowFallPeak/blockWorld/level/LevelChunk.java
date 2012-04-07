package com.snowFallPeak.blockWorld.level;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.jme3.math.Vector3f;
import com.snowFallPeak.blockWorld.level.neighbor.BackNeighbor;
import com.snowFallPeak.blockWorld.level.neighbor.BottomNeighbor;
import com.snowFallPeak.blockWorld.level.neighbor.FrontNeighbor;
import com.snowFallPeak.blockWorld.level.neighbor.LeftNeighbor;
import com.snowFallPeak.blockWorld.level.neighbor.Neighbor;
import com.snowFallPeak.blockWorld.level.neighbor.RightNeighbor;
import com.snowFallPeak.blockWorld.level.neighbor.TopNeighbor;

public class LevelChunk {
	private final short[] data;
	private final int sizeX, sizeY, sizeZ;
	private static final Logger log = Logger.getLogger(LevelChunk.class.getName());

	public LevelChunk(final int newSizeX, final int newSizeY, final int newSizeZ) {
		sizeX = newSizeX;
		sizeY = newSizeY;
		sizeZ = newSizeZ;

		int dataSize = sizeX * sizeY * sizeZ;
		log.debug("Chunk data size: " + dataSize);
		data = new short[dataSize];
	}

	public LevelChunk(final Vector3f chunkDimensions) {
		this((int) chunkDimensions.x, (int) chunkDimensions.y, (int) chunkDimensions.z);
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

	public short get(final int x, final int y, final int z) {
		final int pos = x + y * sizeX + z * sizeX * sizeY;
		return data[pos];
	}

	public void set(final short val, final int x, final int y, final int z) {
		final int pos = x + y * sizeX + z * sizeX * sizeY;
		data[pos] = val;
	}

	public int getSizeInBytes() {
		return sizeX * sizeY * sizeZ * 2;
	}
}
