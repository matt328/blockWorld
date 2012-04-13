package org.blockworld.level.neighbor;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class FrontNeighbor implements Neighbor {

	@Override
	public Geometry getGeometry(int x, int y, int z, final Vector2f chunkPosition, int levelSize) {
		final Quad q = new Quad(1.0f, 1.0f);
		final Geometry g = new Geometry("Box" + x + y + z, q);
		Vector2f pos = chunkPosition.mult(levelSize);
		g.setLocalTranslation(x + 0.5f + pos.x, (y - levelSize) - 0.5f, z + 0.5f + pos.x);
		g.rotate(0.0f, -FastMath.PI, 0.0f);
		return g;
	}

}
