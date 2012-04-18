/**
 * ChunkLoader
 * Author: Matt Teeter
 * Apr 17, 2012
 */
package org.blockworld.level;

import com.jme3.math.Vector2f;

/**
 * @author Matt Teeter
 *
 */
public interface ChunkLoader {
	byte getCellAt(int x, int y, int z, Vector2f chunkCoords, int cellsPerChunk);
}
