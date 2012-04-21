/**
 * BlockChunk
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.chunk;

import org.blockworld.world.Block;
import org.blockworld.world.BlockVolume;
import org.blockworld.world.loader.BlockLoader;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 *
 */
public interface BlockChunk<T> {
	void fill(BlockLoader<T> loader);
	BlockVolume<Block> getVolume();
	boolean isDirty();
	void removeBlock(Vector3f targetPosition);
	void setBlock(final Vector3f targetPosition, final int type);
	void setDirty(boolean flag);
}
