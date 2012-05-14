/**
 * WorldGrid
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.util;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class WorldGrid {

	public static Vector3f worldCoordsToGridCoords(int chunkDimension, Vector3f location) {
		Vector3f translated = new Vector3f(translate(location.x, chunkDimension), location.y, translate(location.z, chunkDimension));
		return translated;
	}

	public static float translate(float i, int d) {
		int halfd = d / 2;
		float hb0 = (int) (i / halfd);
		if (hb0 % 2 == 0) {
			hb0 *= halfd;
		} else {
			hb0 += hb0 < 0 ? -1 : 1;
			hb0 *= halfd;
		}
		return hb0;
	}

	public static Collection<Vector3f> getSurroundingChunkPositions(Vector3f location, int radius, Vector3f chunkDimensions) {
		Vector3f centerChunkPosition = new Vector3f(translate(location.x, (int) chunkDimensions.x), location.y, translate(location.z, (int) chunkDimensions.z));

		Collection<Vector3f> positions = Lists.newArrayList();

		int xStart = (int) (centerChunkPosition.x - (chunkDimensions.x * radius));
		int zStart = (int) (centerChunkPosition.z - (chunkDimensions.z * radius));
		int xEnd = (int) (centerChunkPosition.x + (chunkDimensions.x * radius));
		int zEnd = (int) (centerChunkPosition.z + (chunkDimensions.z * radius));

		for (int x = xStart; x <= xEnd; x += chunkDimensions.x) {
			for (int z = zStart; z <= zEnd; z += chunkDimensions.z) {
				positions.add(new Vector3f(x, 0, z));
			}
		}

		return positions;
	}

	public static Collection<Vector3f> getOffsetsForRadius(final int radius) {
		Collection<Vector3f> offsets = Lists.newArrayList();
		offsets.add(Vector3f.ZERO);
		for (int x = -radius; x < radius; x++) {
			for (int z = -radius; z < radius; z++) {
				offsets.add(new Vector3f(x, 0, z));
			}
		}
		return offsets;
	}
}
