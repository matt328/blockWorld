/**
 * ChunkSetNode
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.Range;
import org.blockworld.asset.BlockTextureAtlas;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.util.WorldGrid;
import org.blockworld.world.Chunk;
import org.blockworld.world.HashMapChunk;
import org.blockworld.world.loader.BlockLoader;
import org.blockworld.world.loader.DummyBlockLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkSetNode extends Node {
	private static final int CHUNK_DIMENSION = 8;
	private static final int TWO_CHUNK_DIMENSION = 2 * CHUNK_DIMENSION;
	private static final Logger LOG = LoggerFactory.getLogger(ChunkSetNode.class);
	private final TextureAtlas atlas;
	private final Map<Vector3f, AbstractChunkNode> createdChunks;
	private final BlockLoader<Chunk> loader;

	private static final List<Vector3f> offsets = Lists.newArrayList(new Vector3f[] { new Vector3f(TWO_CHUNK_DIMENSION, 0.0f, TWO_CHUNK_DIMENSION), new Vector3f(TWO_CHUNK_DIMENSION, 0.0f, 0.0f), new Vector3f(TWO_CHUNK_DIMENSION, 0.0f, -TWO_CHUNK_DIMENSION),

	new Vector3f(0.0f, 0.0f, TWO_CHUNK_DIMENSION), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, -TWO_CHUNK_DIMENSION),

	new Vector3f(-TWO_CHUNK_DIMENSION, 0.0f, -TWO_CHUNK_DIMENSION), new Vector3f(-TWO_CHUNK_DIMENSION, 0.0f, 0.0f), new Vector3f(-TWO_CHUNK_DIMENSION, 0.0f, -TWO_CHUNK_DIMENSION) });

	private final ExecutorService threadPool;
	private final BlockingQueue<AbstractChunkNode> chunksToFill;
	private final BlockingQueue<AbstractChunkNode> chunksToCalculate;
	private final BlockingQueue<AbstractChunkNode> chunksToAdd;

	public ChunkSetNode(AssetManager theAssetManager) throws Exception {
		threadPool = Executors.newFixedThreadPool(8, new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				final Thread th = new Thread(r);
				th.setDaemon(true);
				return th;
			}
		});
		chunksToFill = new LinkedBlockingQueue<AbstractChunkNode>(15);
		chunksToCalculate = new LinkedBlockingQueue<AbstractChunkNode>(15);
		chunksToAdd = new LinkedBlockingQueue<AbstractChunkNode>(15);
		atlas = new BlockTextureAtlas(theAssetManager);
		createdChunks = Maps.newHashMap();
		loader = new DummyBlockLoader<Chunk>("M");

		threadPool.execute(new FillChunkDispatcher());
		threadPool.execute(new CalculateChunkDispatcher());
	}

	private class FillChunkDispatcher implements Runnable {
		@Override
		public void run() {
			LOG.debug("Fired up Fill Chunk Dispatcher");
			while (true) {
				try {
					AbstractChunkNode node = chunksToFill.take();
					threadPool.execute(new FillChunkThread(node));
				} catch (InterruptedException iex) {
					LOG.debug("Fill Chunk Dispatcher shutting down");
					break;
				}
			}
		}
	}

	private class FillChunkThread implements Runnable {
		private final AbstractChunkNode node;

		public FillChunkThread(AbstractChunkNode node) {
			this.node = node;
		}

		@Override
		public void run() {
			fillChunk(node, loader, chunksToCalculate);
		}
	}

	private class CalculateChunkDispatcher implements Runnable {
		@Override
		public void run() {
			LOG.debug("Calculate Chunk Dispatcher Fired up");
			while (true) {
				try {
					AbstractChunkNode node = chunksToCalculate.take();
					threadPool.execute(new CalculateChunkThread(node));
				} catch (InterruptedException iex) {
					LOG.debug("Calculate Chunk Dispatcher Shutdown");
					break;
				}
			}
		}
	}

	private class CalculateChunkThread implements Runnable {
		private AbstractChunkNode node;

		public CalculateChunkThread(AbstractChunkNode node) {
			this.node = node;
		}

		@Override
		public void run() {
			LOG.debug("Calculating Chunk: " + node.toString());
			node.calculate();
			chunksToAdd.add(node);
		}
	}

	private void fillChunk(AbstractChunkNode node, BlockLoader<Chunk> loader, BlockingQueue<AbstractChunkNode> resultQueue) {
		LOG.debug("Filling chunk");
		loader.fill(node.getTerrainChunk());
		LOG.debug("Filled chunk");
		node.calculate();
		LOG.debug("Calculated chunk");
		resultQueue.add(node);
	}

	public void update(Vector3f location, Vector3f direction) {
		AbstractChunkNode node = chunksToAdd.poll();
		if (node != null) {
			attachChild(node);
		}
		for (Vector3f v : getSurroundingChunkPositions(location)) {
			createNewChunkNode(v);
		}
	}

	private static final List<Vector3f> getSurroundingChunkPositions(Vector3f location) {
		Vector3f gridLocation = WorldGrid.worldCoordsToGridCoords(CHUNK_DIMENSION, location);
		List<Vector3f> surrounding = Lists.newArrayList();
		for (Vector3f v : offsets) {
			surrounding.add(v.add(gridLocation));
		}
		return surrounding;
	}

	public static Vector3f globalLocationToGridLocation(Vector3f location) {
		return new Vector3f(translate(location.x), translate(location.y), translate(location.z));
	}

	/**
	 * @param x
	 */
	private static float translate(float x) {
		float chunkX = x / (CHUNK_DIMENSION * 2);
		int iChunkX = (int) chunkX;
		return iChunkX + (CHUNK_DIMENSION * 2) + CHUNK_DIMENSION;
	}

	public void createNewChunkNode(Vector3f position) {
		if (!createdChunks.containsKey(position)) {
			LOG.debug("Creating Chunk: " + position);
			Chunk chunk = new HashMapChunk(CHUNK_DIMENSION, 256, position);
			FacesMeshChunkNode chunkNode = new FacesMeshChunkNode(chunk, this, atlas);
			createdChunks.put(position, chunkNode);
			chunksToFill.add(chunkNode);
		}
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
		return createdChunks.get(chunkPosition);
	}

}
