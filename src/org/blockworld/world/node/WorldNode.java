/**
 * WorldNode
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jme3tools.optimize.FastOctnode;
import jme3tools.optimize.Octree;

import org.apache.commons.lang3.Range;
import org.blockworld.core.asset.BlockWorldAssetManager;
import org.blockworld.util.GeometryBuilder;
import org.blockworld.world.Block;
import org.blockworld.world.BlockVolume;
import org.blockworld.world.TerrainChunkLocationIterator;
import org.blockworld.world.chunk.BlockChunk;
import org.blockworld.world.chunk.Matrix3DChunk;
import org.blockworld.world.loader.BlockLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;

/**
 * @author Matt Teeter
 * 
 */
public class WorldNode extends Node {
	private static final Logger LOG = LoggerFactory.getLogger(WorldNode.class);
	private final Lock getChunkLock = new ReentrantLock();
	private final BlockingQueue<AbstractChunkNode> chunksToFill = new LinkedBlockingQueue<AbstractChunkNode>(1500);
	private final BlockingQueue<AbstractChunkNode> filledChunks = new LinkedBlockingQueue<AbstractChunkNode>(1500);
	private final BlockingQueue<AbstractChunkNode> chunksToUpdate = new LinkedBlockingQueue<AbstractChunkNode>(20);
	private final BlockingQueue<AbstractChunkNode> updatedChunks = new LinkedBlockingQueue<AbstractChunkNode>(20);
	private final Map<Vector3f, AbstractChunkNode> loadedChunks = Maps.newHashMap();
	private final BlockLoader<Block> terrainLoader;
	private final BlockWorldAssetManager bloxelFactory;
	private final PhysicsSpace physicsSpace;
	private final Camera camera;
	private final Node chunks = new Node("chunks");
	private final Node debugNode = new Node("debug");
	private final float chunkSize;
	private final BoundingBox horizontBoundingBox;
	private Set<AbstractChunkNode> visibleChunks = Sets.newLinkedHashSet();
	private final ExecutorService threadPool = Executors.newFixedThreadPool(8, new ThreadFactory() {
		@Override
		public Thread newThread(final Runnable r) {
			final Thread th = new Thread(r);
			th.setDaemon(true);
			return th;
		}
	});
	private Geometry[] globalGeoms;
	private FastOctnode fastRoot;
	private BoundingBox octBox;

	public WorldNode(final BlockWorldAssetManager theBlockWorldFactory, final BlockLoader<Block> theTerrainLoader) {
		this(theBlockWorldFactory, theTerrainLoader, null, null, 16, 256);
	}

	public WorldNode(final BlockWorldAssetManager theBloxelFactory, final BlockLoader<Block> theTerrainLoader, final PhysicsSpace thePhysicsSpace, final Camera aCamera, final float theChunkSize, final float horizont) {
		super("world-node");
		attachChild(chunks);
		bloxelFactory = theBloxelFactory;
		physicsSpace = thePhysicsSpace;
		camera = aCamera;
		chunkSize = theChunkSize;
		terrainLoader = theTerrainLoader;
		threadPool.execute(new FillChunkNodesDispatcher());
		threadPool.execute(new UpdateChunkNodesDispatcher());
		horizontBoundingBox = new BoundingBox(Vector3f.ZERO, horizont, horizont / 2, horizont);
		final Geometry horizontDebugBox = GeometryBuilder.geometry().mesh(new WireBox(horizont, horizont / 2, horizont)).material(bloxelFactory.getMaterial(-1)).get();
		horizontDebugBox.setQueueBucket(Bucket.Opaque);
		debugNode.attachChild(horizontDebugBox);
		float hd = 0.5f;
		debugNode.attachChild(GeometryBuilder.geometry().mesh(new Grid((int) hd, (int) hd, 0.5f)).material(bloxelFactory.getMaterial(-1)).localTranslation(-hd / 4, 0, -hd / 4).get());
		attachChild(debugNode);
	}

