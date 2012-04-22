/**
 * TextureAtlas
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.asset;

import gnu.trove.set.hash.TIntHashSet;

import java.util.List;

import org.blockworld.world.node.Face;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;

/**
 * @author Matt Teeter
 * 
 */
public interface TextureAtlas {
	TIntHashSet getTypes();

	Material getMaterial(final int type);

	List<Vector2f> getTextureCoordinates(final int type, Face face);

	boolean isTransparent(int type);
}
