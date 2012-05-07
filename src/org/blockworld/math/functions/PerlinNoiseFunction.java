/**
 * PerlinNoiseFunction
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math.functions;

import org.blockworld.math.Noise;
import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.PerlinNoise;
import org.blockworld.math.RidgedMultifractalNoise;

/**
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseFunction implements Function {
	private NoiseType type;
	private Noise noise;
	private float frequency;
	private int octaveCount;
	private int seed;
	
	public PerlinNoiseFunction(NoiseType type, float frequency, int octaveCount, int seed) {
		this.type = type;
		createNoiseGenerator(type, frequency, octaveCount, seed);
	}

	/**
	 * @param type
	 * @param frequency
	 * @param octaveCount
	 * @param seed
	 */
	private void createNoiseGenerator(NoiseType type, float frequency, int octaveCount, int seed) {
		switch(type) {
		case PERLIN:
			noise = new PerlinNoise(frequency, octaveCount, seed);
			break;
		case RIDGED_MULTIFRACTAL:
			noise = new RidgedMultifractalNoise(frequency, octaveCount, seed);
			break;
		default:
			noise = new PerlinNoise(frequency, octaveCount, seed);
		}
	}

	@Override
	public float get(float x, float y, float z) {
		return noise.getValue(x, y, z);
	}

	public NoiseType getType() {
		return type;
	}

	public void setType(NoiseType type) {
		this.type = type;
		createNoiseGenerator(type, frequency, octaveCount, seed);
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
		noise.setFrequency(frequency);
	}

	public int getOctaveCount() {
		return octaveCount;
	}

	public void setOctaveCount(int octaveCount) {
		this.octaveCount = octaveCount;
		noise.setOctaveCount(octaveCount);
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
		noise.setSeed(seed);
	}

}
