/**
 * ScalingFunction Author: Matt Teeter May 16, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.Interpolation;

import com.google.common.base.MoreObjects;

/**
 * @author Matt Teeter
 */
public class Scaling implements Function {
   private float oldMin, oldMax, newMin, newMax;
   private Function source;

   public Scaling() {

   }

   /**
    * @param oldMin
    * @param oldMax
    * @param newMin
    * @param newMax
    * @param source
    */
   public Scaling(final Function source, final float oldMin, final float oldMax, final float newMin, final float newMax) {
      this.oldMin = oldMin;
      this.oldMax = oldMax;
      this.newMin = newMin;
      this.newMax = newMax;
      this.source = source;
   }

   /*
    * (non-Javadoc)
    * @see org.blockworld.math.functions.Function#get(float, float, float)
    */
   @Override
   public float get(final float x, final float y, final float z) {
      final float result = source.get(x, y, z);
      return Interpolation.translate(result, oldMin, oldMax, newMin, newMax);
   }

   public float getOldMin() {
      return oldMin;
   }

   public void setOldMin(final float oldMin) {
      this.oldMin = oldMin;
   }

   public float getOldMax() {
      return oldMax;
   }

   public void setOldMax(final float oldMax) {
      this.oldMax = oldMax;
   }

   public float getNewMin() {
      return newMin;
   }

   public void setNewMin(final float newMin) {
      this.newMin = newMin;
   }

   public float getNewMax() {
      return newMax;
   }

   public void setNewMax(final float newMax) {
      this.newMax = newMax;
   }

   public Function getSource() {
      return source;
   }

   public void setSource(final Function source) {
      this.source = source;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("oldMin", oldMin)
      .add("oldMax", oldMax)
      .add("newMin", newMin)
      .add("newMax", newMax)
      .add("source", source)
      .toString();
   }

}
