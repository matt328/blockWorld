/**
 * WorldGrid
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.util;

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
}
