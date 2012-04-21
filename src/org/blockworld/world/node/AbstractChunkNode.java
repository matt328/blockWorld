/**
 * AbstractChunkNode
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.node;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.blockworld.core.asset.BlockWorldAssetManager;
import org.blockworld.world.Block;
import org.blockworld.world.chunk.BaseChunk;
import org.blockworld.world.chunk.BlockChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @author Matt Teeter
 * 
 */
public abstract class AbstractChunkNode extends Node implements ChunkNode {
	enum CHUNKSTATE {
		NEW, CALCULATED, UP2DATE, DIRTY
	}

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	protected final BlockWorldAssetManager assetManager;
	protected final BlockChunk<Block> terrainChunk;
	protected final List<Spatial> geometries = Lists.newArrayList();
	protected CHUNKSTATE state = CHUNKSTATE.NEW;
	protected final Lock calculateAndUpdateLock = new ReentrantLock();

	AbstractChunkNode(final BlockWorldAssetManager theAssetManager, final BlockChunk<Block> aTerrainChunk) {
		super("chunknode:" + BaseChunk.createName(aTerrainChunk));
		assetManager = theAssetManager;
		terrainChunk = aTerrainChunk;
	}

	@Override
	public boolean calculate() {
		final long startTime = System.currentTimeMillis();

		calculateAndUpdateLock.lock();
		try {
			if (!terrainChunk.isDirty()) {
				state = CHUNKSTATE.CALCULATED;
				return false;
			}
			geometries.clear();
			geometries.addAll(createGeometries());
			terrainChunk.setDirty(false);
			state = CHUNKSTATE.CALCULATED;
		} finally {
			calculateAndUpdateLock.unlock();
		}

		final float duration = (System.currentTimeMillis() - startTime);
		LOG.debug("Calculate chunk time was " + duration + "ms");
		return true;
	}

	protected abstract List<Geometry> createGeometries();

	public CHUNKSTATE getState() {
		return state;
	}

	public BlockChunk<Block> getTerrainChunk() {
		return terrainChunk;
	}

	@Override
	public void removeBloxel(Vector3f theLocation) {
		state = CHUNKSTATE.DIRTY;
		terrainChunk.removeBlock(theLocation);
		calculate();
	}

	@Override
	public void setBloxel(Vector3f theLocation, int theBlockType) {
		state = CHUNKSTATE.DIRTY;
		terrainChunk.setBlock(theLocation, theBlockType);
		calculate();
	}

	@Override
	public void update(Vector3f theLocation, Vector3f theDirection) {
		calculateAndUpdateLock.lock();
		try {
			LOG.debug(String.format("Update chunk geometries for '%s'", this));
			detachAllChildren();
			for (final Spatial spatial : geometries) {
				attachChild(spatial);
			}
			geometries.clear();
			state = CHUNKSTATE.UP2DATE;
		} finally {
			calculateAndUpdateLock.unlock();
		}
	}

	@Override
	public String toString() {
		return String.format("%s, state=%s", this.getName(), state);
	}
}
