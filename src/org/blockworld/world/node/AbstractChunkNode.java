/**
 * AbstractChunkNode
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world.node;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.blockworld.world.BasicBlock;
import org.blockworld.world.Block;
import org.blockworld.world.BlockChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * A base for different types of {@link ChunkNode}s. It provides functionality that will not change for different implementations of {@link ChunkNode}. It exposes the createGeometries() method for implementation in child classes to different tesselation algorithms.
 * 
 * @author Matt Teeter
 * 
 */
public abstract class AbstractChunkNode extends Node implements ChunkNode {
	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	enum ChunkState {
		NEW, CALCULATED, UP2DATE, DIRTY
	}

	protected final BlockChunk<Block> terrainChunk;
	protected final List<Spatial> geometries;
	protected ChunkState state = ChunkState.NEW;
	protected final Lock updateLock;

	protected abstract List<Spatial> createGeometries();

	/**
	 * Create a {@link ChunkNode}, supplying the {@link BlockChunk} that will serve as its geometry source.
	 * 
	 * @param terrainChunk
	 *            - a BlockChunk that this {@link ChunkNode} will 'wrap' and create {@link Geometry} for in order to be rendered by JME.
	 */
	public AbstractChunkNode(final BlockChunk<Block> terrainChunk) {
		super(BlockChunk.createName(terrainChunk));
		this.terrainChunk = terrainChunk;
		updateLock = new ReentrantLock();
		geometries = Lists.newArrayList();
	}

	public BlockChunk<Block> getTerrainChunk() {
		return terrainChunk;
	}

	@Override
	public Block getBlock(Vector3f location) {
		return terrainChunk.getBlock(location);
	}

	public ChunkState getState() {
		return state;
	}

	/**
	 * Calculates the {@link ChunkNode}'s {@link Geometry}. Performs basic checks to see if the Chunk is dirty, and if not, defers geometry creation to the abstract method, createGeometries().
	 * 
	 * This function can be time intensive, depending on the tesselation algorithm supplied, as such care should be taken to ensure it does not execute on the graphics thread, but rather on a background thread. This method will prepare the geometry list, to be added to the scene by the update() method. Care should also be taken to ensure the update method IS called on the graphics update thread as it causes updates to be made to the Scene.
	 */
	@Override
	public boolean calculate() {
		final long startTime = System.currentTimeMillis();
		updateLock.lock();
		try {
			if (!terrainChunk.isDirty()) {
				state = ChunkState.CALCULATED;
				return false;
			}
			geometries.clear();
			geometries.addAll(createGeometries());
			terrainChunk.setDirty(false);
			state = ChunkState.CALCULATED;
		} finally {
			updateLock.unlock();
		}
		final float duration = (System.currentTimeMillis() - startTime);
		LOG.debug("Calculated Chunk in {}ms" + duration);
		return true;
	}

	@Override
	public void removeBlock(Vector3f location) {
		state = ChunkState.DIRTY;
		terrainChunk.removeBlock(location);
		calculate();
	}

	@Override
	public void setBlock(int blockType, Vector3f location) {
		state = ChunkState.DIRTY;
		terrainChunk.setBlock(new BasicBlock(blockType, terrainChunk.getElementSize(), location), location);
		calculate();
	}

	@Override
	public void update(Vector3f location, Vector3f direction) {
		updateLock.lock();
		try {
			LOG.debug("Updating chunk geometries for {}", this);
			LOG.debug("{} contains {} children", this, getChildren().size());
			LOG.debug("{} adding {} geometries", this, geometries.size());
			detachAllChildren();
			for (final Spatial s : geometries) {
				attachChild(s);
			}
			geometries.clear();
			state = ChunkState.UP2DATE;
		} finally {
			updateLock.unlock();
		}
	}

}
