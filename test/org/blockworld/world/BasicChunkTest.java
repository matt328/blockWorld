/**
 * BasicChunkTest
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.world;

import junit.framework.Assert;

import org.junit.Test;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class BasicChunkTest {

	@Test
	public void testMethod() {
		test(Vector3f.ZERO, new Vector3f(0, 0, 0), new Vector3f(8, 0, 8));
		test(Vector3f.ZERO, new Vector3f(-8, 0, 0), new Vector3f(0, 0, 8));
		test(Vector3f.ZERO, new Vector3f(7, 255, 7), new Vector3f(15, 255, 15));

		// test(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(23.0f, 0.0f, 0.0f), new Vector3f(15, 0, 0));
		test(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(8.0f, 0.0f, 0.0f), new Vector3f(0, 0, 8));

	}

	public void test(Vector3f position, Vector3f input, Vector3f expected) {
		BasicChunk c = new BasicChunk(new Vector3f(16, 256, 16), position);
		Vector3f actual = c.globalToLocal(input);
		c.setBlock(1, input);
		Assert.assertEquals(String.format("Input was %s", input.toString()), expected, actual);
	}
}