	private AbstractChunkNode createChunkNode(final Vector3f aChunkLocation, final boolean lazyFilling) {
		final Matrix3DChunk chunk = new Matrix3DChunk(aChunkLocation, chunkSize);
		final AbstractChunkNode result = new FacesMeshChunkNode(bloxelFactory, chunk, this);
		if (lazyFilling) {
			LOG.debug("lazy fill new chunk for " + aChunkLocation + "/" + result);
			chunksToFill.add(result);
		} else {
			LOG.debug("create new chunk for " + aChunkLocation + "...");
			fillChunk(result, terrainLoader, filledChunks);
			LOG.debug("created new chunk for " + aChunkLocation);
		}
		return result;
	}

	private void fillChunk(final AbstractChunkNode chunk, final BlockLoader<Block> terrainLoader, final BlockingQueue<AbstractChunkNode> resultQueue) {
		chunk.getTerrainChunk().fill(terrainLoader);
		chunk.calculate();
		resultQueue.add(chunk);
	}

	/**
	 * Calculate the center position of a {@link BloxelChunk} which contains the given point <code>aWorldLocation</code>. The returned {@link Vector3f} would be used to get the right {@link BloxelChunk} from the loaded chunks (or to create a chunk for that position).
	 * 
	 * @param aWorldLocation
	 *            in world coordinates
	 * @return the location of a {@link BloxelChunk} which contains the given position
	 */
	private Vector3f getChunkLocation(final Vector3f aWorldLocation) {
		return calcChunkLocation(aWorldLocation, chunkSize);
	}

	public AbstractChunkNode getChunkNode(final Vector3f aLocation) {
		final Vector3f chunkLocation = getChunkLocation(aLocation);
		synchronized (getChunkLock) {
			return loadedChunks.get(chunkLocation);
		}
	}

	public float getChunkSize() {
		return chunkSize;
	}

	public int getChunkToFillCount() {
		return chunksToFill.size();
	}

	public float getHeightAt(final Vector3f theLocation) {
		Vector3f l = theLocation;
		float lastHeight = 0;
		float lastCheck = 0;
		while (true) {
			final AbstractChunkNode cn = getOrCreateChunkNode(l, false);
			final float h[] = getHeightAt(cn, theLocation);
			LOG.debug(String.format("%s: lc:%f,lh:%f : h0:%f,h1:%f", cn, lastCheck, lastHeight, h[0], h[1]));
			if (h[0] == -1) {
				if (lastCheck == 1) {
					// avoid endless loop between two chunks
					return lastHeight;
				}
				lastCheck = h[0];
				lastHeight = h[1];
				l = l.subtract(0, chunkSize * 2, 0);
			} else if (h[0] == 1) {
				if (lastCheck == -1) {
					// avoid endless loop between two chunks
					return lastHeight;
				}
				lastCheck = h[0];
				lastHeight = h[1];
				l = l.add(0, chunkSize * 2, 0);
			} else if (h[0] == 0) {
				return h[1];
			}
		}
	}

	public int getLoadedChunkCount() {
		return loadedChunks.size();
	}

	/**
	 * This method will find the correct {@link BlockChunk} which contains the given position. If the loaded chunks doesn't contain the position more chunks must loaded, still "visible" chunks must moved (x,y,z), "invisible" chunks must persists etc.
	 * 
	 * @param aLocation
	 *            the location relative to the center of this {@link WorldNode}
	 * @param lazyLoading
	 * @return the {@link BlockChunk} which contains the given location
	 */
	private AbstractChunkNode getOrCreateChunkNode(final Vector3f aLocation, final boolean lazyLoading) {
		final Vector3f chunkLocation = getChunkLocation(aLocation);
		synchronized (getChunkLock) {
			AbstractChunkNode chunk = loadedChunks.get(chunkLocation);
			if (chunk == null) {
				loadedChunks.put(chunkLocation, chunk = createChunkNode(chunkLocation, lazyLoading));
			}
			return chunk;
		}
	}

