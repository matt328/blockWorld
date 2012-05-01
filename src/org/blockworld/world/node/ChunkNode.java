/**
 * ChunkNode
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world.node;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public interface ChunkNode {
	boolean calculate();

	void removeBlock(Vector3f location);

	void setBlock(int blockType, Vector3f location);

	int getBlock(Vector3f location);

	void update(final Vector3f location, final Vector3f direction);
}
