/**
 * TextureAtlas
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.asset;

import java.util.List;
import java.util.Set;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;

/**
 * @author Matt Teeter
 * 
 */
public interface TextureAtlas {
	/**
	 * @return a collection of available block types, not case sensitive
	 */
	Set<Integer> getBlockTypes();

	/**
	 * @param blockType
	 *            must not be <code>null</code>
	 * @return the material for the given type
	 */
	Material getMaterial(final Integer blockType);

	/**
	 * Return the texture coordinates for a block type and a face (left, right, up, down, front and back).
	 * 
	 * @param blockType
	 *            must not be <code>null</code>
	 * @param face
	 *            (left, right, up, down, front and back)
	 * @return a list with four {@link Vector2f elements}
	 */
	List<Vector2f> getTextureCoordinates(final Integer blockType, final int face);

	/**
	 * @param blockType
	 *            must not be <code>null</code>
	 * @return <code>true</code> if the block type represents a transparent material else <code>false</code>
	 */
	boolean isTransparent(Integer blockType);

	public Material getRedMaterial();

	public Material getBlueMaterial();

}
