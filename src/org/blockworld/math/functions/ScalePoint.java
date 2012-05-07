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

	private float scale;
	private Function source;

	public ScalePoint(final Function source, final float scale) {
		this.source = source;
		this.scale = scale;
	}

	@Override
	public float get(float x, float y, float z) {
		return source.get(x * scale, y * scale, z * scale);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Function getSource() {
		return source;
	}

	public void setSource(Function source) {
		this.source = source;
	}

}
