/**
 * Chunk
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * A {@link Chunk} is a section of the terrain composed of single blocks. A
 * {@link Chunk} represents only the logical data composing a chunk that will be
 * tesselated into a {@link Geometry} to be rendered by JME.
 * 
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

	/**
	 * @param position
	 */
	boolean hasBlock(Vector3f position);
}
