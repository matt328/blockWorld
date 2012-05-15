/**
 * Selector
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

/**
 * Splits a given source function's output based upon the threshold value. First calculates the output of <code>mainSource</code>. If the result is above the threshold value, highSource's value is calculated and returned. If the result is below or equal to the threshold value, lowSource's value is calculated and returned.
 * 
 * @author Matt Teeter
 * 
 */
public class Selector implements Function {

	private Function mainSource;
	private Function lowSource;
	private Function highSource;
	private float threshold;

	/**
	 * Constructs a Selector function with the given parameters.
	 * 
	 * @param mainSource
	 *            - The main {@link Function} to evaluate.
	 * @param lowSource
	 *            - The {@link Function} whose value is returned if the main {@link Function}'s output is above the threshold value.
	 * @param highSource
	 *            - The {@link Function} whose value is returned if the main {@link Function}'s output is below or equal to the threshold value.
	 * @param threshold
	 *            - The threshold value.
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
		if (main > threshold) {
			return highSource.get(x, y, z);
		} else {
			return lowSource.get(x, y, z);
		}
	}

	public Function getMainSource() {
		return mainSource;
	}

	public void setMainSource(Function mainSource) {
		this.mainSource = mainSource;
	}

	public Function getLowSource() {
		return lowSource;
	}

	public void setLowSource(Function lowSource) {
		this.lowSource = lowSource;
	}

	public Function getHighSource() {
		return highSource;
	}

	public void setHighSource(Function highSource) {
		this.highSource = highSource;
	}

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

}
