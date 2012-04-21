/**
 * Matrix3DChunk
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.chunk;

import org.blockworld.world.Block;
import org.blockworld.world.Matrix3DVolume;

import com.google.common.collect.Maps;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class Matrix3DChunk extends BaseChunk<Matrix3DVolume<Block>> {

	/**
	 * @param theCenter
	 *            is absolute position in the "world", if you using only one
	 *            chunk the use the {@link Vector3f#ZERO}, if you combine more
	 *            than one chunk (i.e. in a grid of chunks for open world
	 *            scenarios) then only the central chunk will have the
	 *            {@link Vector3f#ZERO} as center and all other chunks will stay
	 *            around the center chunk
	 * @param theChunkSize
	 *            in x, y and z direction, then the chunk have a volume of
	 *            (x:-aSize..aSize, y:-aSize..aSize, z:-aSize..aSize), depending
	 *            on this size the chunk can have as much {@link Bloxel}S as
	 *            possible, all {@link Bloxel}S of the chunk will be
	 *            <b>inside</b> the chunk
	 */
	public Matrix3DChunk(final Vector3f theCenter, final float theChunkSize) {
		this(theCenter, theChunkSize, DEFAULT_BLOCK_DIMENSION);
	}

	/**
	 * @param theCenter
	 *            is absolute position in the "world", if you using only one
	 *            chunk the use the {@link Vector3f#ZERO}, if you combine more
	 *            than one chunk (i.e. in a grid of chunks for open world
	 *            scenarios) then only the central chunk will have the
	 *            {@link Vector3f#ZERO} as center and all other chunks will stay
	 *            around the center chunk
	 * @param theChunkSize
	 *            in x, y and z direction, then the chunk have a volume of
	 *            (x:-aSize..aSize, y:-aSize..aSize, z:-aSize..aSize), depending
	 *            on this size the chunk can have as much {@link Bloxel}S as
	 *            possible, all {@link Bloxel}S of the chunk will be
	 *            <b>inside</b> the chunk
	 * @param theBloxelSize
	 *            size of each bloxel in the chunk, (x:-aSize..aSize,
	 *            y:-aSize..aSize, z:-aSize..aSize)
	 */
	public Matrix3DChunk(final Vector3f theCenter, final float theChunkSize, final float theBlockSize) {
		super(new Matrix3DVolume<Block>(theCenter, (int) theChunkSize, theBlockSize, Maps.<Vector3f, Block> newHashMap()));
	}
}
