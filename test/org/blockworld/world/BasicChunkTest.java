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

		test(new Vector3f(0, 0, 0), new Vector3f(8, 0, 8));
		test(new Vector3f(-8, 8, -8), new Vector3f(0, 8, 0));
		test(new Vector3f(7, 255, 7), new Vector3f(15, 255, 15));
	}

	public void test(Vector3f input, Vector3f expected) {
		BasicChunk c = new BasicChunk(new Vector3f(16, 256, 16), Vector3f.ZERO);
		Vector3f actual = c.globalToLocal(input);
		c.setBlock(1, input);
		Assert.assertEquals(String.format("Input was %s", input.toString()), expected, actual);
	}
}
