/**
 * Chunk
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import java.util.Collection;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public interface Chunk<T extends Block> {
	boolean isEmpty(Vector3f position);

	void setBlock(T data, Vector3f position);

	T getBlock(Vector3f position);

	void removeBlock(Vector3f position);

	BoundingBox getBoundingBox();

	float getElementSize();

	void clear();

	boolean contains(Vector3f point);

	boolean isDirty();

	void setDirty(boolean dirty);

	Collection<T> getLeaves();
}
