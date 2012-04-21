/**
 * ChunkNode
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.node;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public interface ChunkNode {

	boolean calculate();

	void removeBloxel(Vector3f theLocation);

	void setBloxel(final Vector3f theLocation, final int theBoxelType);

	void update(final Vector3f theLocation, final Vector3f theDirection);
}
