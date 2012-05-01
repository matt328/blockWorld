/**
 * PerlinNoiseFunction
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.PerlinNoise2;

/**
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseFunction implements Function {

	private final PerlinNoise2 noise;

	public PerlinNoiseFunction(float frequency, int octaveCount, int seed) {
		noise = new PerlinNoise2(frequency, octaveCount, seed);
	}

	@Override
	public float get(float x, float y, float z) {
		return noise.getValue(x, y, z);
	}

}
