/**
 * BlockVolume
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world;

import com.google.common.base.Equivalence;
import com.jme3.bounding.BoundingBox;

/**
 * @author Matt Teeter
 *
 */
public interface BlockVolume<T> {
	void clear();
	boolean contains(float x, float y, float z);
	T getBlock(float x, float y, float z);
	BoundingBox getBoundingBox();
	float getElementSize();
	boolean isEmpty(float x, float y, float z);
	void pack(Equivalence<T> equivalence);
	void removeBlock(float x, float y, float z);
	void setBlock(float x, float y, float z, T data);
	int size();
}
