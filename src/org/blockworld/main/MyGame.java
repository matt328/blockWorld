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

		final float levelSize = 4;
		final LevelGenerator generator = new LevelGenerator(16, new Vector3f(levelSize, levelSize, levelSize));
		Collection<LevelChunk> chunks = new ArrayList<LevelChunk>();
		for (int i = 0; i < 1; i++) {
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
		AppSettings settings = new AppSettings(true);
		settings.setUseJoysticks(true);
		app.setShowSettings(false);
		app.setSettings(settings);
		app.start();
	}

	@Override
	public void simpleUpdate(final float tpf) {
		// geom.rotate(0.0f, 0.2f * tpf, 0.0f);
		super.simpleUpdate(tpf);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		log.log(Level.INFO, "name: " + name + ": " + value);
	}

}
