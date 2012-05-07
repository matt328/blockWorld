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
 * @author Matt Teeter
 * 
 */
public class BasicChunk implements Chunk {
	private int[][][] data;
	private final BoundingBox boundingBox;
	private final Vector3f dimensions;
	private boolean dirty;
	private boolean empty;
	
	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;

	public BasicChunk(Vector3f dimensions, Vector3f position) {
		this.dimensions = dimensions;
		Vector3f gridPosition = worldCoordsToGridCoords((int) dimensions.x, position);
		boundingBox = new BoundingBox(position, dimensions.x, dimensions.y, dimensions.z);
		data = new int[(int) dimensions.x][(int) dimensions.y][(int) dimensions.z];
		offsetX = (int) ((int) ((dimensions.x / 2)) + (gridPosition.x * dimensions.x));
		offsetY = 0;
		offsetZ = (int) ((int) ((dimensions.z / 2)) + (gridPosition.z * dimensions.z));
		dirty = false;
		empty = true;
	}

	public void setBlock(int type, Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		Preconditions.checkArgument(nPosition.x < dimensions.x, "Position X Value (%s) would cause access outside of bounds (Max X: %s)", nPosition.x, dimensions.x - 1);
		Preconditions.checkArgument(nPosition.y < dimensions.y, "Position Y Value (%s) would cause access outside of bounds (Max Y: %s)", nPosition.y, dimensions.y - 1);
		Preconditions.checkArgument(nPosition.z < dimensions.z, "Position Z Value (%s) would cause access outside of bounds (Max Z: %s)", nPosition.z, dimensions.z - 1);
		data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z] = type;
		empty = false;
	}

	public int getBlock(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		return data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z];
	}

	public Vector3f globalToLocal(Vector3f globalPosition) {
		int nx = (int) (globalPosition.x + offsetX);
		int ny = (int) (globalPosition.y + offsetY);
		int nz = (int) (globalPosition.z + offsetZ);
		return new Vector3f(nx, ny, nz);
	}

	public Vector3f worldCoordsToGridCoords(int chunkDimension, Vector3f location) {
		Vector3f translated = new Vector3f(translate(location.x, chunkDimension), translate(location.y, chunkDimension), translate(location.z, chunkDimension));
		return translated.mult(chunkDimension * 2);
	}

	private float translate(float i, int d) {
		int d2 = d * 2;
		int dMinusOne = d - 1;
		return (int) ((i - d) / d2) + (i > dMinusOne ? 1 : 0);
	}

	public Vector3f getDimensions() {
		return dimensions;
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		return empty;
	}

	@Override
	public void removeBlock(Vector3f position) {
		Vector3f nPosition = globalToLocal(position);
		data[(int) nPosition.x][(int) nPosition.y][(int) nPosition.z] = 0;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void clear() {
		data = new int[(int) dimensions.x][(int) dimensions.y][(int) dimensions.z];
		empty = true;
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

}
