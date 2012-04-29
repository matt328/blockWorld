/**
 * OctNode
 * Author: Matt Teeter
 * Apr 26, 2012
 */
package org.blockworld.world;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class OctNode<T extends Block> {
	private T block;
	private BoundingBox boundingBox;
	private Collection<OctNode<T>> children;
	private boolean full;
	private static final List<Vector3f> CENTER_VECTORS;
	
	static {
		CENTER_VECTORS = Lists.newArrayList(new Vector3f(-1,-1,-1),
											new Vector3f(-1,-1, 1),
											new Vector3f(-1, 1,-1),
											new Vector3f(-1, 1, 1),
											new Vector3f( 1,-1,-1),
											new Vector3f( 1,-1, 1),
											new Vector3f( 1, 1,-1),
											new Vector3f( 1, 1, 1)
											);
	}					

	public OctNode(Vector3f center, float dimension) {
		boundingBox = new BoundingBox(center, dimension, dimension, dimension);
		children = Lists.newArrayList();
	}

	public boolean blockPositionedIn(T block) {
		return boundingBox.contains(block.getCenter());
	}

	public void insertBlock(T block) {
		if (blockPositionedIn(block)) {
			// If the given block IS the same as the child block, place it here.
			if(Float.compare(boundingBox.getXExtent(), block.getDimension()) == 0) {
				this.block = block;
				full = true;
				return;
			}
			createChildren();
			boolean childFull = false;
			for (OctNode<T> child : children) {
				if (child.blockPositionedIn(block)) {
					child.insertBlock(block);
					break;
				}
			}
			if(childFull) {
				for(OctNode<T> child : children) {
					if(!child.isFull()) {
						return;
					}
				}
				// Set this to full since all of its children are full
				full = true;
			}
		}
	}

	public boolean isFull() {
		return full;
	}
	
	public void clear() {
		this.block = null;
		for(final OctNode<T> child : children) {
			child.clear();
		}
		children.clear();
	}
	
	public T getBlock() {
		return block;
	}

	private void createChildren() {
		float halfDimension = boundingBox.getXExtent() / 2;
		for(final Vector3f c : CENTER_VECTORS) {
			Vector3f newCenter = c.mult(halfDimension);
			OctNode<T> newChild = new OctNode<T>(newCenter, halfDimension);
			this.children.add(newChild);
		}
	}
}
