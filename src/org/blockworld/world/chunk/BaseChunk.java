/**
 * BaseChunk
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.chunk;

import org.blockworld.world.Block;
import org.blockworld.world.BlockVolume;
import org.blockworld.world.loader.BlockLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Equivalence;
import com.jme3.math.Vector3f;
import com.google.common.base.Objects;

/**
 * @author Matt Teeter
 * 
 */
public class BaseChunk<V extends BlockVolume<Block>> implements BlockChunk<Block> {
	private static final Logger LOG = LoggerFactory.getLogger(BaseChunk.class);
	private final V volume;
	private boolean dirty;
	public static final float DEFAULT_BLOCK_DIMENSION = 0.5f;

	public static final Equivalence<Block> BLOXEL_TYPE_EQUIVALENCE = new Equivalence<Block>() {
		@Override
		public boolean doEquivalent(final Block a, final Block b) {
			if (a == null && b == null) {
				return true;
			}
			if (a == null) {
				return false;
			}
			if (b == null) {
				return false;
			}
			return a.getType() == b.getType();
		}

		@Override
		protected int doHash(Block t) {
			return t == null ? 0 : t.hashCode();
		}
	};

	public static String createName(final BlockChunk<Block> bc) {
		final Vector3f center = bc.getVolume().getBoundingBox().getCenter();
		return String.format("%.2f:%.2f:%.2f", center.x, center.y, center.z);
	}

	public BaseChunk(final V volume) {
		this.volume = volume;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.blockworld.world.chunk.BlockChunk#fill(org.blockworld.level.BlockLoader
	 * )
	 */
	@Override
	public void fill(BlockLoader<Block> loader) {
		final float startTime = System.currentTimeMillis();
		loader.fill(volume);
		dirty = true;
		final float duration = (System.currentTimeMillis() - startTime);
		LOG.trace("Filled chunk. Time was " + duration + "ms");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.chunk.BlockChunk#getVolume()
	 */
	@Override
	public BlockVolume<Block> getVolume() {
		return volume;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.chunk.BlockChunk#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.blockworld.world.chunk.BlockChunk#removeBlock(com.jme3.math.Vector3f)
	 */
	@Override
	public void removeBlock(Vector3f targetPosition) {
		final long startTime = System.currentTimeMillis();
		volume.removeBlock(targetPosition.x, targetPosition.y, targetPosition.z);
		final float duration = (System.currentTimeMillis() - startTime);
		LOG.trace(String.format("Removed terrain-element at position %s in %s ms", targetPosition, duration));
		dirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.blockworld.world.chunk.BlockChunk#setBlock(com.jme3.math.Vector3f,
	 * int)
	 */
	@Override
	public synchronized void setBlock(Vector3f targetPosition, int type) {
		final long startTime = System.currentTimeMillis();
		volume.setBlock(targetPosition.x, targetPosition.y, targetPosition.z, new Block(targetPosition, DEFAULT_BLOCK_DIMENSION, type));
		final float duration = (System.currentTimeMillis() - startTime);
		LOG.trace(String.format("Add bloxel at position %s in %s ms", targetPosition, duration));
		dirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.chunk.BlockChunk#setDirty(boolean)
	 */
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("volume-box", volume.getBoundingBox()).add("volume-element-size", volume.getElementSize()).add("volume-size", volume.size()).toString();
	}

}
