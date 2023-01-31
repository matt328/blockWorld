/**
 * PerlinNoiseFunction Author: Matt Teeter Apr 29, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.Noise;
import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.PerlinNoiseGenerator;
import org.blockworld.math.RidgedMultifractalNoise;

import com.google.common.base.MoreObjects;

/**
 * @author Matt Teeter
 */
public class PerlinNoise implements Function {
   private NoiseType noiseType;
   private Noise noise;
   private float frequency;
   private int octave;
   private String seed;

   public PerlinNoise() {
      this(NoiseType.PERLIN, 1.0f, 1, "S");
   }

   public PerlinNoise(final NoiseType type, final float frequency, final int octaveCount, final String seed) {
      this.noiseType = type;
      this.frequency = frequency;
      this.octave = octaveCount;
      this.seed = seed;
      createNoiseGenerator(type, frequency, octaveCount, seed);
   }

   /**
    * @param type
    * @param frequency
    * @param octaveCount
    * @param seed
    */
   private void createNoiseGenerator(final NoiseType type, final float frequency, final int octaveCount, final String seed) {
      switch (type) {
      case PERLIN:
         noise = new PerlinNoiseGenerator(frequency, octaveCount, seed.hashCode());
         break;
      case RIDGED_MULTIFRACTAL:
         noise = new RidgedMultifractalNoise(frequency, octaveCount, seed.hashCode());
         break;
      default:
         noise = new PerlinNoiseGenerator(frequency, octaveCount, seed.hashCode());
      }
   }

   @Override
   public float get(final float x, final float y, final float z) {
      return noise.getValue(x, y, z);
   }

   public String getNoiseType() {
      return noiseType.getName();
   }

   public void setNoiseType(final String type) {
      this.noiseType = NoiseType.lookup(type);
      createNoiseGenerator(noiseType, frequency, octave, seed);
   }

   public float getFrequency() {
      return frequency;
   }

   public void setFrequency(final float frequency) {
      this.frequency = frequency;
      if (noise != null) {
         noise.setFrequency(frequency);
      }
   }

   public int getOctave() {
      return octave;
   }

   public void setOctave(final int octaveCount) {
      this.octave = octaveCount;
      if (noise != null) {
         noise.setOctaveCount(octaveCount);
      }
   }

   public String getSeed() {
      return seed;
   }

   public void setSeed(final String seed) {
      this.seed = seed;
      if (noise != null) {
         noise.setSeed(seed.hashCode());
      }
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("noiseType", noiseType)
      .add("noise", noise)
      .add("frequency", frequency)
      .add("octave", octave)
      .add("seed", seed)
      .toString();
   }

}
