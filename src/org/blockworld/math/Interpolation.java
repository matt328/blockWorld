/**
 * Interpolation
 * Author: Matt Teeter
 * Apr 30, 2012
 */
package org.blockworld.math;

/**
 * @author Matt Teeter
 * 
 */
public class Interpolation {

	/**
	 * Clamps a value to within a given range.
	 * 
	 * @param value
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static float clamp(float value, float lowerBound, float upperBound) {
		if (value > upperBound) {
			return upperBound;
		} else if (value < lowerBound) {
			return lowerBound;
		} else {
			return value;
		}
	}

	/**
	 * Translates a value from one range to another.
	 * 
	 * @param x
	 * @param old_min
	 * @param old_max
	 * @param new_min
	 * @param new_max
	 * @return
	 */
	public static float scale(float x, float old_min, float old_max, float new_min, float new_max) {
		float old_range = old_max - old_min;
		float new_range = new_max - new_min;
		float ret = (new_min + (x - old_min)) * (new_range / old_range);
		return ret;
	}

	/**
	 * Performs cubic interpolation between two values bound between two other values. The alpha value should range from 0.0 to 1.0. If the alpha value is 0.0, this function returns @a n1. If the alpha value is 1.0, this function returns @a n2.
	 * 
	 * @param n0
	 *            The value before the first value.
	 * @param n1
	 *            The first value.
	 * @param n2
	 *            The second value.
	 * @param n3
	 *            The value after the second value.
	 * @param a
	 *            The alpha value.
	 * 
	 * @return The interpolated value.
	 */
	public static float cubic(float n0, float n1, float n2, float n3, float a) {
		float p = (n3 - n2) - (n0 - n1);
		float q = (n0 - n1) - p;
		float r = n2 - n0;
		float s = n1;
		return p * a * a * a + q * a * a + r * a + s;
	}

	/**
	 * Performs linear interpolation between two values. The alpha value should range from 0.0 to 1.0. If the alpha value is 0.0, this function returns <code>a</code> n0. If the alpha value is 1.0, this function returns @a n1.
	 * 
	 * @param n0
	 *            The first value.
	 * @param n1
	 *            The second value.
	 * @param a
	 *            The alpha value.
	 * 
	 * @return The interpolated value.
	 * 
	 */
	public static float linear(float n0, float n1, float a) {
		return ((1.0f - a) * n0) + (a * n1);
	}

	/**
	 * Maps a value onto a cubic S-curve.
	 * 
	 * @param a
	 *            The value to map onto a cubic S-curve. Should range from 0.0 to 1.0.
	 * @return The mapped value.
	 */
	public static float sCurve3(float a) {
		return (a * a * (3.0f - 2.0f * a));
	}

	/**
	 * Maps a value onto a quintic S-curve.
	 * 
	 * @param a
	 *            The value to map onto a quintic S-curve. Should range from 0.0 to 1.0.
	 * @return The mapped value.
	 */
	public static float sCurve5(float a) {
		float a3 = a * a * a;
		float a4 = a3 * a;
		float a5 = a4 * a;
		return (6.0f * a5) - (15.0f * a4) + (10.0f * a3);
	}
}
