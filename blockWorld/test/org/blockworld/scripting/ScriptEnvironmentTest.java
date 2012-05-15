/**
 * ScriptEnvironmentTest
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.scripting;

import java.util.BitSet;

import org.blockworld.world.node.Face;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matt Teeter
 * 
 */
public class ScriptEnvironmentTest {

	private ScriptEnvironment env;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		env = new ScriptEnvironment();
	}

	@Test
	public void test() throws Exception {
		Face instance = Face.FACE_UP;
		env.registerObject("newFace", instance);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		env = null;
	}

	public static void main(String[] args) {
		BitSet b = new BitSet(2);
		b.set(1);
		System.out.println(b.toString());
		System.out.println(b.length());
	}
	
}
