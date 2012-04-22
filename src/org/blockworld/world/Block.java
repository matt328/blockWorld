/**
 * BlockInterface
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public interface Block {
	int getType();

	Vector3f getCenter();

	float getDimension();

	float getBlockSize();
}
