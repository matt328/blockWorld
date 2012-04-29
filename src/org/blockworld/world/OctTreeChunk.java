/**
 * OctTreeChunk
 * Author: Matt Teeter
 * Apr 26, 2012
 */
package org.blockworld.world;

import java.util.Collection;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class OctTreeChunk<T extends Block> implements Chunk<T> {

	private final OctNode<T> rootNode;
	private final BoundingBox boundingBox;
	private final float elementSize;
	private boolean dirty;

	public OctTreeChunk(final int dimension, final float elementSize, final Vector3f center) {
		this.elementSize = elementSize;
		boundingBox = new BoundingBox(center, dimension, dimension, dimension);
		rootNode = new OctNode<T>(center, dimension);
	}

	@Override
	public boolean isEmpty(Vector3f position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBlock(T data, Vector3f position) {
		rootNode.insertBlock(data);
	}

	@Override
	public T getBlock(Vector3f position) {
		throw new UnsupportedOperationException("getBlock is not supplied by OctTreeChunk.");
	}

	public OctNode<T> getRootNode() {
		return rootNode;
	}

	@Override
	public void removeBlock(Vector3f position) {
		// TODO Auto-generated method stub

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
		rootNode.clear();
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

	@Override
	public Collection<T> getLeaves() {
		throw new UnsupportedOperationException("getLeaves() is not supplied by OctTreeChunk");
	}
}
