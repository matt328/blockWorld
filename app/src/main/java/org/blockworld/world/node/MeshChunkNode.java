/**
 * MeshChunkNode Author: Matt Teeter May 6, 2012
 */
package org.blockworld.world.node;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import jme3tools.optimize.GeometryBatchFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.util.GeometryBuilder;
import org.blockworld.world.Chunk;
import org.blockworld.world.loader.BlockworldBlockLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.jme3.util.BufferUtils;

/**
 * {@link MeshChunkNode}'s are the geometric representation of a chunk to JME.
 * This class takes in a {@link Chunk} and turns it's raw data into a
 * {@link List} of {@link Geometry} that JME can render. Right now this process
 * is incredibly tedious since for each solid block, all 6 of its neighbors must
 * be checked to determine if a face for that direction needs to be created. It
 * is currently capable of calculating {@link Geometry} for a {@link Chunk} of
 * 16x256x16 size in a few hundred milliseconds which should be acceptable.
 * 
 * @author Matt Teeter
 */
public class MeshChunkNode extends AbstractChunkNode {
   private final BlockworldBlockLoader loader;
   @SuppressWarnings("unused")
   private static final Logger LOG = LoggerFactory.getLogger(MeshChunkNode.class);
   private static final ArrayList<Integer> TRIANGLE_INDIZES = Lists.newArrayList(2, 0, 1, 1, 3, 2);
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

   private Multimap<Integer, Vector3f> vertices; // points
   private Multimap<Integer, Vector3f> normals; // normals
   private Multimap<Integer, Vector2f> texCoord; // texture coords
   private Multimap<Integer, Vector2f> lightCoord; // light texture coords
   private Multimap<Integer, Integer> indexes; // indexes

   private final TextureAtlas atlas;

   public MeshChunkNode(final Chunk terrainChunk, final TextureAtlas atlas, final BlockworldBlockLoader loader) {
      super(terrainChunk);
      this.loader = loader;
      this.atlas = atlas;
      vertices = ArrayListMultimap.create();
      normals = ArrayListMultimap.create();
      texCoord = ArrayListMultimap.create();
      indexes = ArrayListMultimap.create();
      lightCoord = ArrayListMultimap.create();
   }

