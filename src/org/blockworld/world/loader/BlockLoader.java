/**
 * ChunkLoader
 * Author: Matt Teeter
 * Apr 17, 2012
 */
package org.blockworld.world.loader;

import org.blockworld.world.BlockVolume;

/**
 * @author Matt Teeter
 * 
 */
public interface BlockLoader<T> {
	void fill(BlockVolume<T> volume);
}
