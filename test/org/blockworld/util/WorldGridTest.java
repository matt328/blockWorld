/**
 * WorldGridTest
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.util;

import junit.framework.Assert;

import org.junit.Test;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class WorldGridTest {

	/**
	 * Test method for {@link org.blockworld.util.WorldGrid#worldCoordsToGridCoords(int, com.jme3.math.Vector3f)}.
	 */
	@Test
	public final void testWorldCoordsToGridCoords() {
		test(new Vector3f(-15.0f, 10.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		test(new Vector3f(-16.0f, 10.0f, 0.0f), new Vector3f(-32.0f, 0.0f, 0.0f));
		test(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		test(new Vector3f(10.0f, 10.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		test(new Vector3f(15.0f, 10.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		test(new Vector3f(16.0f, 10.0f, 0.0f), new Vector3f(32.0f, 0.0f, 0.0f));
		test(new Vector3f(31.0f, 10.0f, 0.0f), new Vector3f(32.0f, 0.0f, 0.0f));
		test(new Vector3f(33.0f, 10.0f, 0.0f), new Vector3f(32.0f, 0.0f, 0.0f));
		test(new Vector3f(47.0f, 10.0f, 0.0f), new Vector3f(32.0f, 0.0f, 0.0f));
		test(new Vector3f(48.0f, 10.0f, 0.0f), new Vector3f(64.0f, 0.0f, 0.0f));
	}

	private void test(Vector3f input, Vector3f expectedOutput) {
		Vector3f actual = new WorldGrid().worldCoordsToGridCoords(16, input);
		Assert.assertEquals(String.format("Failed with input: %s", input.toString()), expectedOutput, actual);
	}

}
