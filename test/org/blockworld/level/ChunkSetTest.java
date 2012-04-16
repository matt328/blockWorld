/**
 * ChunkSetTest
 * Author: Matt Teeter
 * Apr 15, 2012
 */
package org.blockworld.level;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkSetTest {

	private ChunkSet chunkSet;

	@Before
	public void setUp() {
		chunkSet = new ChunkSet(32, null);
	}

	/**
	 * Test method for
	 * {@link org.blockworld.level.ChunkSet#globalLocationToGridLocation(com.jme3.math.Vector3f)}
	 * .
	 */
	@Test
	public void testGlobalLocationToGridLocation() {
		Vector3f test1 = new Vector3f(10.0f, 10.0f, 0.0f);
		Vector2f expected1 = new Vector2f(0.0f, 0.0f);

		Vector2f actual1 = chunkSet.globalLocationToGridLocation(test1);
		Assert.assertEquals(expected1, actual1);

		Vector3f test2 = new Vector3f(31.0f, 0.0f, 31.0f);
		Vector2f expected2 = new Vector2f(0.0f, 0.0f);

		Vector2f actual2 = chunkSet.globalLocationToGridLocation(test2);
		Assert.assertEquals(expected2, actual2);

		Vector3f test3 = new Vector3f(32.0f, 0.0f, 32.0f);
		Vector2f expected3 = new Vector2f(1.0f, 1.0f);

		Vector2f actual3 = chunkSet.globalLocationToGridLocation(test3);
		Assert.assertEquals(expected3, actual3);

		Vector3f test4 = new Vector3f(64.0f, 0.0f, 32.0f);
		Vector2f expected4 = new Vector2f(2.0f, 1.0f);

		Vector2f actual4 = chunkSet.globalLocationToGridLocation(test4);
		Assert.assertEquals(expected4, actual4);
	}

	@Test
	public void testCalculateNeededChunks() {
		Vector2f cgl = new Vector2f(4.0f, 4.0f);
		Collection<Vector2f> expected = new ArrayList<Vector2f>(9);
		expected.add(new Vector2f(3, 3));
		expected.add(new Vector2f(3, 4));
		expected.add(new Vector2f(3, 5));
		expected.add(new Vector2f(4, 3));
		expected.add(new Vector2f(4, 4));
		expected.add(new Vector2f(4, 5));
		expected.add(new Vector2f(5, 3));
		expected.add(new Vector2f(5, 4));
		expected.add(new Vector2f(5, 5));

		Collection<Vector2f> actual = chunkSet.calculateNeededChunks(cgl);

		Assert.assertTrue(CollectionUtils.isEqualCollection(actual, expected));

		Vector2f cgl2 = new Vector2f(0.0f, 2.0f);
		Collection<Vector2f> expected2 = new ArrayList<Vector2f>(6);
		expected2.add(new Vector2f(0, 1));
		expected2.add(new Vector2f(1, 1));
		expected2.add(new Vector2f(-1, 1));
		expected2.add(new Vector2f(0, 2));
		expected2.add(new Vector2f(1, 2));
		expected2.add(new Vector2f(-1, 2));
		expected2.add(new Vector2f(0, 3));
		expected2.add(new Vector2f(1, 3));
		expected2.add(new Vector2f(-1, 3));

		Collection<Vector2f> actual2 = chunkSet.calculateNeededChunks(cgl2);

		Assert.assertTrue(CollectionUtils.isEqualCollection(actual2, expected2));

		Vector2f cgl3 = new Vector2f(0.0f, 0.0f);
		Collection<Vector2f> expected3 = new ArrayList<Vector2f>(6);
		expected3.add(new Vector2f(-1, -1));
		expected3.add(new Vector2f(-1, 0));
		expected3.add(new Vector2f(-1, 1));
		expected3.add(new Vector2f(0, -1));
		expected3.add(new Vector2f(0, 0));
		expected3.add(new Vector2f(0, 1));
		expected3.add(new Vector2f(1, -1));
		expected3.add(new Vector2f(1, 0));
		expected3.add(new Vector2f(1, 1));

		Collection<Vector2f> actual3 = chunkSet.calculateNeededChunks(cgl3);

		Assert.assertTrue("Vector(0, 0) failed.", CollectionUtils.isEqualCollection(actual3, expected3));
	}

}
