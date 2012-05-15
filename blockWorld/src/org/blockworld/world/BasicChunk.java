/**
 * BasicChunk
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.world;

import com.google.common.base.Preconditions;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * This implementation of {@link Chunk} uses a simple 3 dimensional array to
 * store blocks as an integer where 0 means air with other block types to be
 * determined.
 * 
 * @author Matt Teeter
 */
public class BasicChunk implements Chunk {
	private int[][][] data;
	private final BoundingBox boundingBox;
	private final Vector3f dimensions;
	private boolean dirty;
	private final String name;

	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;

	/**
	 * Creates a {@link BasicChunk} with the given dimensions and world
	 * position.
	 * 
	 * @param dimensions
	 *            - the dimensions of the chunk, measured in blocks.
	 * @param position
	 *            - the position of the chunk, in global coordinates.
	 */
	public BasicChunk(Vector3f dimensions, Vector3f position) {
		this.dimensions = dimensions;
		this.name = BasicChunk.createChunkName(position);
		float centerX = position.x;
		float centerY = (dimensions.y / 2);
		float centerZ = position.z;

		boundingBox = new BoundingBox(new Vector3f(centerX, centerY, centerZ), dimensions.x / 2, dimensions.y / 2, dimensions.z / 2);
		data = new int[(int) dimensions.x][(int) dimensions.y][(int) dimensions.z];

		offsetX = (int) ((dimensions.x / 2) - centerX);
		offsetY = 0;
		offsetZ = (int) ((dimensions.z / 2) - centerZ);
		dirty = false;
	}

	/**
	 * Utility function to map a block's global position to it's position in the
	 * 3 dimensional array
	 * 
	 * @param globalPosition
	 *            - the global position of the block.
	 * @return - the position of the given block in the array.
	 */
	public Vector3f globalToLocal(Vector3f globalPosition) {
		int nx = (int) (globalPosition.x + offsetX);
		int ny = (int) (globalPosition.y + offsetY);
		int nz = (int) (globalPosition.z + offsetZ);
		return new Vector3f(nx, ny, nz);
	}

	public void setBlock(int type, Vector3f position) {
		Vector3f nPosition = globalToLocal(position);

		checkArrayAccess(position, nPosition);
		data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z] = type;
	}

	public int getBlock(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		checkArrayAccess(position, nPosition);
		return data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z];
	}

	public Vector3f getDimensions() {
		return dimensions;
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		checkArrayAccess(position, nPosition);
		return data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z] == 0;
	}

	@Override
	public void removeBlock(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		checkArrayAccess(position, nPosition);
		data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z] = 0;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void clear() {
		data = null;
	}

	@Override
	public boolean contains(Vector3f point) {
		return boundingBox.contains(point);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockworld.world.Chunk#hasBlock(com.jme3.math.Vector3f)
	 */
	@Override
	public boolean hasBlock(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		if (nPosition.x < 0 || nPosition.y < 0 || nPosition.z < 0) {
			return false;
		}

		if (nPosition.x > dimensions.x - 1 || nPosition.y > dimensions.y - 1 || nPosition.z > dimensions.z - 1) {
			return false;
		}

		return true;
	}

	/**
	 * Prints useful debug info if a global position outside of this
	 * {@link Chunk}'s range is requested from it.
	 * 
	 * @param globalPosition
	 *            - the global position of the block to check
	 * @param localPosition
	 *            - the translated local position to check
	 * 
	 * @throws IllegalArgumentException
	 *             if the local position is found to be outside this
	 *             {@link Chunk}'s range
	 */
	private void checkArrayAccess(Vector3f globalPosition, Vector3f localPosition) {
		Preconditions.checkArgument(localPosition.x >= 0, "Position X Value (%s) produces translated value less than zero", globalPosition.x);
		Preconditions.checkArgument(localPosition.y >= 0, "Position Y Value (%s) produces translated value less than zero", globalPosition.y);
		Preconditions.checkArgument(localPosition.z >= 0, "Position Z Value (%s) produces translated value less than zero", globalPosition.z);

		Preconditions.checkArgument(localPosition.x < dimensions.x, "Position X Value (%s) would cause access outside of bounds (Max X: %s)", localPosition.x, dimensions.x - 1);
		Preconditions.checkArgument(localPosition.y < dimensions.y, "Position Y Value (%s) would cause access outside of bounds (Max Y: %s)", localPosition.y, dimensions.y - 1);
		Preconditions.checkArgument(localPosition.z < dimensions.z, "Position Z Value (%s) would cause access outside of bounds (Max Z: %s)", localPosition.z, dimensions.z - 1);
	}

	public static String createChunkName(Vector3f position) {
		return "Chunk " + position.toString();
	}

	@Override
	public String getName() {
		return name;
	}

}
