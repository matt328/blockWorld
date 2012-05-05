/**
 * PerlinNoiseFunction
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.Noise;
import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.PerlinNoise2;
import org.blockworld.math.RidgedMultifractalNoise;

/**
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseFunction implements Function {

	private final Noise noise;

	public PerlinNoiseFunction(NoiseType type, float frequency, int octaveCount, int seed) {
		switch(type) {
		case PERLIN:
			noise = new PerlinNoise2(frequency, octaveCount, seed);
			break;
		case RIDGED_MULTIFRACTAL:
			noise = new RidgedMultifractalNoise(frequency, octaveCount, seed);
			break;
		default:
			noise = new PerlinNoise2(frequency, octaveCount, seed);
		}
	}

	@Override
	public float get(float x, float y, float z) {
		return noise.getValue(x, y, z);
	}

}
