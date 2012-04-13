/**
 * 
 */
package org.blockworld.level.neighbor;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;

/**
 * @author matt
 *
 */
public interface Neighbor {
	Geometry getGeometry(int x, int y, int z, Vector2f chunkPosition, int levelSize, int bias);
}
