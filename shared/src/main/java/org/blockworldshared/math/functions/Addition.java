/**
 * Addition Author: Matt Teeter May 16, 2012
 */
package org.blockworldshared.math.functions;

import com.google.common.base.MoreObjects;

/**
 * @author Matt Teeter
 */
public class Addition implements Function {
   private Function source1;
   private Function source2;

   public Addition() {

   }

   public Function getSource1() {
      return source1;
   }

   public void setSource1(final Function source1) {
      this.source1 = source1;
   }

   public Function getSource2() {
      return source2;
   }

   public void setSource2(final Function source2) {
      this.source2 = source2;
   }

   /**
    * @param source1
    * @param source2
    */
   public Addition(final Function newSource1, final Function newSource2) {
      this.source1 = newSource1;
      this.source2 = newSource2;
   }

   /*
    * (non-Javadoc)
    * @see org.blockworld.math.functions.Function#get(float, float, float)
    */
   @Override
   public final float get(final float x, final float y, final float z) {
      final float s1 = source1.get(x, y, z);
      final float s2 = source2.get(x, y, z);
      return s1 + s2;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("source1", source1)
      .add("source2", source2)
      .toString();
   }
}
