/**
 * BlockworldBlockLoader Author: Matt Teeter May 6, 2012
 */
package org.blockworld.world.loader;

import org.blockworld.util.Stopwatch;
import org.blockworld.world.Chunk;
import org.blockworldshared.math.Noise.NoiseType;
import org.blockworldshared.math.functions.Function;
import org.blockworldshared.math.functions.PerlinNoise;
import org.blockworldshared.math.functions.ScalePoint;
import org.blockworldshared.math.functions.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Vector3f;

/**
 * Utilizes the blockworld math function library to generate random terrain and
 * floating islands using a perlin noise based algorithm<br>
 * <br>
 * Inspired by articles:<br>
 * <br>
 * 
 * <pre>
 * http://www.gamedev.net/blog/33/entry-2227887-more-on-minecraft-type-world-gen/
 * http://www.gamedev.net/blog/33/entry-2249106-more-procedural-voxel-world-generation/
 * </pre>
 * 
 * @author Matt Teeter
 */
public class BlockworldBlockLoader {

   @SuppressWarnings("unused")
   private static final Logger LOG = LoggerFactory.getLogger(BlockworldBlockLoader.class);

   private final Function groundBase;

   public BlockworldBlockLoader() {

      final Function zoneNoise = new PerlinNoise(NoiseType.PERLIN, 0.15f, 1, "Matt");
      final Function scaleZones = new ScalePoint(zoneNoise, 0.005f);

      groundBase = new World("M", 0.4f, scaleZones, 1.5f, 2, 0.02f);
   }

   /**
    * This function is needed when tesselating a block whose neighbor falls
    * outside of the current {@link Chunk}. This is only a temporary solution as
    * when allowing players to manipulate the terrain, the stored data will have
    * to also be consulted.
    * 
    * @param position
    *           - the global position to get a block for.
    * @param totalHeight
    *           - the height of the chunks.
    * @return - an integer representing the type of the requested block.
    */
   public int getAdHocBlock(final Vector3f position) {
      final float noise = groundBase.get(position.x, position.y, position.z);
      return noise > 0.0f ? 0 : 1;
   }

   public void fill(final Chunk chunk) {
      final Stopwatch s = new Stopwatch(getClass());
      s.start();
      final Vector3f chunkCenter = chunk.getBoundingBox().getCenter();

      final int sx = (int) (chunkCenter.x - chunk.getBoundingBox().getXExtent());
      final int sy = (int) (chunkCenter.y - chunk.getBoundingBox().getYExtent());
      final int sz = (int) (chunkCenter.z - chunk.getBoundingBox().getZExtent());

      final int ex = (int) (chunkCenter.x + chunk.getBoundingBox().getXExtent());
      final int ey = (int) (chunkCenter.y + chunk.getBoundingBox().getYExtent());
      final int ez = (int) (chunkCenter.z + chunk.getBoundingBox().getZExtent());

      for (int x = sx; x < ex; x++) {
         for (int y = sy; y < ey; y++) {
            for (int z = sz; z < ez; z++) {
               final float noise = groundBase.get(x, y, z);
               // LOG.debug(String.format("Setting block %s: %f", new
               // Vector3f(x, y, z), noise));
               chunk.setBlock(noise > 0.0f ? 0 : 1, new Vector3f(x, y, z));
            }
         }
      }
      chunk.setDirty(true);
      s.stop("Filled chunk in %dms");
   }

}
