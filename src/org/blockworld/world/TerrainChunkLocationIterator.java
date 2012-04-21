package org.blockworld.world;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;
import com.jme3.math.Vector3f;

/**
 * This iterator return {@link Vector3f chunk-locations} around the given
 * center-location. The returned locations could be used to get chunk neighbors.
 * 
 */
public class TerrainChunkLocationIterator implements Iterator<Vector3f> {

	/**
	 * The available chunk locations around the given initial chunk.
	 */
	private final Iterator<Vector3f> chunkLocationIterator;

	/**
	 * The full size (in each dimension) of one chunk.
	 */
	private final float chunkSize;

	/**
	 * Chunks around the given initial chunk, in chunk-size steps.
	 */
	private final Vector3f eventHorizonMax;

	/**
	 * Chunks around the given initial chunk, in chunk-size steps.
	 */
	private final Vector3f eventHorizonMin;

	/**
	 * @param theCenter
	 *            the start position of this iterator
	 * @param theEventHorizonMin
	 *            define the min number of chunks around the given center - from
	 *            here the location iterator starts returning more locations
	 * @param theEventHorizonMax
	 *            define the max number of chunks in x, y and z direction around
	 *            the given center which should include in the available chunks,
	 *            in chunk-size steps - to here the location iterator runs
	 * @param theChunkSize
	 *            is half size of a chunk, so the space between neighbor-chunks
	 *            is <code>theChunkSize*2</code>
	 */
	public TerrainChunkLocationIterator(final Vector3f theCenter, final Vector3f theEventHorizonMin, final Vector3f theEventHorizonMax, final float theChunkSize) {
		eventHorizonMax = theEventHorizonMax;
		eventHorizonMin = theEventHorizonMin;
		chunkSize = theChunkSize * 2;
		chunkLocationIterator = createChunkLocationsIterator(theCenter);
	}

	/**
	 * Calculate all chunk-locations around the given chunkLocation, include the
	 * given chunkLocation.
	 * 
	 * @param chunkLocations
	 *            to fill, must not be <code>null</code>
	 * @param theCenter
	 *            the center location
	 */
	private void addChunks(final Set<Vector3f> chunkLocations, final Vector3f theCenter) {
		final float rxMax = eventHorizonMax.x * chunkSize;
		final float ryMax = eventHorizonMax.y * chunkSize;
		final float rzMax = eventHorizonMax.z * chunkSize;
		for (float y = -ryMax; y <= ryMax; y += chunkSize) {
			for (float x = -rxMax; x <= rxMax; x += chunkSize) {
				for (float z = -rzMax; z <= rzMax; z += chunkSize) {
					chunkLocations.add(new Vector3f(x, y, z).add(theCenter));
				}
			}
		}
	}

	private Iterator<Vector3f> createChunkLocationsIterator(final Vector3f theCenter) {
		final Set<Vector3f> chunkLocations = Sets.newHashSet();
		addChunks(chunkLocations, theCenter);
		return chunkLocations.iterator();
	}

	@Override
	public boolean hasNext() {
		return chunkLocationIterator.hasNext();
	}

	@Override
	public Vector3f next() {
		return chunkLocationIterator.next();
	}

	@Override
	public void remove() {
		chunkLocationIterator.remove();
	}
}
