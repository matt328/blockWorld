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

	private final Function mainSource;
	private final Function xSource;
	private final Function ySource;
	private final Function zSource;
	private final float xPower;
	private final float yPower;
	private final float zPower;

	public Turbulence(Function mainSource, Function ySource, float yPower) {
		this.mainSource = mainSource;
		this.ySource = ySource;
		this.xSource = new Constant(0.0f);
		this.zSource = new Constant(0.0f);
		
		this.yPower = yPower;
		this.xPower = 1.0f;
		this.zPower = 1.0f;
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
		
		float xDistort = x + (xSource.get(x0, y0, z0) * xPower);
		float yDistort = y + (ySource.get(x1, y1, z1) * yPower);
		float zDistort = z + (zSource.get(x2, y2, z2) * zPower);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return mainSource.get(xDistort, yDistort, zDistort);
	}
}
