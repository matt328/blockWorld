/**
 * FacesMeshChunkNode
 * Author: Matt Teeter
 * Apr 20, 2012
 */
package org.blockworld.world.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jme3tools.optimize.GeometryBatchFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.blockworld.core.asset.BlockWorldAssetManager;
import org.blockworld.util.GeometryBuilder;
import org.blockworld.world.Block;
import org.blockworld.world.BlockVolume;
import org.blockworld.world.Matrix3DVolume;
import org.blockworld.world.chunk.BlockChunk;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * @author Matt Teeter
 * 
 */
public class FacesMeshChunkNode extends AbstractChunkNode {
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

	private static final int FACE_NO = 0;
	private static final int FACE_RIGHT = 1;
	private static final int FACE_LEFT = 2;
	private static final int FACE_UP = 4;
	private static final int FACE_DOWN = 8;
	private static final int FACE_FRONT = 16;
	private static final int FACE_BACK = 32;

	private static final ArrayList<Integer> TRIANGLE_INDIZES = Lists.newArrayList(2, 0, 1, 1, 3, 2);

	private final WorldNode world;

	private Multimap<Integer, Vector3f> vertices; // points
	private Multimap<Integer, Vector3f> normals; // normals
	private Multimap<Integer, Vector2f> texCoord; // texture coords
	private Multimap<Integer, Vector2f> lightCoord; // light texture coords
	private Multimap<Integer, Integer> indexes; // indexes

	public FacesMeshChunkNode(final BlockWorldAssetManager theAssetManager, final BlockChunk<Block> aTerrainChunk) {
		this(theAssetManager, aTerrainChunk, null);
	}

	public FacesMeshChunkNode(final BlockWorldAssetManager theAssetManager, final BlockChunk<Block> aTerrainChunk, final WorldNode worldNode) {
		super(theAssetManager, aTerrainChunk);
		world = worldNode;
	}

