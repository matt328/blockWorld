/**
 * Noise
 * Author: Matt Teeter
 * May 4, 2012
 */
package org.blockworld.math;

/**
 * @author Matt Teeter
 * 
 */
public interface Noise {
	
	public enum NoiseType {
		PERLIN, RIDGED_MULTIFRACTAL;
	}
	
	float getValue(float x, float y, float z);
}
