/**
 * HashMapChunk
 * Author: Matt Teeter
 * Apr 23, 2012
 */
package org.blockworld.world;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class HashMapChunk implements Chunk {
	private static final Logger LOG = LoggerFactory.getLogger(HashMapChunk.class);
	private final Map<Vector3f, Integer> blockMap;
	private final BoundingBox boundingBox;
	private boolean dirty;

	public HashMapChunk(final int dimension, final int height, final Vector3f center) {
		boundingBox = new BoundingBox(center, dimension, height, dimension);
		blockMap = Maps.newHashMap();
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		return blockMap.containsKey(position);
	}

	@Override
	public void setBlock(int data, Vector3f position) {
		LOG.debug("Putting data: " + position.toString());
		blockMap.put(position, data);
		dirty = true;
	}

	@Override
	public int getBlock(Vector3f position) {
		Preconditions.checkNotNull(position);
		if (blockMap.containsKey(position)) {
			return blockMap.get(position);
		} else {
			return 0;
		}
	}

	@Override
	public void removeBlock(Vector3f position) {
		blockMap.remove(position);
		dirty = true;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void clear() {
		blockMap.clear();
		dirty = true;
	}

	@Override
	public boolean contains(Vector3f point) {
		return blockMap.containsKey(point);
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
