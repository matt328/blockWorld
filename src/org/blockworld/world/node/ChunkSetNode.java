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

	public ChunkSetNode(AssetManager theAssetManager) throws Exception {
		atlas = new BlockTextureAtlas(theAssetManager);
		createChunk(new Vector3f(00.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(16.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(32.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(48.0f, 64.0f, 00.0f));
		
		createChunk(new Vector3f(00.0f, 64.0f, 16.0f));
		createChunk(new Vector3f(16.0f, 64.0f, 16.0f));
		createChunk(new Vector3f(32.0f, 64.0f, 16.0f));
		createChunk(new Vector3f(48.0f, 64.0f, 16.0f));
		
		createChunk(new Vector3f(00.0f, 64.0f, 32.0f));
		createChunk(new Vector3f(16.0f, 64.0f, 32.0f));
		createChunk(new Vector3f(32.0f, 64.0f, 32.0f));
		createChunk(new Vector3f(48.0f, 64.0f, 16.0f));
	}

	private void createChunk(Vector3f position) {
		BlockChunk<Block> chunk = new BlockChunk<Block>(16, 0.5f, position);
		BlockLoader<BlockChunk<Block>> loader = new TerasologyBlockLoader<BlockChunk<Block>>("M");
		loader.fill(chunk);

		FacesMeshChunkNode terrainNode = new FacesMeshChunkNode(chunk, this, atlas);
		terrainNode.calculate();
		terrainNode.update(Vector3f.ZERO, Vector3f.ZERO);
		attachChild(terrainNode);
	}

	public ChunkNode getChunkNode(Vector3f position) {
		return null;
	}

}
