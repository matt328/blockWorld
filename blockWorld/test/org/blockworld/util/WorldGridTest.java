/**
 * WorldGridTest
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.util;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class WorldGridTest {

	@Test
	public final void testSurroundingChunks() {
		Collection<Vector3f> chunks = WorldGrid.getSurroundingChunkPositions(new Vector3f(16, 0, 0), 4, new Vector3f(16, 256, 16));
		System.out.println(chunks);
	}
	
	@Test
	public final void testTranslate() {
		testInner(0.0f, 0.0f);
		testInner(7.0f, 0.0f);
		testInner(8.0f, 16.0f);
		testInner(9.0f, 16.0f);
		testInner(23.0f, 16.0f);
		testInner(24.0f, 32.0f);
		testInner(25.0f, 32.0f);
		
		testInner(-7.0f, 0.0f);
		testInner(-8.0f, -16.0f);
		testInner(-10.0f, -16.0f);
		testInner(-16.0f, -16.0f);
		testInner(-23.0f, -16.0f);
		testInner(-24.0f, -32.0f);
		testInner(-25.0f, -32.0f);
	}

	private void testInner(float input, float expected) {
		float actual = WorldGrid.translate(input, 16);
		Assert.assertEquals(String.format("Failed with input: %s", input), expected, actual);
	}

	// /**
	// * Test method for {@link
	// org.blockworld.util.WorldGrid#worldCoordsToGridCoords(int,
	// com.jme3.math.Vector3f)}.
	// */
	// @Test
	// public final void testWorldCoordsToGridCoords() {
	// test(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
	// test(new Vector3f(9.0f, 0.0f, 0.0f), new Vector3f(16.0f, 0.0f, 0.0f));
	// }

	private void test(Vector3f input, Vector3f expectedOutput) {
		Vector3f actual = new WorldGrid().worldCoordsToGridCoords(16, input);
		Assert.assertEquals(String.format("Failed with input: %s", input.toString()), expectedOutput, actual);
	}

}
