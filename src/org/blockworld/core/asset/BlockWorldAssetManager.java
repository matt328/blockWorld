/**
 * BlockWorldAssetManager
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.core.asset;

import java.util.List;
import java.util.Set;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;

/**
 * @author Matt Teeter
 *
 */
public interface BlockWorldAssetManager {

	  /**
	   * @return a collection of available bloxel types, not case sensitive
	   */
	  Set<Integer> getBloxelTypes();

	  /**
	   * @param bloxelType
	   *          must not be <code>null</code>
	   * @return the material for the given type
	   */
	  Material getMaterial(final Integer bloxelType);

	  /**
	   * Return the texture coordinates for a bloxel type and a face (left, right, up, down, front and back).
	   * 
	   * @param bloxelType
	   *          must not be <code>null</code>
	   * @param face
	   *          (left, right, up, down, front and back)
	   * @return a list with four {@link Vector2f elements}
	   */
	  List<Vector2f> getTextureCoordinates(final Integer bloxelType, final int face);

	  /**
	   * @param bloxelType
	   *          must not be <code>null</code>
	   * @return <code>true</code> if the bloxel type represents a transparent material else <code>false</code>
	   */
	  boolean isTransparent(Integer bloxelType);
}
