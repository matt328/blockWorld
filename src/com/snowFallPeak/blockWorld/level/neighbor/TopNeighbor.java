/**
 * 
 */
package com.snowFallPeak.blockWorld.level.neighbor;

import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * @author matt
 *
 */
public class TopNeighbor implements Neighbor {

	@Override
	public Geometry getGeometry(int x, int y, int z, int levelSize) {
		final Quad q = new Quad(1.0f, 1.0f);
		final Geometry g = new Geometry("Box" + x + y + z, q);
		g.setLocalTranslation(x - 0.5f, (y - levelSize) + 0.5f, z - 0.5f);
		g.rotate(FastMath.HALF_PI, 0.0f, 0.0f);
		return g;
	}

}
