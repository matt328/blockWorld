/**
 * TurbulenceFunction Author: Matt Teeter Apr 30, 2012
 */
package org.blockworldshared.math.functions;

import com.google.common.base.MoreObjects;

/**
 * Function used to perturb the result of another function.
 * 
 * @author Matt Teeter
 */
public class Turbulence implements Function {

   private Function mainSource;
   private Function xSource;
   private Function ySource;
   private Function zSource;
   private float xPower;
   private float yPower;
   private float zPower;

   public Turbulence() {

   }

   /**
    * Constructs a turbulence function that only perturbs the source function
    * along it's Y-Axis.
    * 
    * @param mainSource
    *           - The {@link Function} to perturb.
    * @param ySource
    *           - The {@link Function} to perturb the main source's Y-Axis input
    *           by.
    * @param yPower
    *           - The amount to perturb the Y-Axis' input by.
    */
   public Turbulence(final Function mainSource, final Function ySource, final float yPower) {
      this.mainSource = mainSource;
      this.ySource = ySource;
      this.xSource = new Constant(0.0f);
      this.zSource = new Constant(0.0f);

      this.yPower = yPower;
      this.xPower = 1.0f;
      this.zPower = 1.0f;
   }

   @Override
   public float get(final float x, final float y, final float z) {
      // Get the values from the three Perlin noise modules and
      // add each value to each coordinate of the input value. There are also
      // some offsets added to the coordinates of the input values. This
      // prevents
      // the distortion modules from returning zero if the (x, y, z)
      // coordinates,
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

      final float xDistort = x + (xSource.get(x0, y0, z0) * xPower);
      final float yDistort = y + (ySource.get(x1, y1, z1) * yPower);
      final float zDistort = z + (zSource.get(x2, y2, z2) * zPower);

      // Retrieve the output value at the offsetted input value instead of the
      // original input value.
      return mainSource.get(xDistort, yDistort, zDistort);
   }

   public Function getMainSource() {
      return mainSource;
   }

   public void setMainSource(final Function mainSource) {
      this.mainSource = mainSource;
   }

   public Function getxSource() {
      return xSource;
   }

   public void setxSource(final Function xSource) {
      this.xSource = xSource;
   }

   public Function getySource() {
      return ySource;
   }

   public void setySource(final Function ySource) {
      this.ySource = ySource;
   }

   public Function getzSource() {
      return zSource;
   }

   public void setzSource(final Function zSource) {
      this.zSource = zSource;
   }

   public float getxPower() {
      return xPower;
   }

   public void setxPower(final float xPower) {
      this.xPower = xPower;
   }

   public float getyPower() {
      return yPower;
   }

   public void setyPower(final float yPower) {
      this.yPower = yPower;
   }

   public float getzPower() {
      return zPower;
   }

   public void setzPower(final float zPower) {
      this.zPower = zPower;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("mainSource", mainSource)
      .add("xSource", xSource)
      .add("ySource", ySource)
      .add("zSource", zSource)
      .add("xPower", xPower)
      .add("yPower", yPower)
      .add("zPower", zPower)
      .toString();
   }
}
