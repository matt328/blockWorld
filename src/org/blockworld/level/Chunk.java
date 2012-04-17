package org.blockworld.level;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class Chunk extends Geometry {
	private byte[][][] byteList;
	private final int numCells;
	private final Vector2f globalGridPosition;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Chunk.class.getName());

	public Chunk(final int size, final Vector2f newChunkPosition) {
		super("Chunk-" + newChunkPosition.toString());
		numCells = size;
		globalGridPosition = newChunkPosition;
		byteList = new byte[size][size][size];
	}

	public void set(final byte val, final int x, final int y, final int z) {
		byteList[x][y][z] = val;
	}

	public void generateMesh() {
		mesh = new Mesh();
		float halfCubeSize = 0.5f;
		TIntList indices = new TIntArrayList();
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector3f> texcoords = new ArrayList<Vector3f>();
		for (int x = 0; x < numCells; x++) {
			for (int y = 0; y < numCells; y++) {
				for (int z = 0; z < numCells; z++) {
					if (byteList[x][y][z] == 0) {
						continue;
					}
					float xOffset = globalGridPosition.x * numCells;
					float zOffset = globalGridPosition.y * numCells;
					Vector3f v1 = new Vector3f(x + xOffset, y, z + halfCubeSize + zOffset);
					Vector3f v2 = new Vector3f(x + xOffset + halfCubeSize, y, z + halfCubeSize + zOffset);
					Vector3f v3 = new Vector3f(x + xOffset, y + halfCubeSize, z + halfCubeSize + zOffset);
					Vector3f v4 = new Vector3f(x + xOffset + halfCubeSize, y + halfCubeSize, z + halfCubeSize + zOffset);
					Vector3f v5 = new Vector3f(x + xOffset, y, z + zOffset);
					Vector3f v6 = new Vector3f(x + xOffset + halfCubeSize, y, z + zOffset);
					Vector3f v7 = new Vector3f(x + xOffset, y + halfCubeSize, z + zOffset);
					Vector3f v8 = new Vector3f(x + xOffset + halfCubeSize, y + halfCubeSize, z + zOffset);
					EnumSet<Faces> facesSet = getNeededFaces(x, y, z);
					if (facesSet.contains(Faces.FRONT)) {
						addVertexIndex(v5, vertices, indices);
						addVertexIndex(v6, vertices, indices);
						addVertexIndex(v7, vertices, indices);
						addVertexIndex(v8, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
					if (facesSet.contains(Faces.BACK)) {
						addVertexIndex(v1, vertices, indices);
						addVertexIndex(v2, vertices, indices);
						addVertexIndex(v3, vertices, indices);
						addVertexIndex(v4, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
					if (facesSet.contains(Faces.RIGHT)) {
						addVertexIndex(v2, vertices, indices);
						addVertexIndex(v4, vertices, indices);
						addVertexIndex(v6, vertices, indices);
						addVertexIndex(v8, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
					if (facesSet.contains(Faces.LEFT)) {
						addVertexIndex(v1, vertices, indices);
						addVertexIndex(v3, vertices, indices);
						addVertexIndex(v5, vertices, indices);
						addVertexIndex(v7, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
					if (facesSet.contains(Faces.TOP)) {
						addVertexIndex(v3, vertices, indices);
						addVertexIndex(v4, vertices, indices);
						addVertexIndex(v7, vertices, indices);
						addVertexIndex(v8, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
					if (facesSet.contains(Faces.BOTTOM)) {
						addVertexIndex(v1, vertices, indices);
						addVertexIndex(v2, vertices, indices);
						addVertexIndex(v5, vertices, indices);
						addVertexIndex(v6, vertices, indices);
						texcoords.add(new Vector3f(0, 0, 0));
						texcoords.add(new Vector3f(1, 0, 0));
						texcoords.add(new Vector3f(1, 1, 0));
						texcoords.add(new Vector3f(0, 1, 0));
					}
				}
			}
		}
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		mesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices.toArray()));
		mesh.setBuffer(Type.TexCoord, 3, BufferUtils.createFloatBuffer(texcoords.toArray(new Vector3f[texcoords.size()])));
		mesh.updateCounts();
		mesh.updateBound();
	}

	private void addVertexIndex(Vector3f vertex, List<Vector3f> vertices, TIntList indices) {
		int index = vertices.indexOf(vertex);
		if (index != -1) {
			indices.add(index);
		} else {
			vertices.add(vertex);
			indices.add(vertices.size() - 1);
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private EnumSet<Faces> getNeededFaces(int x, int y, int z) {
		EnumSet<Faces> faces = EnumSet.noneOf(Faces.class);
		if (x == 0 || isSeeThrough(x - 1, y, z)) {
			faces.add(Faces.LEFT);
		}
		if (x == numCells - 1 || isSeeThrough(x + 1, y, z)) {
			faces.add(Faces.RIGHT);
		}
		if (y == numCells - 1 || isSeeThrough(x, y + 1, z)) {
			faces.add(Faces.TOP);
		}
		if (y == 0 || isSeeThrough(x, y - 1, z)) {
			faces.add(Faces.BOTTOM);
		}
		if (z == numCells - 1 || isSeeThrough(x, y, z + 1)) {
			faces.add(Faces.FRONT);
		}
		if (z == 0 || isSeeThrough(x, y, z - 1)) {
			faces.add(Faces.BACK);
		}
		return faces;
	}

	private boolean isSeeThrough(int x, int y, int z) {
		return byteList[x][y][z] == 0;
	}

	public int getSizeInBytes() {
		return byteList.length;
	}

}
