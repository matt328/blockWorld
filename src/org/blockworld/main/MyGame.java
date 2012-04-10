/**
 * 
 */
package org.blockworld.main;

import java.util.ArrayList;
import java.util.Collection;

import org.blockworld.level.LevelChunk;
import org.blockworld.level.LevelGenerator;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * @author Matt
 * 
 */
public class MyGame extends SimpleApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.SimpleApplication#simpleInitApp()
	 */
	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);

		final float levelSize = 4;
		final LevelGenerator generator = new LevelGenerator(16, new Vector3f(levelSize, levelSize, levelSize));
		Collection<LevelChunk> chunks = new ArrayList<LevelChunk>();
		for (int i = 0; i < 3; i++) {
			LevelChunk c = generator.generateChunk(new Vector2f(i, 0));
			chunks.add(c);
		}
		for (LevelChunk chunk : chunks) {
			Geometry g = chunk.createGeometry();
			final Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
			mat2.setTexture("m_ColorMap", assetManager.loadTexture("Textures/grass.png"));
			g.setMaterial(mat2);
			rootNode.attachChild(g);
		}
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
		// geom.rotate(0.0f, 0.2f * tpf, 0.0f);
		super.simpleUpdate(tpf);
	}

}
