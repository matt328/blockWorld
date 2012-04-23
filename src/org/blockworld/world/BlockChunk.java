/**
 * CellVolume
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world;

import java.util.List;

import cern.colt.matrix.ObjectFactory3D;
import cern.colt.matrix.ObjectMatrix3D;

import com.google.common.collect.Lists;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class BlockChunk<T extends Block> implements Chunk<T> {
	private ObjectMatrix3D matrix;
	private final BoundingBox boundingBox;
	private final float elementSize;
	private final int d;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;
	private boolean dirty;

	public BlockChunk(final int dimension, final float elementSize, final Vector3f center) {
		this.elementSize = elementSize;
		boundingBox = new BoundingBox(center, dimension, dimension, dimension);
		d = dimension * 2 + 1;
		matrix = ObjectFactory3D.sparse.make(d, d, d);
		offsetX = -center.x + dimension + elementSize;
		offsetY = -center.y + dimension + elementSize;
		offsetZ = -center.z + dimension + elementSize;
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		return matrix.get((int) position.x, (int) position.y, (int) position.z) == null;
	}

	public void setBlock(T data, Vector3f position) {
		final int mx = (int) (position.x + offsetX);
		final int my = (int) (position.y + offsetY);
		final int mz = (int) (position.z + offsetZ);
		matrix.set(mx, my, mz, data);
		dirty = true;
	}

	@SuppressWarnings("unchecked")
	public T getBlock(Vector3f position) {
		final int mx = (int) (position.x + offsetX);
		final int my = (int) (position.y + offsetY);
		final int mz = (int) (position.z + offsetZ);
		return (T) matrix.getQuick(mx, my, mz);
	}

	@Override
	public void removeBlock(Vector3f position) {
		final int mx = (int) (position.x + offsetX);
		final int my = (int) (position.y + offsetY);
		final int mz = (int) (position.z + offsetZ);
		matrix.set(mx, my, mz, null);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public float getElementSize() {
		return elementSize;
	}

	@Override
	public void clear() {
		matrix = ObjectFactory3D.sparse.make(d, d, d);
	}

	@Override
	public boolean contains(Vector3f point) {
		return boundingBox.intersects(point);
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public List<Block> getLeaves() {
		final List<Block> result = Lists.newArrayList();
		for (int slices = 0; slices < matrix.slices(); slices++) {
			for (int rows = 0; rows < matrix.rows(); rows++) {
				for (int columns = 0; columns < matrix.columns(); columns++) {
					final Block element = (Block) matrix.get(slices, rows, columns);
					if (element != null) {
						result.add(element);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param terrainChunk
	 * @return
	 */
	public static String createName(BlockChunk<Block> terrainChunk) {
		final Vector3f center = terrainChunk.getBoundingBox().getCenter();
		return String.format("%.2f:%.2f:%.2f", center.x, center.y, center.z);
	}

}
