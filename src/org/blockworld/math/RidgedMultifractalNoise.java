/**
 * RidgedMultifractalNoise
 * Author: Matt Teeter
 * May 4, 2012
 */
package org.blockworld.math;

import gnu.trove.list.TFloatList;
import gnu.trove.list.array.TFloatArrayList;

import org.blockworld.math.NoiseUtil.NoiseQuality;

import com.google.common.base.Preconditions;

/**
 * @author Matt Teeter
 * 
 */
public class RidgedMultifractalNoise implements Noise {
	private float DEFAULT_RIDGED_FREQUENCY = 1.0f;
	private float DEFAULT_RIDGED_LACUNARITY = 2.0f;
	private int DEFAULT_RIDGED_OCTAVE_COUNT = 6;
	private NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STD;
	private int DEFAULT_RIDGED_SEED = 0;
	private int RIDGED_MAX_OCTAVE = 30;

	/**
	 * Frequency of the first octave.
	 */
	private float frequency;

	/**
	 * Frequency multiplier between successive octaves.
	 */
	private float lacunarity;

	/**
	 * Quality of the ridged-multifractal noise.
	 */
	private NoiseQuality noiseQuality;

	/**
	 * Total number of octaves that generate the ridged-multifractal noise
	 */
	private int octaveCount;

	/**
	 * Contains the spectral weights for each octave.
	 */
	private TFloatList spectralWeights;

	/**
	 * Seed value used by the ridged-multifractal-noise function.
	 */
	private int seed;

	public RidgedMultifractalNoise() {
		this.octaveCount = DEFAULT_RIDGED_OCTAVE_COUNT;
		this.frequency = DEFAULT_RIDGED_FREQUENCY;
		this.lacunarity = DEFAULT_RIDGED_LACUNARITY;
		this.noiseQuality = DEFAULT_RIDGED_QUALITY;
		this.seed = DEFAULT_RIDGED_SEED;
		spectralWeights = new TFloatArrayList(octaveCount);
		calculateSpectralWeights();
	}

	public RidgedMultifractalNoise(float frequency, int octaves, int seed) {
		Preconditions.checkArgument(octaves <= RIDGED_MAX_OCTAVE, "Octaves cannot be greater than 30");
		this.octaveCount = octaves;
		this.frequency = frequency;
		this.lacunarity = DEFAULT_RIDGED_LACUNARITY;
		this.noiseQuality = DEFAULT_RIDGED_QUALITY;
		this.seed = seed;
		spectralWeights = new TFloatArrayList(octaveCount);
		calculateSpectralWeights();
	}

	@Override
	public float getValue(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float signal = 0.0f;
		float value = 0.0f;
		float weight = 1.0f;

		float offset = 1.0f;
		float gain = 2.0f;

		for (int currOctave = 0; currOctave < octaveCount; currOctave++) {
			float nx, ny, nz;
			nx = x;
			ny = y;
			nz = z;

			int seed = (this.seed + currOctave) & 0x7fffffff;
			signal = NoiseUtil.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
			signal = Math.abs(signal);
			signal = offset - signal;

			// Square the signal to increase sharpness of the ridges;
			signal *= signal;

			// Set up feedback loop with weight to produce sharper points at the tops of the ridges
			signal *= weight;

			// Weight successive contributions by the previous signal.
			weight = signal * gain;
			if (weight > 1.0f) {
				weight = 1.0f;
			}
			if (weight < 0.0f) {
				weight = 0.0f;
			}

			value += (signal * spectralWeights.get(currOctave));

			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}

		return (value * 1.25f) - 1.0f;
	}

	private void calculateSpectralWeights() {
		float h = 1.0f;
		float frequency = 1.0f;
		for (int i = 0; i < octaveCount; i++) {
			spectralWeights.add((float) Math.pow(frequency, -h));
			frequency *= lacunarity;
		}
	}

}
