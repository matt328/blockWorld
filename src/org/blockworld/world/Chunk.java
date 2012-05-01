/**
 * Chunk
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public interface Chunk {
	boolean isEmpty(Vector3f position);

	void setBlock(int data, Vector3f position);

	int getBlock(Vector3f position);

	void removeBlock(Vector3f position);

	BoundingBox getBoundingBox();

	void clear();

	boolean contains(Vector3f point);

	boolean isDirty();

	void setDirty(boolean dirty);
}
