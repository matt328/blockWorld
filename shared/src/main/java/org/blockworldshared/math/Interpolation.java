/**
 * Interpolation Author: Matt Teeter Apr 30, 2012
 */
package org.blockworldshared.math;

/**
 * @author Matt Teeter
 */
public final class Interpolation {

   private Interpolation() {

   }

   /**
    * Clamps a value to within a given range.
    * 
    * @param value
    * @param lowerBound
    * @param upperBound
    * @return
    */
   public static float clamp(final float value, final float lowerBound, final float upperBound) {
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
    * @param oldMin
    * @param oldMax
    * @param newMin
    * @param newMax
    * @return
    */
   public static float translate(final float x, final float oldMin, final float oldMax, final float newMin, final float newMax) {
      final float oldRange = oldMax - oldMin;
      final float newRange = newMax - newMin;
      final float ret = (newMin + (x - oldMin)) * (newRange / oldRange);
      return clamp(ret, newMin, newMax);
   }

   /**
    * Performs cubic interpolation between two values bound between two other
    * values. The alpha value should range from 0.0 to 1.0. If the alpha value
    * is 0.0, this function returns @a n1. If the alpha value is 1.0, this
    * function returns @a n2.
    * 
    * @param n0
    *           The value before the first value.
    * @param n1
    *           The first value.
    * @param n2
    *           The second value.
    * @param n3
    *           The value after the second value.
    * @param a
    *           The alpha value.
    * @return The interpolated value.
    */
   public static float cubic(final float n0, final float n1, final float n2, final float n3, final float a) {
      final float p = (n3 - n2) - (n0 - n1);
      final float q = (n0 - n1) - p;
      final float r = n2 - n0;
      final float s = n1;
      return p * a * a * a + q * a * a + r * a + s;
   }

   /**
    * Performs linear interpolation between two values. The alpha value should
    * range from 0.0 to 1.0. If the alpha value is 0.0, this function returns
    * <code>a</code> n0. If the alpha value is 1.0, this function returns @a n1.
    * 
    * @param n0
    *           The first value.
    * @param n1
    *           The second value.
    * @param a
    *           The alpha value.
    * @return The interpolated value.
    */
   public static float linear(final float n0, final float n1, final float a) {
      return ((1.0f - a) * n0) + (a * n1);
   }

   /**
    * Maps a value onto a cubic S-curve.
    * 
    * @param a
    *           The value to map onto a cubic S-curve. Should range from 0.0 to
    *           1.0.
    * @return The mapped value.
    */
   public static float sCurve3(final float a) {
      return (a * a * (3.0f - 2.0f * a));
   }

   /**
    * Maps a value onto a quintic S-curve.
    * 
    * @param a
    *           The value to map onto a quintic S-curve. Should range from 0.0
    *           to 1.0.
    * @return The mapped value.
    */
   public static float sCurve5(final float a) {
      final float a3 = a * a * a;
      final float a4 = a3 * a;
      final float a5 = a4 * a;
      return (6.0f * a5) - (15.0f * a4) + (10.0f * a3);
   }
}
