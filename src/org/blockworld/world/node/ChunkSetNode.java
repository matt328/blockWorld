/**
 * ChunkSetNode
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import org.blockworld.asset.BlockTextureAtlas;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.world.Block;
import org.blockworld.world.BlockChunk;
import org.blockworld.world.loader.BlockLoader;
import org.blockworld.world.loader.TerasologyBlockLoader;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkSetNode extends Node {
	private final TextureAtlas atlas;
	
	/**
	 * @param singleNode
	 */
	public ChunkSetNode(AssetManager theAssetManager) {
		atlas = new BlockTextureAtlas(theAssetManager);
		for (int i = 0; i < 9; i++) {
			BlockChunk<Block> chunk = new BlockChunk<Block>(32, 1.0f, Vector3f.ZERO);
			BlockLoader<BlockChunk<Block>> loader = new TerasologyBlockLoader<BlockChunk<Block>>("Matt Teeter");
			loader.fill(chunk);

			FacesMeshChunkNode terrainNode = new FacesMeshChunkNode(chunk, this, atlas);
			attachChild(terrainNode);
		}
	}
	
	public ChunkNode getChunkNode(Vector3f position) {
		return null;
	}

}
