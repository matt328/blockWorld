/**
 * 
 */
package com.snowFallPeak.blockWorld.main;

import java.util.ArrayList;
import java.util.Collection;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.snowFallPeak.blockWorld.level.LevelChunk;
import com.snowFallPeak.blockWorld.level.LevelGenerator;
import com.snowFallPeak.blockWorld.level.neighbor.Neighbor;

/**
 * @author Matt
 * 
 */
public class MyGame extends SimpleApplication {
	private Collection<Geometry> geometries;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.SimpleApplication#simpleInitApp()
	 */
	@Override
	public void simpleInitApp() {
		geometries = new ArrayList<Geometry>();
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);

		final Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat2.setTexture("m_ColorMap", assetManager.loadTexture("Textures/grass.png"));
		
		final float levelSize = 32;
		final LevelGenerator generator = new LevelGenerator(16, new Vector3f(levelSize, levelSize, levelSize));
		final LevelChunk chunk = generator.generateChunk(new Vector2f(0, 0));
		Collection<LevelChunk> chunks = new ArrayList<LevelChunk>();
		for(int i = 0; i < 9; i++) {
			LevelChunk c = generator.generateChunk(new Vector2f(i+1, 0));
			chunks.add(c);
		}
		for (int x = 0; x < levelSize; x++) {
			for (int y = 0; y < levelSize; y++) {
				for (int z = 0; z < levelSize; z++) {
					if (chunk.get(x, y, z) == 0) {
						Collection<Neighbor> neighbors = chunk.getNeighbors(x, y, z);
						if (!neighbors.isEmpty()) {
							for(Neighbor neighbor : neighbors) {
								geometries.add(neighbor.getGeometry(x, y, z, (int)levelSize));
							}
						}
					}
				}
			}
		}
		Mesh mesh = new Mesh();
		GeometryBatchFactory.mergeGeometries(geometries, mesh);
		mesh.updateBound();
		Geometry g = new Geometry("World", mesh);
		g.setMaterial(mat2);
		rootNode.attachChild(g);			
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final MyGame app = new MyGame();
		app.start();
	}

	@Override
	public void simpleUpdate(final float tpf) {
//		geom.rotate(0.0f, 0.2f * tpf, 0.0f);
		super.simpleUpdate(tpf);
	}

}
