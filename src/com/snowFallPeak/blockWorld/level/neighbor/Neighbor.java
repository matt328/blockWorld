/**
 * 
 */
package com.snowFallPeak.blockWorld.level.neighbor;

import com.jme3.scene.Geometry;

/**
 * @author matt
 *
 */
public interface Neighbor {
	Geometry getGeometry(int x, int y, int z, int levelSize);
}