	public int getVisibleChunkCount() {
		return visibleChunks.size();
	}

	private Set<AbstractChunkNode> getVisibleChunks(final Vector3f theLocation, final Vector3f direction) {
		horizontBoundingBox.setCenter(theLocation);
		final Set<AbstractChunkNode> result = Sets.newHashSet();
		for (final AbstractChunkNode cn : loadedChunks.values()) {
			if (horizontBoundingBox.contains(cn.getTerrainChunk().getVolume().getBoundingBox().getCenter())) {
				result.add(cn);
			}
		}
		return result;
	}

	public void init(final Vector3f theLocation) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				LOG.debug("init:" + theLocation);
				// final float hx = horizontBoundingBox.getXExtent() / (8 *
				// chunkSize);
				// final float hy = horizontBoundingBox.getYExtent() / (2 *
				// chunkSize);
				// final float hz = horizontBoundingBox.getZExtent() / (8 *
				// chunkSize);
				final float hx = 16;
				final float hy = 0f;
				final float hz = 16;
				LOG.debug(hx + ":" + hy + ":" + hz);
				final TerrainChunkLocationIterator li = new TerrainChunkLocationIterator(theLocation, Vector3f.ZERO, new Vector3f(hx, hy, hz), chunkSize);
				final List<Vector3f> area = Lists.newArrayList(li);
				Collections.sort(area, new Comparator<Vector3f>() {

					@Override
					public int compare(final Vector3f o1, final Vector3f o2) {
						final float d1 = theLocation.distance(o1);
						final float d2 = theLocation.distance(o2);
						return Float.valueOf(d1).compareTo(Float.valueOf(d2));
					}
				});
				int count = 0;
				for (final Vector3f pos : area) {
					LOG.debug("[" + (count++) + "]Init chunk " + pos);
					getOrCreateChunkNode(pos, false);
					// if (count++ == 3) {
					// count = 0;
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
					}
					// }
				}
				LOG.debug("init:" + theLocation + " DONE!");
			}
		});
	}

	/**
	 * @return
	 */
	public boolean needUpdate() {
		return !updatedChunks.isEmpty() || !filledChunks.isEmpty();
	}

	public void removeBloxel(final Vector3f theLocation) {
		final AbstractChunkNode c = getChunkNode(theLocation);
		if (c == null) {
			return;
		}
		LOG.debug(String.format("Remove bloxel %s from chunk %s", theLocation, c));
		c.removeBloxel(theLocation);
		// immediately update the chunk
		c.calculate();
		updatedChunks.add(c);
		updateNeighborChunks(c, theLocation);
	}

	public void setBloxel(final Vector3f theLocation, final Integer theBoxelType) {
		final AbstractChunkNode c = getChunkNode(theLocation);
		if (c == null) {
			return;
		}
		LOG.debug(String.format("Add bloxel %s to chunk %s", theLocation, c));
		c.setBloxel(theLocation, theBoxelType);
		// immediately update the chunk
		c.calculate();
		updatedChunks.add(c);
		updateNeighborChunks(c, theLocation);
	}

	public void update(final Vector3f theLocation, final Vector3f direction) {
		// check if nodes around the player position exists
		final TerrainChunkLocationIterator li = new TerrainChunkLocationIterator(theLocation, Vector3f.ZERO, new Vector3f(1, 1, 1), chunkSize);
		while (li.hasNext()) {
			getOrCreateChunkNode(li.next(), true);
		}
		// getOrCreateChunkNode(theLocation.add(direction.mult(chunkSize)), true);
		boolean updateScene = false;
		AbstractChunkNode n = filledChunks.poll();
		if (n != null) {
			LOG.debug("Insert new filled chunk " + n);
			n.update(theLocation, direction);
			updateScene = true;
		}
		n = updatedChunks.poll();
		if (n != null) {
			LOG.debug("Update changed chunk " + n);
			n.update(theLocation, direction);
			updateScene = true;
		}
		horizontBoundingBox.setCenter(theLocation);
		debugNode.setLocalTranslation(theLocation);
		for (final AbstractChunkNode cn : Lists.newArrayList(loadedChunks.values())) {
			if (horizontBoundingBox.contains(cn.getTerrainChunk().getVolume().getBoundingBox().getCenter())) {
				chunks.attachChild(cn);
			} else {
				chunks.detachChild(cn);
			}
		}
		// visibleChunks = getVisibleChunks(theLocation, direction);
		// if (updateScene) {
		// chunks.detachAllChildren();
		// for (final Geometry geometry :
		// GeometryBatchFactory.makeBatches(getGeometries(loadedChunks.values())))
		// {
		// chunks.attachChild(geometry);
		// }
		// }
		// XXX auch zu langsam
		// final List<Geometry> g = Lists.newArrayList();
		// for (final AbstractChunkNode cn : visibleChunks) {
		// final List<Spatial> children = cn.getChildren();
		// for (final Spatial c : children) {
		// g.add((Geometry) c);
		// }
		// }
		// final List<Geometry> geos = GeometryBatchFactory.makeBatches(g);
		// chunks.detachAllChildren();
		// for (final Geometry geometry : geos) {
		// chunks.attachChild(geometry);
		// }
		// XXX from TestOctree ... works not very well, octree-construction
		// takes to long
		// if (fastRoot != null) {
		// final Set<Geometry> renderSet = new HashSet<Geometry>(300);
		// fastRoot.generateRenderSet(globalGeoms, renderSet, camera, octBox,
		// true);
		// LOG.debug(String.format("%d geometries in renderset",
		// renderSet.size()));
		// chunks.detachAllChildren();
		// for (final Geometry geometry : renderSet) {
		// chunks.attachChild(geometry);
		// }
		// }
	}

	/**
	 * @param centerChunk
	 *            the changed chunk
	 * @param theChangedBloxelLocation
	 *            of the bloxel inside the given {@link AbstractChunkNode centerChunk}
	 */
	private void updateNeighborChunks(final AbstractChunkNode centerChunk, final Vector3f theChangedBloxelLocation) {
		final BoundingBox bb = centerChunk.getTerrainChunk().getVolume().getBoundingBox();
		if (!bb.intersects(theChangedBloxelLocation)) {
			throw new IllegalArgumentException("Changed bloxel is not inside the chunk can't do neighbor chunk update");
		}
		// find faces for the changed bloxel, then update the chunks at this
		// faces
		final Vector3f center = bb.getCenter();
		final float elementSize = centerChunk.getTerrainChunk().getVolume().getElementSize();
		final Set<AbstractChunkNode> neighbors = Sets.newHashSet();
		final float x = theChangedBloxelLocation.x;
		final float y = theChangedBloxelLocation.y;
		final float z = theChangedBloxelLocation.z;
		final float edgeLeft = center.x - bb.getXExtent() + elementSize;
		final float edgeRight = center.x + bb.getXExtent() - elementSize;
		final float edgeBottom = center.y - bb.getYExtent() + elementSize;
		final float edgeTop = center.y - bb.getYExtent() - elementSize;
		final float edgeBack = center.z - bb.getZExtent() + elementSize;
		final float edgeFront = center.z - bb.getZExtent() - elementSize;
		if (x <= edgeLeft) {
			// left face
			final Vector3f cl = new Vector3f(edgeLeft - chunkSize, y, z);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Left:" + cl + "/" + x + "/" + edgeLeft + "::" + cn);
			neighbors.add(cn);
		}
		if (x == edgeRight) {
			// right face
			final Vector3f cl = new Vector3f(edgeRight + chunkSize, y, z);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Right:" + cl + "/" + x + "/" + edgeRight + "::" + cn);
			neighbors.add(cn);
		}
		if (y <= edgeBottom) {
			// bottom face
			final Vector3f cl = new Vector3f(x, edgeBottom - chunkSize, z);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Bottom:" + cl + "/" + y + "/" + edgeBottom + "::" + cn);
			neighbors.add(cn);
		}
		if (y == edgeTop) {
			// top face
			final Vector3f cl = new Vector3f(x, edgeTop + chunkSize, z);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Top:" + cl + "/" + y + "/" + edgeTop + "::" + cn);
			neighbors.add(cn);
		}
		if (z == edgeBack) {
			// back face
			final Vector3f cl = new Vector3f(x, y, edgeBack - chunkSize);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Back:" + cl + "/" + z + "/" + edgeBack + "::" + cn);
			neighbors.add(cn);
		}
		if (z == edgeFront) {
			// front face
			final Vector3f cl = new Vector3f(x, y, edgeFront + chunkSize);
			final AbstractChunkNode cn = getChunkNode(cl);
			System.out.println("Front:" + cl + "/" + z + "/" + edgeFront + "::" + cn);
			neighbors.add(cn);
		}
		neighbors.remove(centerChunk);
		LOG.debug(String.format("Changed bloxel force lazy update of %d neigbor chunks", neighbors.size()));
		for (final AbstractChunkNode n : neighbors) {
			if (n == null) {
				// skip none existing neighbor chunks
				continue;
			}
			LOG.debug(String.format("Changed bloxel force lazy update neigbor chunk '%s'", n));
			n.getTerrainChunk().setDirty(true);
			chunksToUpdate.add(n);
		}
	}

	private void updateSceneOctree() {
		updateSceneOctree(null, null);
	}

	private void updateSceneOctree(final Vector3f theLocation, final Vector3f direction) {
		final Node scene = new Node("scene");
		if (theLocation != null && direction != null) {
			for (final AbstractChunkNode cn : visibleChunks = getVisibleChunks(theLocation, direction)) {
				scene.attachChild(cn);
			}
		} else {
			for (final AbstractChunkNode cn : loadedChunks.values()) {
				scene.attachChild(cn);
			}
		}
		LOG.debug("Create scene octree");
		final Octree sceneOctree = new Octree(scene, 1000);
		LOG.debug("Create scene octree done");
		LOG.debug("Construct octree");
		sceneOctree.construct();
		LOG.debug("Construct octree done");
		final ArrayList<Geometry> globalGeomList = new ArrayList<Geometry>();
		sceneOctree.createFastOctnodes(globalGeomList);
		sceneOctree.generateFastOctnodeLinks();
		globalGeoms = globalGeomList.toArray(new Geometry[0]);
		fastRoot = sceneOctree.getFastRoot();
		octBox = sceneOctree.getBound();
	}

	/**
	 * Translate a world location into chunk location.
	 * 
	 * @param aLocation
	 *            to use
	 * @param theChunkSize
	 *            to use
	 * @return the location of a {@link BlockChunk} which can handle the given <code>aLocation</code>
	 */
	private static Vector3f calcChunkLocation(final Vector3f aLocation, final float theChunkSize) {
		final int x = getChunkIndexForAxis(aLocation.x, theChunkSize);
		final int y = getChunkIndexForAxis(aLocation.y, theChunkSize);
		final int z = getChunkIndexForAxis(aLocation.z, theChunkSize);
		return new Vector3f(x, y, z).mult(2 * theChunkSize);
	}

	static int getChunkIndexForAxis(final float theChunkLocation, final float theChunkSize) {
		int i = 0;
		int sign = 1;
		final Range<Float> zeroChunkRange = Range.between(-theChunkSize, theChunkSize);
		if (!(zeroChunkRange.contains(theChunkLocation))) {
			if (theChunkLocation < 0) {
				sign = -1;
			}
			i = (((int) ((Math.abs(theChunkLocation) - theChunkSize) / (2 * theChunkSize))) + 1) * sign;
		}
		return i;
	}

	private static List<Geometry> getGeometries(final Collection<AbstractChunkNode> nodes) {
		final List<Geometry> geoms = new ArrayList<Geometry>();
		for (final Node n : nodes) {
			for (final Spatial child : n.getChildren()) {
				geoms.add((Geometry) child);
			}
		}
		return geoms;
	}

	static float[] getHeightAt(final AbstractChunkNode node, final Vector3f theLocation) {
		final BlockVolume<Block> volume = node.getTerrainChunk().getVolume();
		final BoundingBox bv = volume.getBoundingBox();
		final Vector3f center = bv.getCenter();
		final float elementSize = volume.getElementSize();
		final float yExtent = bv.getYExtent();
		final float step = elementSize * 2;
		final float mapx = (Math.round(theLocation.x / (step)) * step);
		final float mapz = (Math.round(theLocation.z / (step)) * step);

		float maxHeight = Float.MIN_VALUE;
		int heightElementsCount = 0;
		for (float y = yExtent * 2; y >= 0; y -= step) {
			final float mapy = center.y - yExtent + y * step;
			final Block bloxel = volume.getBlock(mapx, mapy, mapz);
			if (bloxel != null) {
				maxHeight = Math.max(maxHeight, bloxel.getCenter().y);
				heightElementsCount++;
			}
		}
		heightElementsCount--;
		LOG.debug(heightElementsCount + ":" + maxHeight);
		if (heightElementsCount == -1) {
			return new float[] { -1, center.y - yExtent + elementSize };
		}
		if (heightElementsCount == Math.round(yExtent / elementSize)) {
			return new float[] { 1, center.y + yExtent - elementSize };
		}
		return new float[] { 0, maxHeight };
	}

	private class FillChunkNodesDispatcher implements Runnable {

		@Override
		public void run() {
			LOG.debug("FillChunkNodesDispatcher started.");
			while (true) {
				try {
					LOG.debug("FillChunkNodesDispatcher wait for more nodes to fill ...");
					final AbstractChunkNode n = chunksToFill.take();
					threadPool.execute(new FillChunkNodeThread(n));
				} catch (final InterruptedException e) {
					LOG.debug("FillChunkNodesDispatcher was interrupted");
					break;
				}
			}
		}
	}

	private class FillChunkNodeThread implements Runnable {
		private final AbstractChunkNode chunkNodeToFill;

		public FillChunkNodeThread(final AbstractChunkNode nodeToFill) {
			this.chunkNodeToFill = nodeToFill;
		}

		@Override
		public void run() {
			fillChunk(chunkNodeToFill, terrainLoader, filledChunks);
		}
	}

	private class UpdateChunkNodesDispatcher implements Runnable {

		@Override
		public void run() {
			LOG.debug("UpdateChunkNodesDispatcher started.");
			while (true) {
				try {
					LOG.debug("UpdateChunkNodesDispatcher wait for more nodes to update ...");
					final AbstractChunkNode n = chunksToUpdate.take();
					threadPool.execute(new UpdateChunkNodeThread(n));
				} catch (final InterruptedException e) {
					LOG.debug("UpdateChunkNodesDispatcher was interrupted");
					break;
				}
			}
		}
	}

	private class UpdateChunkNodeThread implements Runnable {
		private final AbstractChunkNode chunkNodeToUpdate;

		public UpdateChunkNodeThread(final AbstractChunkNode chunkNodeToUpdate) {
			this.chunkNodeToUpdate = chunkNodeToUpdate;
		}

		@Override
		public void run() {
			chunkNodeToUpdate.calculate();
			updatedChunks.add(chunkNodeToUpdate);
		}
	}
}
