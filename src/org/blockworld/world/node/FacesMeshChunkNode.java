/**
 * FacesMeshChunkNode
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import jme3tools.optimize.GeometryBatchFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.util.GeometryBuilder;
import org.blockworld.world.Block;
import org.blockworld.world.BlockChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;

/**
 * @author Matt Teeter
 * 
 */
public class FacesMeshChunkNode extends AbstractChunkNode {
	private static final Logger LOG = LoggerFactory.getLogger(FacesMeshChunkNode.class);
	private static final Vector3f NORMAL_UP = new Vector3f(0, 1, 0);
	private static final Vector3f NORMAL_DOWN = new Vector3f(0, -1, 0);
	private static final Vector3f NORMAL_RIGHT = new Vector3f(1, 0, 0);
	private static final Vector3f NORMAL_LEFT = new Vector3f(-1, 0, 0);
	private static final Vector3f NORMAL_FRONT = new Vector3f(0, 0, 1);
	private static final Vector3f NORMAL_BACK = new Vector3f(0, 0, -1);

	private static final ArrayList<Vector3f> NORMALS_DOWNFACE = Lists.newArrayList(NORMAL_DOWN, NORMAL_DOWN, NORMAL_DOWN, NORMAL_DOWN);
	private static final ArrayList<Vector3f> NORMALS_UPFACE = Lists.newArrayList(NORMAL_UP, NORMAL_UP, NORMAL_UP, NORMAL_UP);
	private static final ArrayList<Vector3f> NORMALS_LEFTFACE = Lists.newArrayList(NORMAL_LEFT, NORMAL_LEFT, NORMAL_LEFT, NORMAL_LEFT);
	private static final ArrayList<Vector3f> NORMALS_RIGHTFACE = Lists.newArrayList(NORMAL_RIGHT, NORMAL_RIGHT, NORMAL_RIGHT, NORMAL_RIGHT);
	private static final ArrayList<Vector3f> NORMALS_FRONTFACE = Lists.newArrayList(NORMAL_FRONT, NORMAL_FRONT, NORMAL_FRONT, NORMAL_FRONT);
	private static final ArrayList<Vector3f> NORMALS_BACKFACE = Lists.newArrayList(NORMAL_BACK, NORMAL_BACK, NORMAL_BACK, NORMAL_BACK);

	/**
	 * <pre>
	 *     pg-----ph
	 *    /|      /|
	 *   pc-----pd |
	 *   | pe----|pf
	 *   |/      |/  
	 *   pa-----pb
	 *   
	 *   2\2--3
	 *   | \  | Counter-clockwise -> then this is the front of the polygon
	 *   |  \ |
	 *   0--1\1
	 * 
	 * </pre>
	 */
	private static final ArrayList<Integer> TRIANGLE_INDIZES = Lists.newArrayList(2, 0, 1, 1, 3, 2);

	private final ChunkSetNode world;

	private Multimap<Integer, Vector3f> vertices; // points
	private Multimap<Integer, Vector3f> normals; // normals
	private Multimap<Integer, Vector2f> texCoord; // texture coords
	private Multimap<Integer, Vector2f> lightCoord; // light texture coords
	private Multimap<Integer, Integer> indexes; // indexes

	private final TextureAtlas atlas;

	/**
	 * @param terrainChunk
	 */
	public FacesMeshChunkNode(BlockChunk<Block> terrainChunk, ChunkSetNode world, TextureAtlas atlas) {
		super(terrainChunk);
		this.world = world;
		this.atlas = atlas;
	}

