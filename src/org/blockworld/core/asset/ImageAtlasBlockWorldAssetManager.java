package org.blockworld.core.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Texture;

/**
 * @author Matt Teeter
 */
public class ImageAtlasBlockWorldAssetManager implements BlockWorldAssetManager {

	private static final Logger LOG = LoggerFactory.getLogger(ImageAtlasBlockWorldAssetManager.class);

	private static class UVMapKey extends ArrayList<Object> {
		private static final long serialVersionUID = 1L;

		public UVMapKey(final Integer bloxelType, final int face) {
			add(bloxelType);
			add(face);
		}
	}

	private static final int FACE_RIGHT = 1;
	private static final int FACE_LEFT = 2;
	private static final int FACE_UP = 4;
	private static final int FACE_DOWN = 8;
	private static final int FACE_FRONT = 16;
	private static final int FACE_BACK = 32;

	private static int faceOf(final String face) {
		if (face.equalsIgnoreCase("up")) {
			return FACE_UP;
		}
		if (face.equalsIgnoreCase("down")) {
			return FACE_DOWN;
		}
		if (face.equalsIgnoreCase("front")) {
			return FACE_FRONT;
		}
		if (face.equalsIgnoreCase("back")) {
			return FACE_BACK;
		}
		if (face.equalsIgnoreCase("left")) {
			return FACE_LEFT;
		}
		if (face.equalsIgnoreCase("right")) {
			return FACE_RIGHT;
		}
		throw new IllegalArgumentException("Unkown face " + face);
	}

	private final Set<Integer> types = Sets.newHashSet();
	private final Map<UVMapKey, Vector2f> uvmap = Maps.newHashMap();
	private final Material bloxelMaterial;
	private final Material bloxelMaterialTransparent;
	private boolean wireframe;
	private final RenderManager renderManager;
	
	private static final Set<Integer> TRANSPARENT_BOXELS = Sets.newHashSet(6);

	/**
	 * @param theAssetManager
	 *            for loading textures etc.
	 */
	public ImageAtlasBlockWorldAssetManager(final AssetManager theAssetManager, final RenderManager renderManager) {
		this.renderManager = renderManager;
		final Properties p = new Properties();
		try {
			URL url = ClassLoader.getSystemResource("uvmap.properties");
			if (url == null) {
				throw new RuntimeException("Cannot load uvmap.properties from classpath, aborting");
			}
			InputStream is = url.openStream();
			try {
				p.load(url.openStream());
			} finally {
				is.close();
			}
		} catch (final IOException e) {
			LOG.error("Error closing uvmap.properties", e);
		}
		final Set<Entry<Object, Object>> definitions = p.entrySet();
		for (final Entry<Object, Object> d : definitions) {
			final Integer bloxelType = Integer.valueOf(d.getKey().toString());
			final String value = String.format("%s", d.getValue());
			final Iterable<String> bloxelProperties = Splitter.on(";").split(value);
			for (final String s : bloxelProperties) {
				final String face = s.split(":")[0];
				final String coord = s.split(":")[1];
				final float x = Float.valueOf(coord.split(",")[0]);
				final float y = Float.valueOf(coord.split(",")[1]);
				uvmap.put(new UVMapKey(bloxelType, faceOf(face)), new Vector2f(x, y));
				types.add(bloxelType);
			}
		}

		final Texture texture = theAssetManager.loadTexture("Textures/Blocks/minecraft.png");
		// final Texture normalMap = theAssetManager.loadTexture("Textures/Blocks/minecraft_normal.png");
		final Texture lightMap = theAssetManager.loadTexture("Textures/Blocks/lightmap.png");

		bloxelMaterial = new Material(theAssetManager, "Common/MatDefs/Light/Lighting.j3md");
		bloxelMaterial.setTexture("DiffuseMap", texture);
		// bloxelMaterial.setTexture("NormalMap", normalMap);
		bloxelMaterial.setTexture("LightMap", lightMap);
		bloxelMaterial.setBoolean("SeparateTexCoord", true);
		bloxelMaterial.setBoolean("VertexLighting", true); // need to avoid
															// shader error!
															// "missing vNormal"
															// ?!
		bloxelMaterial.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
		bloxelMaterial.selectTechnique("Default", renderManager);

		bloxelMaterialTransparent = new Material(theAssetManager, "Common/MatDefs/Light/Lighting.j3md");
		bloxelMaterialTransparent.setTexture("DiffuseMap", texture);
		// bloxelMaterialTransparent.setTexture("NormalMap", normalMap);
		bloxelMaterialTransparent.setTexture("LightMap", lightMap);
		bloxelMaterialTransparent.setBoolean("SeparateTexCoord", true);
		bloxelMaterialTransparent.setBoolean("VertexLighting", true);
		bloxelMaterialTransparent.setTransparent(true);
		bloxelMaterialTransparent.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		bloxelMaterialTransparent.selectTechnique("Default", renderManager);
		// Use this if you have several transparent objects obscuring
		// one another. Disables writing of the pixel's depth value to
		// the depth buffer.
		// bloxelMaterialTransparent.getAdditionalRenderState().setDepthWrite(false);
	}

	@Override
	public Set<Integer> getBloxelTypes() {
		return types;
	}

	@Override
	public Material getMaterial(final Integer bloxelType) {
		Material m;
		if (isTransparent(bloxelType)) {
			m = bloxelMaterialTransparent;
		} else {
			m = bloxelMaterial;
		}
		return m;
	}

	@Override
	public List<Vector2f> getTextureCoordinates(final Integer bloxelType, final int face) {
		return uvMapTexture(uvmap.get(new UVMapKey(bloxelType, face)));
	}

	@Override
	public boolean isTransparent(final Integer bloxelType) {
		return TRANSPARENT_BOXELS.contains(bloxelType);
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(final boolean wireframe) {
		this.wireframe = wireframe;
		bloxelMaterial.getAdditionalRenderState().setWireframe(wireframe);
	}

	private List<Vector2f> uvMapTexture(final Vector2f coord) {
		if (coord == null) {
			// use complete image as texture
			final Vector2f bottomLeft = new Vector2f(0, 0);
			final Vector2f bottomRight = new Vector2f(1, 0);
			final Vector2f topLeft = new Vector2f(0, 1);
			final Vector2f topRight = new Vector2f(1, 1);
			return Lists.newArrayList(bottomLeft, bottomRight, topLeft, topRight);
		}
		// each image is 32x32, the whole image-atlas is 512x512
		// coord.x: 0..15
		// coord.y: 0..15
		final float s = 32f / 512f;
		final float x = coord.x * s;
		final float y = coord.y * s;
		final Vector2f bottomLeft = new Vector2f(x, y);
		final Vector2f bottomRight = new Vector2f(x + s, y);
		final Vector2f topLeft = new Vector2f(x, y + s);
		final Vector2f topRight = new Vector2f(x + s, y + s);
		return Lists.newArrayList(bottomLeft, bottomRight, topLeft, topRight);
	}
}