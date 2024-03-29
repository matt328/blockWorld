package org.blockworldshared.math;

import com.jme3.math.FastMath;

/**
 * Lookup table for fast sine & cosine computations. The table currently has a fixed precision of 0.25 degrees to which input angles will be rounded to. All methods are static and can be used with both positive and negative input angles.
 */
public class SinCosLUT {

	/**
	 * set table precision to 0.25 degrees
	 */
	public static final float SC_PRECISION = 0.25f;

	/**
	 * calculate reciprocal for conversions
	 */
	public static final float SC_INV_PREC = 1.0f / SC_PRECISION;

	/**
	 * compute required table length
	 */
	public static final int SC_PERIOD = (int) (360f * SC_INV_PREC);

	/**
	 * LUT for sine values
	 */
	public static final float[] sinLUT = new float[SC_PERIOD];

	/**
	 * LUT for cosine values
	 */
	public static final float[] cosLUT = new float[SC_PERIOD];

	/**
	 * Pre-multiplied degrees -> radians
	 */
	private static final float DEG_TO_RAD = (float) (Math.PI / 180.0) * SC_PRECISION;

	/**
	 * Pre-multiplied radians - degrees
	 */
	private static final float RAD_TO_DEG = (float) (180.0 / Math.PI) / SC_PRECISION;

	// init sin/cos tables with values
	static {
		for (int i = 0; i < SC_PERIOD; i++) {
			sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD);
			cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD);
		}
	}

	/**
	 * Calculate cosine for the passed in angle in radians.
	 * 
	 * @param theta
	 * @return cosine value for theta
	 */
	public static final float cos(float theta) {
		while (theta < 0) {
			theta += FastMath.TWO_PI;
		}
		return cosLUT[(int) (theta * RAD_TO_DEG) % SC_PERIOD];
	}

	/**
	 * Calculates sine for the passed angle in radians.
	 * 
	 * @param theta
	 * @return sine value for theta
	 */
	public static final float sin(float theta) {
		while (theta < 0) {
			theta += FastMath.TWO_PI;
		}
		return sinLUT[(int) (theta * RAD_TO_DEG) % SC_PERIOD];
	}
}