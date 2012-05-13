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
		Vector3f translated = new Vector3f(translate(location.x, chunkDimension), translate(location.y, chunkDimension), translate(location.z, chunkDimension));
		return translated.mult(chunkDimension * 2);
	}

	private static float translate(float i, int d) {
		int d2 = d * 2;
		int dMinusOne = d - 1;
		return (int) ((i - d) / d2) + (i > dMinusOne ? 1 : 0);
	}

	public static Collection<Vector3f> getSurroundingChunkPositions(Vector3f location, int radius, Vector3f chunkDimensions) {
		Vector3f translated = new Vector3f(translate(location.x, (int) chunkDimensions.x), location.y, translate(location.z, (int) chunkDimensions.z));
		Collection<Vector3f> offsets = getOffsetsForRadius(radius);
		Collection<Vector3f> positions = Lists.newArrayListWithExpectedSize(offsets.size());
		for (Vector3f offset : offsets) {
			float x = offset.x + translated.x;
			float y = offset.y;
			float z = offset.z + translated.z;
			positions.add(new Vector3f(x, y, z));
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
