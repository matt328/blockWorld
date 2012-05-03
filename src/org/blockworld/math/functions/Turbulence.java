/**
 * TurbulenceFunction
 * Author: Matt Teeter
 * Apr 30, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.PerlinNoise2;

/**
 * Function used to perturb the result of another function.
 * 
 * @author Matt Teeter
 * 
 */
public class Turbulence implements Function {

	private final PerlinNoise2 xNoise;
	private final PerlinNoise2 yNoise;
	private final PerlinNoise2 zNoise;
	private final float power;
	private final Function source;

	public Turbulence(Function source, int seed, int octaves, float frequency, float power) {
		this.source = source;
		this.power = power;

		xNoise = new PerlinNoise2(frequency, octaves, seed);
		yNoise = new PerlinNoise2(frequency, octaves, seed);
		zNoise = new PerlinNoise2(frequency, octaves, seed);
	}

	@Override
	public float get(float x, float y, float z) {
		// Get the values from the three Perlin noise modules and
		// add each value to each coordinate of the input value. There are also
		// some offsets added to the coordinates of the input values. This prevents
		// the distortion modules from returning zero if the (x, y, z) coordinates,
		// when multiplied by the frequency, are near an integer boundary. This is
		// due to a property of gradient coherent noise, which returns zero at
		// integer boundaries.
		
		float x0, y0, z0;
		float x1, y1, z1;
		float x2, y2, z2;
		
		x0 = x + (12414.0f / 65536.0f);
		y0 = y + (65124.0f / 65536.0f);
		z0 = z + (31337.0f / 65536.0f);
		
		x1 = x + (26519.0f / 65536.0f);
		y1 = y + (18128.0f / 65536.0f);
		z1 = z + (60493.0f / 65536.0f);
		
		x2 = x + (53820.0f / 65536.0f);
		y2 = y + (11213.0f / 65536.0f);
		z2 = z + (44845.0f / 65536.0f);
		
		float xDistort = x + (xNoise.getValue(x0, y0, z0) * power);
		float yDistort = y + (yNoise.getValue(x1, y1, z1) * power);
		float zDistort = z + (zNoise.getValue(x2, y2, z2) * power);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return source.get(xDistort, yDistort, zDistort);
	}
}
