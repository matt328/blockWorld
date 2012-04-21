/**
 * Matrix3DVolume
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world;

import java.util.List;
import java.util.Map;

import cern.colt.matrix.ObjectFactory3D;
import cern.colt.matrix.ObjectMatrix3D;

import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class Matrix3DVolume<T> implements BlockVolume<T> {
	private ObjectMatrix3D matrix3d;
	private final int d;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;

	private final float elementSize;
	private final BoundingBox bb;

	public Matrix3DVolume(final Vector3f center, final int dimension, final float elementSize, final Map<Vector3f, T> volumeData) {
		this.elementSize = elementSize;
		bb = new BoundingBox(center, dimension, dimension, dimension);
		d = dimension * 2 + 1;
		matrix3d = ObjectFactory3D.sparse.make(d, d, d);
		offsetX = -center.x + dimension + elementSize;
		offsetY = -center.y + dimension + elementSize;
		offsetZ = -center.z + dimension + elementSize;
		for (final Map.Entry<Vector3f, T> e : volumeData.entrySet()) {
			final Vector3f location = e.getKey();
			final int mx = (int) (location.x + offsetX);
			final int my = (int) (location.y + offsetY);
			final int mz = (int) (location.z + offsetZ);
			matrix3d.set(mx, my, mz, e.getValue());
		}
	}

	/**
	 * @see org.blockworld.world.BlockVolume#clear()
	 */
	@Override
	public void clear() {
		matrix3d = ObjectFactory3D.sparse.make(d, d, d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#contains(float, float, float)
	 */
	@Override
	public boolean contains(float x, float y, float z) {
		return bb.intersects(new Vector3f(x, y, z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#getBlock(float, float, float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getBlock(float x, float y, float z) {
		final int mx = (int) (x + offsetX);
		final int my = (int) (y + offsetY);
		final int mz = (int) (z + offsetZ);
		return (T) matrix3d.get(mx, my, mz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#getBoundingBox()
	 */
	@Override
	public BoundingBox getBoundingBox() {
		return bb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#getElementSize()
	 */
	@Override
	public float getElementSize() {
		return elementSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#isEmpty(float, float, float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isEmpty(float x, float y, float z) {
		final int mx = (int) (x + offsetX);
		final int my = (int) (y + offsetY);
		final int mz = (int) (z + offsetZ);
		return (T) matrix3d.get(mx, my, mz) == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.blockworld.world.BlockVolume#pack(com.google.common.base.Equivalence)
	 */
	@Override
	public void pack(Equivalence<T> equivalence) {
		// NOOP
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#removeBlock(float, float, float)
	 */
	@Override
	public void removeBlock(float x, float y, float z) {
		setBlock(x, y, z, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#setBlock(float, float, float,
	 * java.lang.Object)
	 */
	@Override
	public void setBlock(float x, float y, float z, T data) {
		final int mx = (int) (x + offsetX);
		final int my = (int) (y + offsetY);
		final int mz = (int) (z + offsetZ);
		matrix3d.set(mx, my, mz, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.BlockVolume#size()
	 */
	@Override
	public int size() {
		int s = 0;
		for (int slices = 0; slices < matrix3d.slices(); slices++) {
			for (int rows = 0; rows < matrix3d.rows(); rows++) {
				for (int columns = 0; columns < matrix3d.columns(); columns++) {
					if (matrix3d.get(slices, rows, columns) != null) {
						s++;
					}
				}
			}
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getLeaves(Matrix3DVolume<T> o) {
		List<T> blocks = Lists.newArrayList();
		for (int slices = 0; slices < o.matrix3d.slices(); slices++) {
			for (int rows = 0; rows < o.matrix3d.rows(); rows++) {
				for (int columns = 0; columns < o.matrix3d.columns(); columns++) {
					final T element = (T) o.matrix3d.get(slices, rows, columns);
					if (element != null) {
						blocks.add(element);
					}
				}
			}
		}
		return blocks;
	}

}
