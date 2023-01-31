/**
 * Selector Author: Matt Teeter Apr 29, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.Interpolation;

import com.google.common.base.MoreObjects;

/**
 * Splits a given source function's output based upon the threshold value. First
 * calculates the output of <code>mainSource</code>. If the result is above the
 * threshold value, highSource's value is calculated and returned. If the result
 * is below or equal to the threshold value, lowSource's value is calculated and
 * returned.
 * 
 * @author Matt Teeter
 */
public class Selector implements Function {

   private Function mainSource;
   private Function lowSource;
   private Function highSource;
   private float threshold;
   private float edgeFalloff;

   public Selector() {

   }

   /**
    * Constructs a Selector function with the given parameters.
    * 
    * @param mainSource
    *           - The main {@link Function} to evaluate.
    * @param lowSource
    *           - The {@link Function} whose value is returned if the main
    *           {@link Function}'s output is above the threshold value.
    * @param highSource
    *           - The {@link Function} whose value is returned if the main
    *           {@link Function}'s output is below or equal to the threshold
    *           value.
    * @param threshold
    *           - The threshold value.
    */
   public Selector(final Function mainSource, final Function lowSource, final Function highSource, final float threshold, final float edgeFallOff) {
      this.mainSource = mainSource;
      this.lowSource = lowSource;
      this.highSource = highSource;
      this.edgeFalloff = edgeFallOff;
   }

   @Override
   public float get(final float x, final float y, final float z) {
      final float controlValue = mainSource.get(x, y, z);
      float alpha = 0.0f;
      if (edgeFalloff > 0.0f) {
         if (controlValue < (threshold - edgeFalloff)) {
            // Below threshold and falloff, return lower
            return lowSource.get(x, y, z);

         } else if (controlValue < (threshold)) {
            // Between threshold and falloff to the lower side
            final float lowerCurve = (threshold - edgeFalloff);
            final float upperCurve = (threshold);
            alpha = Interpolation.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
            return Interpolation.linear(lowSource.get(x, y, z), mainSource.get(x, y, z), alpha);

         } else if (controlValue <= (threshold + edgeFalloff)) {
            final float lowerCurve = threshold;
            final float upperCurve = threshold + edgeFalloff;
            alpha = Interpolation.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
            return Interpolation.linear(mainSource.get(x, y, z), highSource.get(x, y, z), alpha);

         } else if (controlValue > threshold + edgeFalloff) {
            return highSource.get(x, y, z);
         } else {
            // Should never happen
            return controlValue;
         }
      } else {
         if (controlValue > threshold) {
            return highSource.get(x, y, z);
         } else {
            return lowSource.get(x, y, z);
         }
      }
   }

   public Function getMainSource() {
      return mainSource;
   }

   public void setMainSource(final Function mainSource) {
      this.mainSource = mainSource;
   }

   public Function getLowSource() {
      return lowSource;
   }

   public void setLowSource(final Function lowSource) {
      this.lowSource = lowSource;
   }

   public Function getHighSource() {
      return highSource;
   }

   public void setHighSource(final Function highSource) {
      this.highSource = highSource;
   }

   public float getThreshold() {
      return threshold;
   }

   public void setThreshold(final float threshold) {
      this.threshold = threshold;
   }

   public float getEdgeFalloff() {
      return edgeFalloff;
   }

   public void setEdgeFalloff(final float edgeFalloff) {
      this.edgeFalloff = edgeFalloff;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("super", super.toString()).add("mainSource", mainSource).add("lowSource", lowSource).add("highSource", highSource).add("threshold", threshold).add("edgeFalloff", edgeFalloff).toString();
   }

}