   @Override
   protected List<Geometry> createGeometries() {
      final TIntSet usedBlockTypes = new TIntHashSet();
      final Vector3f chunkCenter = terrainChunk.getBoundingBox().getCenter();
      final int sx = (int) (chunkCenter.x - terrainChunk.getBoundingBox().getXExtent());
      final int sy = (int) (chunkCenter.y - terrainChunk.getBoundingBox().getYExtent());
      final int sz = (int) (chunkCenter.z - terrainChunk.getBoundingBox().getZExtent());
      final int ex = (int) (chunkCenter.x + terrainChunk.getBoundingBox().getXExtent());
      final int ey = (int) (chunkCenter.y + terrainChunk.getBoundingBox().getYExtent());
      final int ez = (int) (chunkCenter.z + terrainChunk.getBoundingBox().getZExtent());
      // LOG.debug(String.format("Mesh Chunk Node: Chunk Center: %s, x from %d to %d",
      // chunkCenter, sx, ex));
      final List<Geometry> result = Lists.newArrayList();
      for (int x = sx; x < ex; x++) {
         for (int y = sy; y < ey; y++) {
            for (int z = sz; z < ez; z++) {
               final Vector3f blockPosition = new Vector3f(x, y, z);
               final int blockType = terrainChunk.getBlock(blockPosition);
               if (blockType != 0) {
                  // Box b = new Box(blockPosition, 0.25f, 0.25f, 0.25f);
                  // Geometry g = new Geometry("Box", b);
                  // g.setMaterial(atlas.getBlueMaterial());
                  // result.add(g);
                  final EnumSet<Face> faces = checkFaces(blockPosition, 1.0f);
                  if (!faces.isEmpty()) {
                     createFaces(blockPosition, 0.5f, faces, blockType);
                     usedBlockTypes.add(blockType);
                  }
               }
            }
         }
      }

      final TIntIterator it = usedBlockTypes.iterator();

      while (it.hasNext()) {
         final int blockType = it.next();
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
      // TODO: Cut down on memory usage until we need to actually hold onto this
      // data.
      terrainChunk.clear();

      final List<Geometry> toReturn = GeometryBatchFactory.makeBatches(result);
      // LOG.debug(String.format("Generated %d faces", c));
      return toReturn;
   }

   private EnumSet<Face> checkFaces(final Vector3f blockPosition, final float dimension) {
      final float x = blockPosition.x;
      final float y = blockPosition.y;
      final float z = blockPosition.z;
      final int currentBlock = terrainChunk.getBlock(blockPosition);
      final EnumSet<Face> faces = EnumSet.noneOf(Face.class);
      if (needFace(currentBlock, x + dimension, y, z, Face.FACE_RIGHT)) {
         faces.add(Face.FACE_RIGHT);
      }
      if (needFace(currentBlock, x - dimension, y, z, Face.FACE_LEFT)) {
         faces.add(Face.FACE_LEFT);
      }
      if (needFace(currentBlock, x, y + dimension, z, Face.FACE_UP)) {
         faces.add(Face.FACE_UP);
      }
      if (needFace(currentBlock, x, y - dimension, z, Face.FACE_DOWN)) {
         faces.add(Face.FACE_DOWN);
      }
      if (needFace(currentBlock, x, y, z + dimension, Face.FACE_BACK)) {
         faces.add(Face.FACE_BACK);
      }
      if (needFace(currentBlock, x, y, z - dimension, Face.FACE_FRONT)) {
         faces.add(Face.FACE_FRONT);
      }
      return faces;
   }

   private boolean needFace(final int currentBlock, final float x, final float y, final float z, final Face faceToCheck) {
      if (terrainChunk.hasBlock(new Vector3f(x, y, z))) {
         return needFace(currentBlock, terrainChunk.getBlock(new Vector3f(x, y, z)));
      } else {
         return needFace(currentBlock, loader.getAdHocBlock(new Vector3f(x, y, z)));
      }
   }

   private boolean needFace(final int currentType, final int neighborType) {
      return neighborType == 0;
      // TODO: Make some lakes so we can get transparent water going.
      // if (atlas.isTransparent(currentType)) {
      // // current is translucent
      // // we need a face if the neighbor block is not translucent
      // // we need a face if the neighbor block is also translucent but with a
      // different type
      // return !atlas.isTransparent(neighborType) || currentType !=
      // neighborType;
      // }
      // // normal current
      // // we need a face if the neighbor is translucent
      // return atlas.isTransparent(neighborType);
   }

   private boolean createFaces(final Vector3f position, final float dimension, final EnumSet<Face> faces, final int bloxelType) {
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
      final float x = position.x;
      final float y = position.y;
      final float z = position.z;
      final Vector3f pa = new Vector3f(x - dimension, y - dimension, z + dimension);
      final Vector3f pb = new Vector3f(x + dimension, y - dimension, z + dimension);
      final Vector3f pc = new Vector3f(x - dimension, y + dimension, z + dimension);
      final Vector3f pd = new Vector3f(x + dimension, y + dimension, z + dimension);
      final Vector3f pe = new Vector3f(x - dimension, y - dimension, z - dimension);
      final Vector3f pf = new Vector3f(x + dimension, y - dimension, z - dimension);
      final Vector3f pg = new Vector3f(x - dimension, y + dimension, z - dimension);
      final Vector3f ph = new Vector3f(x + dimension, y + dimension, z - dimension);

      final List<Vector2f> lightMapCoord = lightTextureCoord(9);
      // TODO calculate light value depending on the position of the block and
      // the neighbors

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

   private ArrayList<Integer> verticesIndex(final int verticesSize, final ArrayList<Integer> indexes) {
      final ArrayList<Integer> result = Lists.newArrayList();
      for (final Integer i : indexes) {
         result.add(i + verticesSize);
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
}
