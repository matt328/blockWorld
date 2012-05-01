/**
 * Constant
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

/**
 * @author Matt Teeter
 *
 */
public class Constant implements Function {

	private float constant;
	
	public Constant(float constant) {
		this.constant = constant;
	}
	
	@Override
	public float get(float x, float y, float z) {
		return constant;
	}

}
