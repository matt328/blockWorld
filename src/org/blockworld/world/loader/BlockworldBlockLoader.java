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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class BlockworldBlockLoader {
	private final Function groundBase;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockworldBlockLoader.class);

	public BlockworldBlockLoader() {
		Function constNeg1 = new Constant(-1.0f);
		Function const1 = new Constant(1.0f);
		Function groundShape = new PerlinNoiseFunction(NoiseType.PERLIN, 1.75f, 2, "M".hashCode());
		Function groundGradient = new Gradient(0.0f, 1.0f);
		Function groundTurbulence = new Turbulence(groundGradient, groundShape, 0.4f);
		Function groundSelector = new Selector(groundTurbulence, constNeg1, const1, 5.3f);
		groundBase = new ScalePoint(groundSelector, 0.02f);
	}

	public void fill(BasicChunk chunk) {
		Vector3f chunkCenter = chunk.getBoundingBox().getCenter();

		int sx = (int) (chunkCenter.x - chunk.getBoundingBox().getXExtent());
		int sy = (int) (chunkCenter.y - chunk.getBoundingBox().getYExtent());
		int sz = (int) (chunkCenter.z - chunk.getBoundingBox().getZExtent());

		int ex = (int) (chunkCenter.x + chunk.getBoundingBox().getXExtent());
		int ey = (int) (chunkCenter.y + chunk.getBoundingBox().getYExtent());
		int ez = (int) (chunkCenter.z + chunk.getBoundingBox().getZExtent());

		for (int x = sx; x < ex - 1; x++) {
			for (int y = sy; y < ey - 1; y++) {
				for (int z = sz; z < ez - 1; z++) {
					float noise = groundBase.get(x, (chunk.getBoundingBox().getYExtent() * 2) - y, z);
					//LOG.debug(String.format("Setting block %s: %f", new Vector3f(x, y, z), noise));
					chunk.setBlock(noise > 0.0f ? 1 : 0, new Vector3f(x, y, z));
				}
			}
		}
		chunk.setDirty(true);
	}

}
