/**
 * PerlinNoise2
 * Author: Matt Teeter
 * Apr 30, 2012
 */
package org.blockworld.math;

import org.blockworld.math.Noise.NoiseQuality;

/**
 * Perlin noise class loosely inspired by the techniques used in libnoise.
 * 
 * @author Matt Teeter
 * 
 */
public class PerlinNoise2 {
	/**
	 * Frequency of the first octave.
	 */
	private final float frequency;

	/**
	 * Frequency multiplier between successive octaves.
	 */
	private final float lacunarity;

	/**
	 * Quality of the Perlin noise.
	 */
	private final NoiseQuality noiseQuality;

	/**
	 * Total number of octaves that generate the Perlin noise.
	 */
	private final int octaveCount;

	/**
	 * Persistence of the Perlin noise.
	 */
	private final float persistence;

	/**
	 * Seed value used by the Perlin-noise function.
	 */
	private final int seed;

	/**
	 * Creates a new Perlin Noise generator with some reasonable defaults.
	 */
	public PerlinNoise2() {
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
	public PerlinNoise2(float frequency, int octaveCount, int seed) {
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
	public PerlinNoise2(float frequency, float lacunarity, NoiseQuality noiseQuality, int octaveCount, float persistence, int seed) {
		this.frequency = frequency;
		this.lacunarity = lacunarity;
		this.noiseQuality = noiseQuality;
		this.octaveCount = octaveCount;
		this.persistence = persistence;
		this.seed = seed;
	}

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
			signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
			value += signal * curPersistence;

			// Prepare the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
			curPersistence *= persistence;
		}

		return value;
	}
}
