/**
 * 
 */
package org.blockworld.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.blockworld.level.LevelChunk;
import org.blockworld.level.LevelGenerator;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.Joystick;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;

/**
 * @author Matt
 * 
 */
public class MyGame extends SimpleApplication implements AnalogListener {
	private static Logger log = Logger.getLogger(MyGame.class.getName());
	private static final int CHUNK_SIZE = 32;
	private static final int NUM_CHUNKS = 9;
	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);

		Joystick[] joysticks = inputManager.getJoysticks();
		log.log(Level.INFO, "{0} joysticks found", joysticks.length);

		Joystick p1 = joysticks[0];
		inputManager.addMapping("Joy Left", new JoyAxisTrigger(0, 2, true));
		inputManager.addMapping("Joy Right", new JoyAxisTrigger(0, 2, false));
		inputManager.addMapping("Joy Down", new JoyAxisTrigger(0, 3, true));
		inputManager.addMapping("Joy Up", new JoyAxisTrigger(0, 3, false));
		inputManager.addListener(this, "Joy Left", "Joy Right", "Joy Down", "Joy Up");
		inputManager.setAxisDeadZone(0.2f);

		final LevelGenerator generator = new LevelGenerator(16, new Vector3f(CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE));
		Collection<LevelChunk> chunks = new ArrayList<LevelChunk>();
		
		long start = System.currentTimeMillis();
		for (int x = 0; x < NUM_CHUNKS; x++) {
			for(int z = 0; z < NUM_CHUNKS; z++) {
				LevelChunk c = generator.generateChunk(new Vector2f(x, z));
				chunks.add(c);
			}
		}
		long end = System.currentTimeMillis();
		log.log(Level.INFO, "Generating Chunks took {0}ms", (end - start));
		
		for (LevelChunk chunk : chunks) {
			Geometry g = chunk.createGeometry();
			if (g != null) {
				final Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				mat2.setTexture("m_ColorMap", assetManager.loadTexture("Textures/grass.png"));
				g.setMaterial(mat2);
				rootNode.attachChild(g);
			}
		}
	}

	@Override
	public void simpleUpdate(final float tpf) {
		// geom.rotate(0.0f, 0.2f * tpf, 0.0f);
		super.simpleUpdate(tpf);
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final MyGame app = new MyGame();
		AppSettings settings = new AppSettings(true);
		settings.setUseJoysticks(true);
		settings.setVSync(true);
		settings.setWidth(800);
		settings.setHeight(600);
		settings.setBitsPerPixel(24);
		app.setShowSettings(false);
		app.setSettings(settings);
		app.start();
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		log.log(Level.INFO, "name: " + name + ": " + value);
	}

}
