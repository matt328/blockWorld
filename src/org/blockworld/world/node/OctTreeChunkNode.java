/**
 * OctTreeChunkNode
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.world.node;

import java.util.List;

import org.blockworld.world.Block;
import org.blockworld.world.OctTreeChunk;

import com.jme3.scene.Geometry;

/**
 * @author Matt Teeter
 * 
 */
public class OctTreeChunkNode extends AbstractChunkNode {

	public OctTreeChunkNode(OctTreeChunk<Block> terrainChunk) {
		super(terrainChunk);
	}

	@Override
	protected List<Geometry> createGeometries() {
		return null;
	}

}
