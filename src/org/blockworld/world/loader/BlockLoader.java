/**
 * BlockLoader
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world.loader;


/**
 * @author Matt Teeter
 *
 */
public interface BlockLoader<T> {
	void fill(T chunk);
}
