/**
 * Selector
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

/**
 * @author Matt Teeter
 *
 */
public class Selector implements Function {

	private final Function mainSource;
	private final Function lowSource;
	private final Function highSource;
	private final float threshold;
	
	/**
	 * @param mainSource
	 * @param lowSource
	 * @param highSource
	 * @param threshold
	 */
	public Selector(Function mainSource, Function lowSource, Function highSource, float threshold) {
		this.mainSource = mainSource;
		this.lowSource = lowSource;
		this.highSource = highSource;
		this.threshold = threshold;
	}

	@Override
	public float get(float x, float y, float z) {
		float main = mainSource.get(x, y, z);
		if(main > threshold) {
			return highSource.get(x, y, z);
		} else {
			return lowSource.get(x, y, z);
		}
	}

}
