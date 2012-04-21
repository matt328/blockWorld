/**
 * Block
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world;

import com.google.common.base.Objects;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class Block {
	private final Vector3f center;
	private final float dimension;
	private final int type;

	/**
	 * @param center
	 * @param dimension
	 */
	public Block(Vector3f center, float dimension, final int type) {
		this.center = center;
		this.dimension = dimension;
		this.type = type;
	}

	public Vector3f getCenter() {
		return center;
	}

	public float getDimension() {
		return dimension;
	}

	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(center, dimension, type);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Block) {
			Block that = (Block) object;
			return Objects.equal(this.center, that.center) && Objects.equal(this.dimension, that.dimension) && Objects.equal(this.type, that.type);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("center", center).add("dimension", dimension).add("type", type).toString();
	}

}
