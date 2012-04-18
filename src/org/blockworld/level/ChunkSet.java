package org.blockworld.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.blockworld.util.Noise2;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Iterables;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * ChunkSet is a container node for chunks. It has a companion controller
 * {@link ChunkSetControl} which should always be attached to it in order to
 * call the updateLoadedChunks method.
 * 
 * @author Matt Teeter
 * 
 */
public class ChunkSet extends Node {

	private final ExecutorService executor;
	private final int cellsPerChunk;
	private final Collection<Vector2f> loadedChunks;
	private final Noise2 noiseGenerator;
	private final ReadWriteLock loadedChunksLock;
	private final Material material;
	private Vector2f oldCameraGridLocation;

	/**
	 * Creates a new ChunkSet with the given number of chunks and cells per
	 * chunk. Chunksets are always square.
	 * 
	 * @param newNumChunks
	 *            - <code>int</code> specifiying the length and width of the
	 *            chunkset, in chunks.
	 * @param newCellsPerChunk
	 *            - <code>int</code> specifying the length, width and height of
	 *            <code>Chunk</code>s, in Cells that will make up this chunkset.
	 */
	public ChunkSet(final int newCellsPerChunk, final Material material) {
		executor = Executors.newFixedThreadPool(4, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread th = new Thread(r);
				th.setDaemon(true);
				return th;
			}
		});
		cellsPerChunk = newCellsPerChunk;
		loadedChunks = new ArrayList<Vector2f>(9);
		noiseGenerator = new Noise2(34);
		loadedChunksLock = new ReentrantReadWriteLock();
		this.material = material;
	}

	public void updateLoadedChunks(Vector3f cameraGlobalLocation) {
		Vector2f cameraGridLocation = globalLocationToGridLocation(cameraGlobalLocation);
		if (cameraGridLocation.equals(oldCameraGridLocation)) {
			return;
		} else {
			oldCameraGridLocation = cameraGridLocation;
			Collection<Vector2f> neededChunks = calculateNeededChunks(cameraGridLocation);
			Lock lock = loadedChunksLock.writeLock();
			lock.lock();
			try {
				if (ImmutableMultiset.copyOf(neededChunks).equals(ImmutableMultiset.copyOf(loadedChunks))) {
					return;
				} else {
					// Filter needed chunks
					List<Vector2f> neededToLoad = new ArrayList<Vector2f>(Collections2.filter(neededChunks, Predicates.not(Predicates.in(loadedChunks))));
					final Collection<Vector2f> copyOfLoaded = new ArrayList<Vector2f>(loadedChunks);
					
					final List<Vector2f> neededToUnload = new ArrayList<Vector2f>(Collections2.filter(copyOfLoaded, Predicates.not(Predicates.in(neededChunks))));
					if (!neededToUnload.isEmpty()) {
						getControl(ChunkSetControl.class).enqueue(new Callable<Void>() {
							@Override
							public Void call() throws Exception {
								for (Vector2f v : neededToUnload) {
									detachChildNamed("Chunk-" + v.toString());
								}
								return null;
							}
						});
					}
					if (!neededToLoad.isEmpty()) {
						for (Vector2f gridCoordinates : neededToLoad) {
							ChunkLoaderTask clt = new ChunkLoaderTask(this, gridCoordinates, noiseGenerator, cellsPerChunk);
							executor.submit(clt);
							loadedChunks.add(gridCoordinates);
						}
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * @param cameraGridLocation
	 */
	public Collection<Vector2f> calculateNeededChunks(Vector2f cameraGridLocation) {
		Collection<Vector2f> neededChunks = new ArrayList<Vector2f>(9);
		for (int x = (int) (cameraGridLocation.x - 1.0f); x <= cameraGridLocation.x + 1.0f; x++) {
			for (int y = (int) (cameraGridLocation.y - 1.0f); y <= cameraGridLocation.y + 1.0f; y++) {
				neededChunks.add(new Vector2f(x, y));
			}
		}
		return neededChunks;
	}

	public Vector2f globalLocationToGridLocation(Vector3f globalLocation) {
		return new Vector2f((int) globalLocation.x / cellsPerChunk, (int) globalLocation.z / cellsPerChunk);
	}

	public void markChunkLoaded(Vector2f gridCoord) {
		Lock writeLock = loadedChunksLock.writeLock();
		writeLock.lock();
		try {
			loadedChunks.add(gridCoord);
		} finally {
			writeLock.unlock();
		}
	}

	public Material getMaterial() {
		return material;
	}
}