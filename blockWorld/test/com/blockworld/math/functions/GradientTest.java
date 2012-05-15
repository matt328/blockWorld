/**
 * GradientTest
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package com.blockworld.math.functions;

import junit.framework.Assert;

import org.blockworld.math.functions.Gradient;
import org.junit.Test;

/**
 * @author Matt Teeter
 * 
 */
public class GradientTest {

	/**
	 * Test method for {@link org.blockworld.math.functions.Gradient#get(float, float, float)}.
	 */
	@Test
	public final void testGet() {
		test(100, 150, 125, 0);
		test(100, 200, 175, 0.5f);
		test(-100, 100, -50, -0.5f);
	}

	private void test(float g1, float g2, float test, float expected) {
		Gradient g = new Gradient(g1, g2);
		float actual = g.get(0, test, 0);
		Assert.assertEquals(String.format("g1: %f, g2: %f, test: %f", g1, g2, test), expected, actual);
	}

}
