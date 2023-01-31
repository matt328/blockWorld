/**
 * Function
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworldshared.math.functions;

/**
 * A Function operates on 3 given parameters and produces a single output value.
 * 
 * @author Matt Teeter
 * 
 */
public interface Function {
	/**
	 * Performs a function on the inputs given. The inputs must be constrained between 0 and 1.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	float get(float x, float y, float z);
}
