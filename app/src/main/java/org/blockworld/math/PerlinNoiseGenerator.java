/**
 * PerlinNoise2
 * Author: Matt Teeter
 * Apr 30, 2012
 */
package org.blockworld.math;

import org.blockworld.math.NoiseUtil.NoiseQuality;

/**
 * Perlin noise class loosely inspired by the techniques used in libnoise.
 * 
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseGenerator implements Noise {
   /**
    * Frequency of the first octave.
    */
   private float frequency;

   /**
    * Frequency multiplier between successive octaves.
    */
   private float lacunarity;

   /**
    * Quality of the Perlin noise.
    */
   private NoiseQuality noiseQuality;

   /**
    * Total number of octaves that generate the Perlin noise.
    */
   private int octaveCount;

   /**
    * Persistence of the Perlin noise.
    */
   private float persistence;

   /**
    * Seed value used by the Perlin-noise function.
    */
   private int seed;

   /**
    * Creates a new Perlin Noise generator with some reasonable defaults.
    */
   public PerlinNoiseGenerator() {
      this.frequency = 1.0f;
      this.lacunarity = 2.0f;
      this.noiseQuality = NoiseQuality.STD;
      this.octaveCount = 6;
      this.persistence = 0.5f;
      this.seed = 0;
   }

   /**
    * Creates a new Perlin Noise generator with the given parameters, and reasonable defaults for the others.
    * 
    * @param frequency
    *            Frequency of the first octave.
    * @param octaveCount
    *            Total number of octaves that generate the Perlin noise.
    * @param seed
    *            Seed value used by the Perlin-noise function.
    */
   public PerlinNoiseGenerator(final float frequency, final int octaveCount, final int seed) {
      this.frequency = frequency;
      this.lacunarity = 2.0f;
      this.noiseQuality = NoiseQuality.STD;
      this.octaveCount = octaveCount;
      this.persistence = 0.5f;
      this.seed = seed;
   }

   /**
    * Creates a new Perlin Noise generator with the given parameters.
    * 
    * @param frequency
    *            Frequency of the first octave.
    * @param lacunarity
    *            Frequency multiplier between successive octaves.
    * @param noiseQuality
    *            Quality of the Perlin noise.
    * @param octaveCount
    *            Total number of octaves that generate the Perlin noise.
    * @param persistence
    *            Persistence of the Perlin noise.
    * @param seed
    *            Seed value used by the Perlin-noise function.
    */
   public PerlinNoiseGenerator(final float frequency, final float lacunarity, final NoiseQuality noiseQuality, final int octaveCount, final float persistence, final int seed) {
      this.frequency = frequency;
      this.lacunarity = lacunarity;
      this.noiseQuality = noiseQuality;
      this.octaveCount = octaveCount;
      this.persistence = persistence;
      this.seed = seed;
   }

   @Override
   public float getValue(float x, float y, float z) {
      float value = 0.0f;
      float signal = 0.0f;
      float curPersistence = 1.0f;
      float nx, ny, nz;
      int seed;

      x *= frequency;
      y *= frequency;
      z *= frequency;

      for (int curOctave = 0; curOctave < octaveCount; curOctave++) {
         // TODO: Make sure that these floating-point values have the same range as a 32-
         // bit integer so that we can pass them to the coherent-noise functions.
         nx = (x);
         ny = (y);
         nz = (z);

         // Get the coherent-noise value from the input value and add it to the
         // final result.
         seed = (this.seed + curOctave) & 0xffffffff;
         signal = NoiseUtil.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
         value += signal * curPersistence;

         // Prepare the next octave.
         x *= lacunarity;
         y *= lacunarity;
         z *= lacunarity;
         curPersistence *= persistence;
      }

      return value;
   }

   public float getFrequency() {
      return frequency;
   }

   @Override
   public void setFrequency(final float frequency) {
      this.frequency = frequency;
   }

   public float getLacunarity() {
      return lacunarity;
   }

   public void setLacunarity(final float lacunarity) {
      this.lacunarity = lacunarity;
   }

   public NoiseQuality getNoiseQuality() {
      return noiseQuality;
   }

   public void setNoiseQuality(final NoiseQuality noiseQuality) {
      this.noiseQuality = noiseQuality;
   }

   public int getOctaveCount() {
      return octaveCount;
   }

   @Override
   public void setOctaveCount(final int octaveCount) {
      this.octaveCount = octaveCount;
   }

   public float getPersistence() {
      return persistence;
   }

   public void setPersistence(final float persistence) {
      this.persistence = persistence;
   }

   public int getSeed() {
      return seed;
   }

   @Override
   public void setSeed(final int seed) {
      this.seed = seed;
   }
}
