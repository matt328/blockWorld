/**
 * WorldNode
 * Author: Matt Teeter
 * May 12, 2012
 */
package org.blockworld.world.node;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.blockworld.asset.BlockTextureAtlas;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.util.WorldGrid;
import org.blockworld.world.BasicChunk;
import org.blockworld.world.Chunk;
import org.blockworld.world.loader.BlockworldBlockLoader;
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
public class WorldNode extends Node {
	private static final Logger LOG = LoggerFactory.getLogger(WorldNode.class);
	private final TextureAtlas atlas;
	private final BlockworldBlockLoader loader;
	private final Map<Vector3f, AbstractChunkNode> loadedChunks;
	private final Vector3f chunkDimensions;
	private final int chunkRadius;

	private final ExecutorService threadPool;
	private final BlockingQueue<AbstractChunkNode> chunksToFill;
	private final BlockingQueue<AbstractChunkNode> chunksToCalculate;
	private final BlockingQueue<AbstractChunkNode> chunksToAdd;

	public WorldNode(final int chunkRadius, Vector3f chunkDimensions, AssetManager theAssetManager) throws Exception {
		this.chunkRadius = chunkRadius;
		this.chunkDimensions = chunkDimensions;
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
		loadedChunks = Maps.newHashMap();
		loader = new BlockworldBlockLoader();

		threadPool.execute(new FillChunkDispatcher());
		threadPool.execute(new CalculateChunkDispatcher());
	}

	private void fillChunk(AbstractChunkNode node, BlockworldBlockLoader loader, BlockingQueue<AbstractChunkNode> resultQueue) {
		loader.fill(node.getTerrainChunk());
		node.calculate();
		resultQueue.add(node);
	}

	public void update(Vector3f location, Vector3f direction) {
		AbstractChunkNode node = chunksToAdd.poll();
		if (node != null) {
			attachChild(node);
		}
		for (Vector3f v : WorldGrid.getSurroundingChunkPositions(location, chunkRadius, chunkDimensions)) {
			createNewChunkNode(v);
		}
	}

	public void createNewChunkNode(Vector3f position) {
		if (!loadedChunks.containsKey(position)) {
			Chunk chunk = new BasicChunk(chunkDimensions, position);
			MeshChunkNode chunkNode = new MeshChunkNode(chunk, atlas, loader);
			loadedChunks.put(position, chunkNode);
			chunksToFill.add(chunkNode);
		}
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
}
