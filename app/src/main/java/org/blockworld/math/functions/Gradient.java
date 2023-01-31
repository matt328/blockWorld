/**
 * Gradient Author: Matt Teeter Apr 29, 2012
 */
package org.blockworld.math.functions;

import com.google.common.base.MoreObjects;

/**
 * @author Matt Teeter
 */
public class Gradient implements Function {
   private float y1;
   private float y2;

   public Gradient() {

   }

   public Gradient(final float y1, final float y2) {
      this.y1 = y1;
      this.y2 = y2;
   }

   @Override
   public float get(final float x, final float y, final float z) {
      final float nY2 = y2 - y1;
      final float nY = y - y1;
      final float r = nY / nY2;
      return (r * 2) - 1;
   }

   public float getY1() {
      return y1;
   }

   public float getY2() {
      return y2;
   }

   public void setY1(final float y1) {
      this.y1 = y1;
   }

   public void setY2(final float y2) {
      this.y2 = y2;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("y1", y1)
      .add("y2", y2)
      .toString();
   }

}
