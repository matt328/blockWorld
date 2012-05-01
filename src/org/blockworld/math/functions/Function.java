/**
 * Function
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

/**
 * @author Matt Teeter
 *
 */
public interface Function {
	/**
	 * Performs a function on the inputs given.  The inputs must be constrained between 0 and 1.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	float get(float x, float y, float z);
}
