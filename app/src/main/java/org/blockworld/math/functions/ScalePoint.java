/**
 * ScalePoint Author: Matt Teeter Apr 30, 2012
 */
package org.blockworld.math.functions;

import com.google.common.base.MoreObjects;

/**
 * Scales a point by the scale factor, and then evaluates the source function at
 * that point.
 * 
 * @author Matt Teeter
 */
public class ScalePoint implements Function {

   private float scale;
   private Function source;

   public ScalePoint() {

   }

   public ScalePoint(final Function source, final float scale) {
      this.source = source;
      this.scale = scale;
   }

   @Override
   public float get(final float x, final float y, final float z) {
      return source.get(x * scale, y * scale, z * scale);
   }

   public float getScale() {
      return scale;
   }

   public void setScale(final float scale) {
      this.scale = scale;
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
      .add("scale", scale)
      .add("source", source)
      .toString();
   }

}
