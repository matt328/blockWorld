/**
 * ArrayChunk
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class ArrayChunk implements Chunk {
	private static final Logger LOG = LoggerFactory.getLogger(ArrayChunk.class);
	private final int[][][] data;
	private final BoundingBox boundingBox;
	private boolean dirty;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;

	public ArrayChunk(final int width, final int height, final Vector3f center) {
		data = new int[width][height][width];
		boundingBox = new BoundingBox(center, width, height, width);
	    offsetX = -center.x + width + 1;
	    offsetY = -center.y + 1;
	    offsetZ = -center.z + width + 1;
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		return this.data[(int) position.x][(int) position.y][(int) position.z] == 0;
	}

	@Override
	public void setBlock(int data, Vector3f position) {
		int mx = (int)(position.x + offsetX);
		int my = (int)(position.y + offsetY);
		int mz = (int)(position.z + offsetZ);
		LOG.debug(String.format("Position: %s produces: (%d, %d, %d)", position, mx, my, mz));
		this.data[mx][my][mz] = data;
	}

	@Override
	public int getBlock(Vector3f position) {
		return this.data[(int) position.x][(int) position.y][(int) position.z];
	}

	@Override
	public void removeBlock(Vector3f position) {
		this.data[(int) position.x][(int) position.y][(int) position.z] = 0;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void clear() {
		for (int x = 0; x < boundingBox.getXExtent(); x++) {
			for (int y = 0; y < boundingBox.getYExtent(); y++) {
				for (int z = 0; z < boundingBox.getZExtent(); z++) {
					data[x][y][z] = 0;
				}
			}
		}
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

	public static String createName(Chunk terrainChunk) {
		final Vector3f center = terrainChunk.getBoundingBox().getCenter();
		return String.format("%.2f:%.2f:%.2f", center.x, center.y, center.z);
	}
}