	@Override
	protected List<Geometry> createGeometries() {
		vertices = ArrayListMultimap.create();
		normals = ArrayListMultimap.create();
		texCoord = ArrayListMultimap.create();
		indexes = ArrayListMultimap.create();
		lightCoord = ArrayListMultimap.create();

		final BlockVolume<Block> volume = terrainChunk.getVolume();
		LOG.debug(String.format("Tesselate volume %s", volume.getBoundingBox()));

		int c = 0;
		final Set<Integer> usedBlockTypes = Sets.newHashSet();

		final Matrix3DVolume<Block> o = (Matrix3DVolume<Block>) volume;
		final List<Block> leaves = Matrix3DVolume.getLeaves(o);

		LOG.debug("Check " + leaves.size() + " elements / volume can max contains " + o.getBoundingBox().getVolume() / o.getElementSize() + " elements");

		for (final Block data : leaves) {
			final Integer blockType = data.getType();
			final Vector3f blockPosition = data.getCenter();
			final int faces = checkFaces(blockPosition, data.getDimension() * 2);
			final float x = blockPosition.x;
			final float y = blockPosition.y;
			final float z = blockPosition.z;
			createFaces(x, y, z, data.getDimension(), faces, blockType);
			c++;
			usedBlockTypes.add(blockType);
		}

		LOG.debug("Found " + c + " boxes with " + usedBlockTypes.size() + " different types");
		final List<Geometry> result = Lists.newArrayList();
		for (final Integer blockType : usedBlockTypes) {
			final Mesh chunkMesh = new Mesh();
			chunkMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.get(blockType).toArray(new Vector3f[vertices.size()])));
			LOG.debug("Material " + blockType + " have " + vertices.size() + " vertices");
			chunkMesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals.get(blockType).toArray(new Vector3f[normals.size()])));
			LOG.debug("Material " + blockType + " have " + normals.size() + " normals");
			chunkMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.get(blockType).toArray(new Vector2f[texCoord.size()])));
			LOG.debug("Material " + blockType + " have " + texCoord.size() + " texCoord");
			chunkMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(ArrayUtils.toPrimitive(indexes.get(blockType).toArray(new Integer[0]))));
			LOG.debug("Material " + blockType + " have " + indexes.get(blockType).size() + " indexes");
			chunkMesh.setBuffer(Type.TexCoord2, 2, BufferUtils.createFloatBuffer(lightCoord.get(blockType).toArray(new Vector2f[lightCoord.size()])));
			LOG.debug("Material " + blockType + " have " + lightCoord.get(blockType).size() + " lightCoord");
			// important to update the bound for correct
			// node-world-bound-calculation
			chunkMesh.updateBound();
			chunkMesh.setStatic();
			if (chunkMesh.getVertexCount() != 0) {
				final Material material = assetManager.getMaterial(blockType);
				if(material.getActiveTechnique() == null) {
					LOG.debug("Material: {} has no Active Technique. WTF?", material.getName());
				}
				LOG.debug("Material name: {}", material.getActiveTechnique());
				final Geometry geometry = GeometryBuilder.geometry("b-" + blockType).mesh(chunkMesh).material(material).get();
				if (material.isTransparent()) {
					geometry.setQueueBucket(Bucket.Transparent);
				}
				result.add(geometry);
			}
		}
		vertices = null;
		normals = null;
		texCoord = null;
		indexes = null;
		lightCoord = null;
		return GeometryBatchFactory.makeBatches(result);
	}

	private boolean createFaces(float x, float y, float z, float dimension, int faces, Integer blockType) {
		LOG.trace(String.format("Create faces [%s] for %f, %f, %f and %f", facesToString(faces), x, y, z, dimension));
		if ((faces & FACE_NO) > 0) {
			return false;
		}

		final Vector3f pa = new Vector3f(x - dimension, y - dimension, z + dimension);
		final Vector3f pb = new Vector3f(x + dimension, y - dimension, z + dimension);
		final Vector3f pc = new Vector3f(x - dimension, y + dimension, z + dimension);
		final Vector3f pd = new Vector3f(x + dimension, y + dimension, z + dimension);
		final Vector3f pe = new Vector3f(x - dimension, y - dimension, z - dimension);
		final Vector3f pf = new Vector3f(x + dimension, y - dimension, z - dimension);
		final Vector3f pg = new Vector3f(x - dimension, y + dimension, z - dimension);
		final Vector3f ph = new Vector3f(x + dimension, y + dimension, z - dimension);

		// TODO calculate light value depending on the position of the block
		// and the neighbors
		final List<Vector2f> lightMapCoord = lightTextureCoord(9);

		if ((faces & FACE_BACK) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pa, pb, pc, pd));
			normals.get(blockType).addAll(NORMALS_BACKFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_BACK));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}
		if ((faces & FACE_FRONT) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pf, pe, ph, pg));
			normals.get(blockType).addAll(NORMALS_FRONTFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_FRONT));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}
		if ((faces & FACE_RIGHT) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pb, pf, pd, ph));
			normals.get(blockType).addAll(NORMALS_RIGHTFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_RIGHT));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}
		if ((faces & FACE_LEFT) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pe, pa, pg, pc));
			normals.get(blockType).addAll(NORMALS_LEFTFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_LEFT));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}
		if ((faces & FACE_UP) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pc, pd, pg, ph));
			normals.get(blockType).addAll(NORMALS_UPFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_UP));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}
		if ((faces & FACE_DOWN) > 0) {
			final int verticesSize = vertices.get(blockType).size();
			vertices.get(blockType).addAll(Lists.newArrayList(pe, pf, pa, pb));
			normals.get(blockType).addAll(NORMALS_DOWNFACE);
			texCoord.get(blockType).addAll(assetManager.getTextureCoordinates(blockType, FACE_DOWN));
			indexes.get(blockType).addAll(verticesIndex(verticesSize, TRIANGLE_INDIZES));
			lightCoord.get(blockType).addAll(lightMapCoord);
		}

		return true;
	}

	private int checkFaces(Vector3f blockPosition, float dimension) {
		final float x = blockPosition.x;
		final float y = blockPosition.y;
		final float z = blockPosition.z;
		LOG.trace(String.format("Check faces for %f, %f, %f and %f", x, y, z, dimension));
		final BlockVolume<Block> v = terrainChunk.getVolume();
		final Block currentBlock = v.getBlock(x, y, z);
		int faces = FACE_NO;
		if (needFace(v, currentBlock, x + dimension, y, z)) {
			faces |= FACE_RIGHT;
		}
		if (needFace(v, currentBlock, x - dimension, y, z)) {
			faces |= FACE_LEFT;
		}
		if (needFace(v, currentBlock, x, y + dimension, z)) {
			faces |= FACE_UP;
		}
		if (needFace(v, currentBlock, x, y - dimension, z)) {
			faces |= FACE_DOWN;
		}
		if (needFace(v, currentBlock, x, y, z + dimension)) {
			faces |= FACE_BACK;
		}
		if (needFace(v, currentBlock, x, y, z - dimension)) {
			faces |= FACE_FRONT;
		}
		return faces;
	}

	private boolean needFace(BlockVolume<Block> v, Block currentBlock, float x, float y, float z) {
		if (v.contains(x, y, z)) {
			return needFace(currentBlock, v.getBlock(x, y, z));
		} else {
			// outside neighbor block : is not part of this chunk
			if (world == null) {
				// no connection between chunks possible, each chunk knows only
				// his "own" volume
				return true;
			}
			final AbstractChunkNode neighborChunk = world.getChunkNode(new Vector3f(x, y, z));
			if (neighborChunk == null) {
				// the neighbor chunk is (currently) not available!
				return false;
			}
			return needFace(currentBlock, neighborChunk.getTerrainChunk().getVolume().getBlock(x, y, z));
		}
	}

	private boolean needFace(final Block currentBlock, final Block neighborBloxelToCheck) {
		Preconditions.checkNotNull(currentBlock, "current bloxel must never be null");
		if (neighborBloxelToCheck == null) {
			// neighbor bloxel is air then it doesn't matter if current bloxel
			// is translucent we always need a face here
			return true;
		}
		if (isTranslucentBloxel(currentBlock)) {
			// current bloxel is translucent
			// we need a face if the neighbor bloxel is not translucent
			// we need a face if the neighbor bloxel is also translucent but
			// with a different type
			return !isTranslucentBloxel(neighborBloxelToCheck) || currentBlock.getType() != neighborBloxelToCheck.getType();
		}
		// normal current bloxel
		// we need a face if the neighbor bloxel is translucent
		return isTranslucentBloxel(neighborBloxelToCheck);
	}

	private boolean isTranslucentBloxel(final Block checkBloxel) {
		return isTranslucentBloxel(checkBloxel.getType());
	}

	private boolean isTranslucentBloxel(final Integer bloxelType) {
		return assetManager.isTransparent(bloxelType);
	}

	private String facesToString(final int faces) {
		String result = "";
		if ((faces & FACE_BACK) > 0) {
			result += "back,";
		}
		if ((faces & FACE_FRONT) > 0) {
			result += "front,";
		}
		if ((faces & FACE_LEFT) > 0) {
			result += "left,";
		}
		if ((faces & FACE_RIGHT) > 0) {
			result += "right,";
		}
		if ((faces & FACE_UP) > 0) {
			result += "top,";
		}
		if ((faces & FACE_DOWN) > 0) {
			result += "bottom";
		}
		return result;
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
