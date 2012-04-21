/**
 * PerlinNoiseChunkLoader
 * Author: Matt Teeter
 * Apr 17, 2012
 */
package org.blockworld.world.loader;

import org.blockworld.math.SimplexNoise3;
import org.blockworld.world.Block;
import org.blockworld.world.BlockVolume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class PerlinNoiseBlockLoader implements BlockLoader<Block> {

	private static final Logger LOG = LoggerFactory.getLogger(PerlinNoiseBlockLoader.class);

	private final int noiseSize;
	private final int solidBloxelType;
	private final float density;
	private final int operation;

	public PerlinNoiseBlockLoader() {
		this(128);
	}

	public PerlinNoiseBlockLoader(final int noiseSize) {
		this(noiseSize, 2, 3.1f, 0); // sand per default
	}

	public PerlinNoiseBlockLoader(final int noiseSize, final int solidBloxelType, final float density, final int operation) {
		this.noiseSize = noiseSize;
		this.solidBloxelType = solidBloxelType;
		this.density = density;
		this.operation = operation;
	}

	@Override
	public void fill(final BlockVolume<Block> volume) {
		final float startTime = System.currentTimeMillis();
		final BoundingBox bv = volume.getBoundingBox();
		final Vector3f center = bv.getCenter();
		final float elementSize = volume.getElementSize();
		final float xExtent = bv.getXExtent();
		final float yExtent = bv.getYExtent();
		final float zExtent = bv.getZExtent();
		final float step = elementSize * 2;
		final float sizeX = xExtent * 2;
		final float sizeY = yExtent * 2;
		final float sizeZ = zExtent * 2;
		final float noiseSizeHalfSize = noiseSize / 2;
		final float deltaX = center.x + noiseSizeHalfSize - xExtent;
		final float deltaY = center.y + noiseSizeHalfSize - yExtent;
		final float deltaZ = center.z + noiseSizeHalfSize - zExtent;
		for (float z = 0; z <= sizeZ; z += step) {
			for (float y = 0; y <= sizeY; y += step) {
				for (float x = 0; x <= sizeX; x += step) {
					final float mapx = deltaX + x * step;
					final float mapy = deltaY + y * step;
					final float mapz = deltaZ + z * step;
					final float xf = (mapx) / noiseSize;
					final float yf = (mapy) / noiseSize;
					final float zf = (mapz) / noiseSize;
					float plateau_falloff;
					if (yf <= 0.8) {
						plateau_falloff = 1.0f;
					} else if (0.8 < yf && yf < 0.9) {
						plateau_falloff = 1.0f - (yf - 0.8f) * 10.0f;
					} else {
						plateau_falloff = 0.0f;
					}
					final float center_falloff = (float) (0.1 / (Math.pow((xf - 0.5) * 1.5, 2) + Math.pow((yf - 1.0) * 0.8, 2) + Math.pow((zf - 0.5) * 1.5, 2)));
					final float caves = (float) Math.pow(SimplexNoise3.simplex_noise(1, xf * 5, yf * 5, zf * 5), 3);
					float density = (SimplexNoise3.simplex_noise(5, xf, yf * 0.5f, zf) * center_falloff * plateau_falloff);
					density *= Math.pow(SimplexNoise3.noise((xf + 1) * 3.0f, (yf + 1) * 3.0f, (zf + 1) * 3.0f) + 0.4f, 1.8f);
					if (caves < 0.5) {
						density = 0;
					}
					final float teX = center.x - xExtent + x * step;
					final float teY = center.y - yExtent + y * step;
					final float teZ = center.z - zExtent + z * step;
					final Vector3f c = new Vector3f(teX - elementSize, teY - elementSize, teZ - elementSize);
					if (density > this.density) {
						if (operation == 0) {
							volume.setBlock(c.x, c.y, c.z, new Block(c, elementSize, 1));
						} else if (operation == 1) {
							volume.removeBlock(c.x, c.y, c.z);
						}
					}
				}
			}
		}
		final float duration = (System.currentTimeMillis() - startTime);
		LOG.debug("fill chunk with noise time was " + duration + "ms");
	}
}
