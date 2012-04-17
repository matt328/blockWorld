/**
 * 
 */
package org.blockworld.main;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.blockworld.level.ChunkSet;
import org.blockworld.level.ChunkSetControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * @author Matt
 * 
 */
public class MyGame extends SimpleApplication implements AnalogListener {
	
	private static final int CHUNK_SIZE = 32;
	private static final Logger LOG = LoggerFactory.getLogger(MyGame.class);
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
	    final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
	    final Handler[] handlers = rootLogger.getHandlers();
	    for (int i = 0; i < handlers.length; i++) {
	      rootLogger.removeHandler(handlers[i]);
	    }
	    SLF4JBridgeHandler.install();
	    
	    LOG.info("SLF4J Call from MyGame.main()");
	    
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
		
	}

}
