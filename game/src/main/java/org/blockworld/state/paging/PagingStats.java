/**
 * PagingStats
 * Author: Matt Teeter
 * May 13, 2012
 */
package org.blockworld.state.paging;

import java.text.DecimalFormat;

import org.blockworld.world.node.WorldNode;

import com.jme3.math.Vector3f;

/**
 * @author Matt Teeter
 * 
 */
public class PagingStats {
    private final WorldNode world;
    private DecimalFormat df = new DecimalFormat("#.##");

    public PagingStats(WorldNode world) {
        this.world = world;
    }

    /**
     * Gets an array of the labels of each stat provided by this class.
     * 
     * @return - an array of {@link String}s specifying the labels of the stats.
     */
    public String[] getLabels() {
        return new String[] { "World Position",
                "Grid Position",
                "Chunks To Calculate",
                "Chunks To Add"
        };
    }

    /**
     * Puts the actual stats data in the supplied array of {@link String}s
     * 
     * @param statData
     *                 - (out) the array of {@link String}s that will contain the
     *                 data.
     */
    public void getData(String[] statData) {
        statData[0] = vectorToString(world.getLocation());
        statData[1] = vectorToString(world.getGridLocation());
        statData[2] = Integer.toString(world.getChunksToCalculateSize());
        statData[3] = Integer.toString(world.getChunksToAddSize());
    }

    private String vectorToString(Vector3f v) {
        StringBuilder b = new StringBuilder("(");
        b.append(df.format(v.x)).append(", ").append(df.format(v.y)).append(", ").append(df.format(v.z)).append(")");
        return b.toString();
    }
}
