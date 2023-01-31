/**
 * Noise Author: Matt Teeter May 4, 2012
 */
package org.blockworld.math;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author Matt Teeter
 */
public interface Noise {

   public enum NoiseType {
      PERLIN("Perlin"), RIDGED_MULTIFRACTAL("RidgedMultiFractal");
      private final String name;
      private static Map<String, NoiseType> valueMap = Maps.newHashMap();

      static {
         for (final NoiseType t : NoiseType.values()) {
            valueMap.put(t.getName(), t);
         }
      }

      private NoiseType(final String name) {
         this.name = name;
      }

      public String getName() {
         return name;
      }

      public static NoiseType lookup(final String type) {
         return valueMap.get(type);
      }

   }

   void setFrequency(float frequency);

   void setOctaveCount(int octaves);

   void setSeed(int seed);

   float getValue(float x, float y, float z);
}
