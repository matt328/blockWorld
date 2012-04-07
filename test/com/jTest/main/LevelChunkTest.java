package com.jTest.main;

import java.util.BitSet;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.snowFallPeak.blockWorld.level.Level;
import com.snowFallPeak.blockWorld.util.Noise2;

public class LevelChunkTest {
	private Level level;

	@Before
	public void setUp() throws Exception {
		level = new Level(81);
	}

	@Test
	public void testLevelSize() {
		System.out.println(level.getSizeInBytes() / 1024 / 1024 + "MB");
	}

	@Test
	public void testByteToShort() {
		final short hi = 1;
		final short lo = 1;
		final short val = (short) ((hi & 0xFF) << 8 | lo & 0xFF);
		System.out.println("val=" + val);

		System.out.println("Short.MIN_VALUE: " + Short.MIN_VALUE);
		System.out.println("Short.MAX_VALUE: " + Short.MAX_VALUE);

		final byte[] ret = new byte[2];
		ret[0] = (byte) (val & 0xff);
		ret[1] = (byte) (val >> 8 & 0xff);
	}

	@Test
	public void testNoise() {
		final Noise2 noiseMaker = new Noise2(110);
		final Random r = new Random(110);
		for (int i = 0; i < 1000; i++) {
			final double noise = noiseMaker.noise(-r.nextDouble(), -r.nextDouble(), -r.nextDouble());
			System.out.println((noise + 1) / 2);
		}
	}

	public BitSet fromByteArray(final byte[] bytes) {
		final BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & 1 << i % 8) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}

	@After
	public void tearDown() throws Exception {
		level = null;
	}

}
