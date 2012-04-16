/**
 * 
 */
package org.blockworld.level.neighbor;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * @author matt
 *
 */
public class LeftNeighbor implements Neighbor {

	/* (non-Javadoc)
	 * @see org.blockworld.level.neighbor.Neighbor#getGeometry(int, int, int, int)
	 */
	@Override
	public Geometry getGeometry(float x, float y, float z, final Vector2f chunkPosition, int levelSize, int bias) {
		final Quad q = new Quad(1.0f, 1.0f);
		final Geometry g = new Geometry("Box" + x + y + z, q);
		Vector2f pos = chunkPosition.mult(levelSize);
		g.setLocalTranslation(x - 0.5f + pos.x, (y - levelSize) - 0.5f, z + (0.5f * bias) + pos.y);
		g.rotate(0.0f, FastMath.HALF_PI * bias, 0.0f);
		return g;
	}

}
