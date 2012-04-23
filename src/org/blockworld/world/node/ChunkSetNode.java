/**
 * ChunkSetNode
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import java.util.Map;

import org.apache.commons.lang3.Range;
import org.blockworld.asset.BlockTextureAtlas;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.world.Block;
import org.blockworld.world.BlockChunk;
import org.blockworld.world.loader.BlockLoader;
import org.blockworld.world.loader.TerasologyBlockLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkSetNode extends Node {
	private static final int CHUNK_DIMENSION = 16;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ChunkSetNode.class);
	private final TextureAtlas atlas;
	private final Map<Vector3f, ChunkNode> loadedChunks;

	public ChunkSetNode(AssetManager theAssetManager) throws Exception {
		atlas = new BlockTextureAtlas(theAssetManager);
		loadedChunks = Maps.newHashMap();

		createChunk(new Vector3f(00.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(00.0f, 96.0f, 00.0f));
		createChunk(new Vector3f(16.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(16.0f, 96.0f, 00.0f));
		createChunk(new Vector3f(32.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(32.0f, 96.0f, 00.0f));
		createChunk(new Vector3f(48.0f, 64.0f, 00.0f));
		createChunk(new Vector3f(48.0f, 96.0f, 00.0f));

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
		BlockChunk<Block> chunk = new BlockChunk<Block>(CHUNK_DIMENSION, 0.5f, position);
		BlockLoader<BlockChunk<Block>> loader = new TerasologyBlockLoader<BlockChunk<Block>>("M");
		loader.fill(chunk);

		FacesMeshChunkNode terrainNode = new FacesMeshChunkNode(chunk, this, atlas);
		terrainNode.calculate();
		terrainNode.update(Vector3f.ZERO, Vector3f.ZERO);
		loadedChunks.put(position, terrainNode);
		attachChild(terrainNode);
	}

	/**
	 * Translate a world location into chunk location.
	 * 
	 * @param aLocation
	 *            to use
	 * @param theChunkSize
	 *            to use
	 * @return the location of a {@link BloxelChunk} which can handle the given <code>aLocation</code>
	 */
	private Vector3f calcChunkLocation(final Vector3f aLocation) {
		final int x = getChunkIndexForAxis((int) aLocation.x);
		final int y = getChunkIndexForAxis((int) aLocation.y);
		final int z = getChunkIndexForAxis((int) aLocation.z);
		return new Vector3f(x, y, z).mult(2 * CHUNK_DIMENSION);
	}

	private int getChunkIndexForAxis(final Integer theChunkLocation) {
		int i = 0;
		int sign = 1;
		final Range<Integer> zeroChunkRange = Range.between(-CHUNK_DIMENSION, CHUNK_DIMENSION);
		if (!(zeroChunkRange.contains(theChunkLocation))) {
			if (theChunkLocation < 0) {
				sign = -1;
			}
			i = (((int) ((Math.abs(theChunkLocation) - CHUNK_DIMENSION) / (2 * CHUNK_DIMENSION))) + 1) * sign;
		}
		return i;
	}

	public ChunkNode getChunkNode(Vector3f position) {
		Vector3f chunkPosition = calcChunkLocation(position);
		return loadedChunks.get(chunkPosition);
	}

}
