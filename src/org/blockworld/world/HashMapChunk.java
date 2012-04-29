/**
 * HashMapChunk
 * Author: Matt Teeter
 * Apr 23, 2012
 */
package org.blockworld.world;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class HashMapChunk<T extends Block> implements Chunk<T> {

	private final Map<Vector3f, T> blockMap;
	private final BoundingBox boundingBox;
	private final float elementSize;
	private boolean dirty;

	public HashMapChunk(final int dimension, final float elementSize, final Vector3f center) {
		this.elementSize = elementSize;
		boundingBox = new BoundingBox(center, dimension, dimension, dimension);
		blockMap = Maps.newHashMap();
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		return blockMap.containsKey(position);
	}

	@Override
	public void setBlock(T data, Vector3f position) {
		blockMap.put(position, data);
		dirty = true;
	}

	@Override
	public T getBlock(Vector3f position) {
		return blockMap.get(position);
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
	public float getElementSize() {
		return elementSize;
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

	@Override
	public Collection<T> getLeaves() {
		return blockMap.values();
	}

}
