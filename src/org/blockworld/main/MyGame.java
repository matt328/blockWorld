/**
 * 
 */
package org.blockworld.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.blockworld.level.ChunkSet;
import org.blockworld.level.ChunkSetControl;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.AppSettings;

/**
 * @author Matt
 * 
 */
public class MyGame extends SimpleApplication implements AnalogListener {
	private static Logger log = Logger.getLogger(MyGame.class.getName());
	private static final int CHUNK_SIZE = 32;

	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setTexture("m_ColorMap", assetManager.loadTexture("Textures/grass.png"));
		ChunkSet set = new ChunkSet(CHUNK_SIZE, mat2);
		flyCam.setMoveSpeed(3.0f);
		getCamera().setLocation(new Vector3f(10.0f, 50.0f, 10.0f));
		ChunkSetControl ccs = new ChunkSetControl(getCamera());
		set.addControl(ccs);
		rootNode.attachChild(set);
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
