/**
 * BlockTextureAtlas
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.asset;

import gnu.trove.set.hash.TIntHashSet;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.blockworld.world.node.Face;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;

/**
 * @author Matt Teeter
 * 
 */
public class BlockTextureAtlas implements TextureAtlas {

	private static class UVMapKey extends ArrayList<Object> {
		private static final long serialVersionUID = 1L;

		public UVMapKey(final Integer type, final Face face) {
			add(type);
			add(face);
		}
	}

	private final TIntHashSet types;
	private final Map<UVMapKey, Vector2f> uvMap;
	private final Material blockMaterial;
	private final Material blockMaterialTransparent;
	private static final TIntHashSet TRANSPARENT_TYPES = new TIntHashSet(new int[] { 6 });

	public BlockTextureAtlas(final AssetManager theAssetManager) throws Exception {
		uvMap = Maps.newHashMap();
		types = new TIntHashSet();

		final Properties p = new Properties();

		URL url = ClassLoader.getSystemClassLoader().getResource("blockAtlas.properties");
		if (url == null) {
			throw new RuntimeException("Cannot load resource: blockAtlas.properties");
		}

		InputStream is = url.openStream();
		try {
			p.load(is);
		} finally {
			is.close();
		}

		final Set<Entry<Object, Object>> definitions = p.entrySet();
		for (final Entry<Object, Object> d : definitions) {
			final Integer bloxelType = Integer.valueOf(d.getKey().toString());
			final String value = String.format("%s", d.getValue());
			final Iterable<String> blockProperties = Splitter.on(";").split(value);
			for (final String s : blockProperties) {
				final String face = s.split(":")[0];
				final String coord = s.split(":")[1];
				final float x = Float.valueOf(coord.split(",")[0]);
				final float y = Float.valueOf(coord.split(",")[1]);
				uvMap.put(new UVMapKey(bloxelType, Face.lookup(face)), new Vector2f(x, y));
				types.add(bloxelType);
			}
		}

		final Texture texture = theAssetManager.loadTexture("Textures/Bloxels/minecraft.png");
		final Texture lightMap = theAssetManager.loadTexture("Textures/Bloxels/lightmap.png");

		blockMaterial = new Material(theAssetManager, "Common/MatDefs/Light/Lighting.j3md");
		blockMaterial.setTexture("DiffuseMap", texture);
		blockMaterial.setTexture("LightMap", lightMap);
		blockMaterial.setBoolean("SeparateTexCoord", true);
		blockMaterial.setBoolean("VertexLighting", true); // need to avoid shader error! "missing vNormal" ?!
		blockMaterial.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);

		blockMaterialTransparent = new Material(theAssetManager, "Common/MatDefs/Light/Lighting.j3md");
		blockMaterialTransparent.setTexture("DiffuseMap", texture);
		blockMaterialTransparent.setTexture("LightMap", lightMap);
		blockMaterialTransparent.setBoolean("SeparateTexCoord", true);
		blockMaterialTransparent.setBoolean("VertexLighting", true);
		blockMaterialTransparent.setTransparent(true);
		blockMaterialTransparent.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

	}

	@Override
	public TIntHashSet getTypes() {
		return types;
	}

	@Override
	public Material getMaterial(int type) {
		if (isTransparent(type)) {
			return blockMaterialTransparent;
		} else {
			return blockMaterial;
		}
	}

	@Override
	public List<Vector2f> getTextureCoordinates(int type, Face face) {
		return uvMapTexture(uvMap.get(new UVMapKey(type, face)));
	}

	@Override
	public boolean isTransparent(int type) {
		return TRANSPARENT_TYPES.contains(type);
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
