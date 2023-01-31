/**
 * Constant
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

import com.google.common.base.MoreObjects;

/**
 * @author Matt Teeter
 *
 */
public class Constant implements Function {

   private float constant;

   public Constant() {

   }

   public Constant(final float constant) {
      this.constant = constant;
   }

   @Override
   public float get(final float x, final float y, final float z) {
      return constant;
   }

   public float getConstant() {
      return constant;
   }

   public void setConstant(final float constant) {
      this.constant = constant;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("constant", constant)
      .toString();
   }

}
