package org.blockworld.math;

import java.util.Random;

public class PerlinNoise {
	protected static final int PERLIN_YWRAPB = 4;
	protected static final int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
	protected static final int PERLIN_ZWRAPB = 8;
	protected static final int PERLIN_ZWRAP = 1 << PERLIN_ZWRAPB;
	protected static final int PERLIN_SIZE = 4095;
	private static final float PERLIN_MIN_AMPLITUDE = 0.001f;
	protected int perlin_octaves = 4; // default to medium smooth
	protected float perlin_amp_falloff = 0.5f; // 50% reduction/octave
	protected int perlin_TWOPI, perlin_PI;
	protected float[] perlin_cosTable;
	protected float perlin[];
	protected Random perlinRandom;

	public PerlinNoise() {
		this(System.nanoTime());
	}

	public PerlinNoise(final long seed) {
		noiseSeed(seed);
	}

	public float multiFractalNoise(float x, float y, float z, final int octaves, final float lacunarity) {
		float result = 0;
		for (int i = 1; i <= octaves; i++) {
			result += noise(x, y, z) * Math.pow(lacunarity, -0.76471 * i);
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return result;
	}

	/**
	 * Computes the Perlin noise function value at point x.
	 */
	public float noise(final float x) {
		// is this legit? it's a dumb way to do it (but repair it later)
		return noise(x, 0f, 0f);
	}

	/**
	 * Computes the Perlin noise function value at the point x, y.
	 */
	public float noise(final float x, final float y) {
		return noise(x, y, 0f);
	}

	/**
	 * Computes the Perlin noise function value at x, y, z.
	 */
	public float noise(float x, float y, float z) {
		if (perlin == null) {
			if (perlinRandom == null) {
				perlinRandom = new Random();
			}
			perlin = new float[PERLIN_SIZE + 1];
			for (int i = 0; i < PERLIN_SIZE + 1; i++) {
				perlin[i] = perlinRandom.nextFloat(); // (float)Math.random();
			}

			perlin_cosTable = SinCosLUT.cosLUT;
			perlin_TWOPI = perlin_PI = SinCosLUT.SC_PERIOD;
			perlin_PI >>= 1;
		}

		if (x < 0) {
			x = -x;
		}
		if (y < 0) {
			y = -y;
		}
		if (z < 0) {
			z = -z;
		}

		int xi = (int) x, yi = (int) y, zi = (int) z;
		float xf = (x - xi);
		float yf = (y - yi);
		float zf = (z - zi);
		float rxf, ryf;

		float r = 0;
		float ampl = 0.5f;

		float n1, n2, n3;

		for (int i = 0; i < perlin_octaves; i++) {
			int of = xi + (yi << PERLIN_YWRAPB) + (zi << PERLIN_ZWRAPB);

			rxf = noise_fsc(xf);
			ryf = noise_fsc(yf);

			n1 = perlin[of & PERLIN_SIZE];
			n1 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n1);
			n2 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n2);
			n1 += ryf * (n2 - n1);

			of += PERLIN_ZWRAP;
			n2 = perlin[of & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n2);
			n3 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n3 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n3);
			n2 += ryf * (n3 - n2);

			n1 += noise_fsc(zf) * (n2 - n1);

			r += n1 * ampl;
			ampl *= perlin_amp_falloff;

			// break if amp has no more impact
			if (ampl < PERLIN_MIN_AMPLITUDE) {
				break;
			}

			xi <<= 1;
			xf *= 2;
			yi <<= 1;
			yf *= 2;
			zi <<= 1;
			zf *= 2;

			if (xf >= 1.0f) {
				xi++;
				xf--;
			}
			if (yf >= 1.0f) {
				yi++;
				yf--;
			}
			if (zf >= 1.0f) {
				zi++;
				zf--;
			}
		}
		return r;
	}

	private float noise_fsc(final float i) {
		// using bagel's cosine table instead
		float iPerlinPI = i * perlin_PI;
		float fPerlinPIMod2Pi = iPerlinPI % perlin_TWOPI;
		int iPerlinPIMod2PI = (int)fPerlinPIMod2Pi;
		
		float cosTable = perlin_cosTable[iPerlinPIMod2PI];
		float oneMinusCosTable = 1.0f - cosTable;
		
		float halfoneMinusCosTable = 0.5f * oneMinusCosTable; 
		
		return halfoneMinusCosTable;
	}

	public void noiseDetail(final int lod) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
	}

	public void noiseDetail(final int lod, final float falloff) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
		if (falloff > 0) {
			perlin_amp_falloff = falloff;
		}
	}

	public void noiseSeed(final long what) {
		if (perlinRandom == null) {
			perlinRandom = new Random();
		}
		perlinRandom.setSeed(what);
		perlin = null;
	}

	private float ridge(float n, final float offset) {
		n = Math.abs(n);
		n = offset - n;
		n = n * n;
		return n;
	}

	public float ridgedMultiFractalNoise(float x, float y, float z, final int octaves, final float lacunarity, final float gain, final float offset) {
		float frequency = 1f;
		float signal;

		/*
		 * Fetch the first noise octave.
		 */
		signal = ridge(noise(x, y, z), offset);
		float result = signal;
		float weight;

		for (int i = 1; i <= octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			weight = gain * signal;

			if (weight > 1.0f) {
				weight = 1.0f;
			} else if (weight < 0.0f) {
				weight = 0.0f;
			}

			signal = ridge(noise(x, y, z), offset);

			signal *= weight;
			result += signal * Math.pow(frequency, -0.86461f);
			frequency *= lacunarity;
		}

		return result;
	}
}