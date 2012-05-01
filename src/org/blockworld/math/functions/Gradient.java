/**
 * Gradient
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

/**
 * @author Matt Teeter
 *
 */
public class Gradient implements Function {
	private final float y1;
	private final float y2;
	
	public Gradient(float y1, float y2) {
		this.y1 = y1;
		this.y2 = y2;
	}
	
	@Override
	public float get(float x, float y, float z) {
		float nY2 = y2 - y1;
		float nY = y - y1;
		float r = nY / nY2;
		return (r * 2) - 1;
	}

}
