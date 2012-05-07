/**
 * BlockworldBlockLoader
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.world.loader;

import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.functions.Constant;
import org.blockworld.math.functions.Function;
import org.blockworld.math.functions.Gradient;
import org.blockworld.math.functions.PerlinNoiseFunction;
import org.blockworld.math.functions.ScalePoint;
import org.blockworld.math.functions.Selector;
import org.blockworld.math.functions.Turbulence;
import org.blockworld.world.BasicChunk;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class BlockworldBlockLoader {
	private final Function groundBase;

	public BlockworldBlockLoader() {
		Function constNeg1 = new Constant(-1.0f);
		Function const1 = new Constant(1.0f);
		Function groundShape = new PerlinNoiseFunction(NoiseType.PERLIN, 1.75f, 2, "Matt".hashCode());
		Function groundGradient = new Gradient(0.0f, 1.0f);
		Function groundTurbulence = new Turbulence(groundGradient, groundShape, 0.4f);
		Function groundSelector = new Selector(groundTurbulence, constNeg1, const1, 5.3f);
		groundBase = new ScalePoint(groundSelector, 0.02f);
	}

	public void fill(BasicChunk chunk) {
		for (int x = -8; x < (chunk.getDimensions().x / 2); x++) {
			for (int y = 0; y < chunk.getDimensions().y - 1; y++) {
				for (int z = -8; z < chunk.getDimensions().z / 2; z++) {
					float noise = groundBase.get(x, y, z);
					chunk.setBlock(noise > 0.0f ? 1 : 0, new Vector3f(x, y, z));
				}
			}
		}
	}

}