	@Override
	protected List<Geometry> createGeometries() {
		final long start = System.currentTimeMillis();
		vertices = ArrayListMultimap.create();
		normals = ArrayListMultimap.create();
		texCoord = ArrayListMultimap.create();
		indexes = ArrayListMultimap.create();
		lightCoord = ArrayListMultimap.create();

		LOG.debug(String.format("Tesselating volume %s", terrainChunk.getBoundingBox()));

		int c = 0;
		final TIntHashSet usedBlockTypes = new TIntHashSet();

		final List<Block> leaves = terrainChunk.getLeaves();
		LOG.debug("Check " + leaves.size() + " elements / volume can max contains " + terrainChunk.getBoundingBox().getVolume() / terrainChunk.getElementSize() + " elements");

		final List<Geometry> result = Lists.newArrayList();

		for (Block data : leaves) {
			final int blockType = data.getType();
			final Vector3f blockPosition = data.getCenter();

			final EnumSet<Face> faces = checkFaces(blockPosition, data.getDimension() * 2);

//			if (!faces.isEmpty()) {
//				Box box = new Box(blockPosition, data.getDimension() / 2, data.getDimension() / 2, data.getDimension() / 2);
//				Geometry debugBox = new Geometry("Box" + blockPosition, box);
//				result.add(debugBox);
//
//				if (faces.contains(Face.FACE_UP)) {
//					debugBox.setMaterial(atlas.getBlueMaterial());
//				} else {
//					debugBox.setMaterial(atlas.getRedMaterial());
//				}
//			}
			createFaces(blockPosition, data.getDimension(), faces, blockType);
			//LOG.debug(String.format("Block at : %s, faces: %s", data.getCenter(), faces.toString()));
			c++;
			usedBlockTypes.add(blockType);
		}

		LOG.debug("Found " + c + " boxes with " + usedBlockTypes.size() + " different types");

		TIntIterator it = usedBlockTypes.iterator();
		while (it.hasNext()) {
			int blockType = it.next();
			LOG.debug("Build mesh for material " + blockType + " ...");
			final Mesh chunkMesh = new Mesh();
			chunkMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.get(blockType).toArray(new Vector3f[vertices.size()])));
			chunkMesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals.get(blockType).toArray(new Vector3f[normals.size()])));
			chunkMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.get(blockType).toArray(new Vector2f[texCoord.size()])));
			chunkMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(ArrayUtils.toPrimitive(indexes.get(blockType).toArray(new Integer[0]))));
			chunkMesh.setBuffer(Type.TexCoord2, 2, BufferUtils.createFloatBuffer(lightCoord.get(blockType).toArray(new Vector2f[lightCoord.size()])));
			chunkMesh.updateBound();
			chunkMesh.setStatic();
			if (chunkMesh.getVertexCount() != 0) {
				final Material material = atlas.getMaterial(blockType);
				final Geometry geometry = GeometryBuilder.geometry("b-" + blockType).mesh(chunkMesh).material(material).get();
				if (material.isTransparent()) {
					geometry.setQueueBucket(Bucket.Transparent);
				}
				result.add(geometry);
			}
		}
		// free memory
		vertices = null;
		normals = null;
		texCoord = null;
		indexes = null;
		lightCoord = null;
		long duration = (System.currentTimeMillis() - start);

		LOG.debug(String.format("Tesselating %d blocks took %dms", leaves.size(), duration));

		List<Geometry> toReturn = GeometryBatchFactory.makeBatches(result);
		return toReturn;
	}

	private EnumSet<Face> checkFaces(final Vector3f blockPosition, final float dimension) {
		final float x = blockPosition.x;
		final float y = blockPosition.y;
		final float z = blockPosition.z;
		LOG.trace(String.format("Check faces for %f, %f, %f and %f", x, y, z, dimension));
		final Block currentBlock = terrainChunk.getBlock(blockPosition);
		EnumSet<Face> faces = EnumSet.noneOf(Face.class);
		if (needFace(currentBlock, x + dimension, y, z)) {
			faces.add(Face.FACE_RIGHT);
		}
		if (needFace(currentBlock, x - dimension, y, z)) {
			faces.add(Face.FACE_LEFT);
		}
		if (needFace(currentBlock, x, y + dimension, z)) {
			faces.add(Face.FACE_UP);
		}
		if (needFace(currentBlock, x, y - dimension, z)) {
			faces.add(Face.FACE_DOWN);
		}
		if (needFace(currentBlock, x, y, z + dimension)) {
			faces.add(Face.FACE_BACK);
		}
		if (needFace(currentBlock, x, y, z - dimension)) {
			faces.add(Face.FACE_FRONT);
		}
		return faces;
	}

	private boolean needFace(final Block currentBlock, final float x, final float y, final float z) {
		if (terrainChunk.getBoundingBox().contains(new Vector3f(x, y, z))) {
			return needFace(currentBlock, terrainChunk.getBlock(new Vector3f(x, y, z)));
		} else {
			// outside neighbor : is not part of this chunk
			if (world == null) {
				// no connection between chunks possible, each chunk knows only his "own" volume
				return true;
			}
			final ChunkNode neighborChunk = world.getChunkNode(new Vector3f(x, y, z));
			if (neighborChunk == null) {
				// TODO: Need to fix this once neighbors become available
				return false;
			}
			return needFace(currentBlock, neighborChunk.getBlock(new Vector3f(x, y, z)));
		}
	}

	private boolean needFace(final Block current, final Block neighborToCheck) {
		Preconditions.checkNotNull(current, "current  must never be null");
		if (neighborToCheck == null) {
			return true;
		}
		if (isTranslucent(current)) {
			// current is translucent
			// we need a face if the neighbor block is not translucent
			// we need a face if the neighbor block is also translucent but with a different type
			return !isTranslucent(neighborToCheck) || current.getType() != neighborToCheck.getType();
		}
		// normal current
		// we need a face if the neighbor is translucent
		return isTranslucent(neighborToCheck);
	}

	private boolean isTranslucent(final Block checkBlock) {
		return isTranslucent(checkBlock.getType());
	}

	private boolean isTranslucent(final Integer Type) {
		return atlas.isTransparent(Type);
	}

	private boolean createFaces(Vector3f position, final float dimension, final EnumSet<Face> faces, final int bloxelType) {
		LOG.trace(String.format("Create faces [%s] for %f, %f, %f and %f", faces.toString(), position.x, position.y, position.z, dimension));
		if (faces.isEmpty()) {
			return false;
		}

		/**
		 * <pre>
		 *     pg-----ph
		 *    /|      /|
		 *   pc-----pd |
		 *   | pe----|pf
		 *   |/      | /  
		 *   pa-----pb
		 *   
		 *   2\2--3
		 *   | \  | Counter-clockwise
		 *   |  \ |
		 *   0--1\1
		 * 
		 * </pre>
		 */
		float x = position.x;
		float y = position.y;
		float z = position.z;
		final Vector3f pa = new Vector3f(x - dimension, y - dimension, z + dimension);
		final Vector3f pb = new Vector3f(x + dimension, y - dimension, z + dimension);
		final Vector3f pc = new Vector3f(x - dimension, y + dimension, z + dimension);
		final Vector3f pd = new Vector3f(x + dimension, y + dimension, z + dimension);
		final Vector3f pe = new Vector3f(x - dimension, y - dimension, z - dimension);
		final Vector3f pf = new Vector3f(x + dimension, y - dimension, z - dimension);
		final Vector3f pg = new Vector3f(x - dimension, y + dimension, z - dimension);
		final Vector3f ph = new Vector3f(x + dimension, y + dimension, z - dimension);

		final List<Vector2f> lightMapCoord = lightTextureCoord(9);
		// TODO calculate light value depending on the position of the block and the neighbors

		if (faces.contains(Face.FACE_BACK)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pa, pb, pc, pd));
			normals.get(bloxelType).addAll(NORMALS_BACKFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_BACK.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}
		if (faces.contains(Face.FACE_FRONT)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pf, pe, ph, pg));
			normals.get(bloxelType).addAll(NORMALS_FRONTFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_FRONT.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}
		if (faces.contains(Face.FACE_RIGHT)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pb, pf, pd, ph));
			normals.get(bloxelType).addAll(NORMALS_RIGHTFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_RIGHT.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}
		if (faces.contains(Face.FACE_LEFT)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pe, pa, pg, pc));
			normals.get(bloxelType).addAll(NORMALS_LEFTFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_LEFT.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}
		if (faces.contains(Face.FACE_UP)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pc, pd, pg, ph));
			normals.get(bloxelType).addAll(NORMALS_UPFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_UP.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}
		if (faces.contains(Face.FACE_DOWN)) {
			final int verticesSize = vertices.get(bloxelType).size();
			vertices.get(bloxelType).addAll(Lists.newArrayList(pe, pf, pa, pb));
			normals.get(bloxelType).addAll(NORMALS_DOWNFACE);
			texCoord.get(bloxelType).addAll(atlas.getTextureCoordinates(bloxelType, Face.FACE_DOWN.getValue()));
			indexes.get(bloxelType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(bloxelType).addAll(lightMapCoord);
		}

		return true;
	}

	private List<Vector2f> lightTextureCoord(final int lightValue) {
		// each image is 32x32, the whole image-atlas is 512x512
		// coord.x: 0..10
		// coord.y: 0..1
		final float sx = 32f / 512f;
		final float sy = 32f / 64f;
		final float x = lightValue * sx;
		final float y = sy;
		final Vector2f bottomLeft = new Vector2f(x, y);
		final Vector2f bottomRight = new Vector2f(x + sx, y);
		final Vector2f topLeft = new Vector2f(x, y + sy);
		final Vector2f topRight = new Vector2f(x + sx, y + sy);
		return Lists.newArrayList(bottomLeft, bottomRight, topLeft, topRight);
	}

	private ArrayList<Integer> verticesIndex(final int verticesSize, final ArrayList<Integer> indexes) {
		final ArrayList<Integer> result = Lists.newArrayList();
		for (final Integer i : indexes) {
			result.add(i + verticesSize);
		}
		return result;
	}
}