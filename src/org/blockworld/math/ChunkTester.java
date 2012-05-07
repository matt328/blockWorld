/**
 * ChunkTester
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.math;

import org.blockworld.util.Stopwatch;
import org.blockworld.world.BasicChunk;
import org.blockworld.world.loader.BlockworldBlockLoader;
import org.blockworld.world.node.MeshChunkNode;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 *
 */
public class ChunkTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicChunk chunk = new BasicChunk(new Vector3f(16, 256, 16), Vector3f.ZERO);
		BlockworldBlockLoader loader = new BlockworldBlockLoader();
		
		Stopwatch s = new Stopwatch(ChunkTester.class);
		s.start();
		loader.fill(chunk);
		s.stop("Filled chunk in %dms");
		
		MeshChunkNode node = new MeshChunkNode(chunk);
		s.start();
		node.calculate();
		s.stop("Calculated Chunk in %dms");
	}

}
