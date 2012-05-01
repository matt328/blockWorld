/**
 * ScalePoint
 * Author: Matt Teeter
 * Apr 30, 2012
 */
package org.blockworld.math.functions;

/**
 * @author Matt Teeter
 * 
 */
public class ScalePoint implements Function {

	private final float scale;
	private final Function source;

	public ScalePoint(final Function source, final float scale) {
		this.source = source;
		this.scale = scale;
	}

	@Override
	public float get(float x, float y, float z) {
		return source.get(x * scale, y * scale, z * scale);
	}

}
