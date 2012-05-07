/**
 * MeshChunkNode
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.world.node;

import java.util.List;

import org.blockworld.world.Chunk;

import com.jme3.scene.Geometry;

/**
 * @author Matt Teeter
 *
 */
public class MeshChunkNode extends AbstractChunkNode {

	public MeshChunkNode(Chunk terrainChunk) {
		super(terrainChunk);
	}

	@Override
	protected List<Geometry> createGeometries() {
		// TODO Auto-generated method stub
		return null;
	}

}
