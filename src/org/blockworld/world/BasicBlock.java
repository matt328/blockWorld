/**
 * Cell
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class BasicBlock implements Block {
	private final int type;
	private final Vector3f center;
	private final float dimension;

	/**
	 * @param type
	 * @param center
	 */
	public BasicBlock(final int type, final float blockSize, final Vector3f center) {
		this.type = type;
		this.center = center;
		this.dimension = blockSize;
	}

	@Override
	public int getType() {
		return type;
	}

	public Vector3f getCenter() {
		return center;
	}

	@Override
	public float getDimension() {
		return dimension;
	}

}
