/**
 * DummyBlockLoader
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.world.loader;

import org.blockworld.world.Chunk;

/**
 * @author Matt Teeter
 * 
 */
public class DummyBlockLoader<T extends Chunk> implements BlockLoader<T> {

	/**
	 * @param string
	 */
	public DummyBlockLoader(String string) {

	}

	@Override
	public void fill(T chunk) {

	}

}
