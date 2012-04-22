/**
 * 
 */
package org.blockworld.main;

import org.blockworld.world.node.ChunkSetNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;

/**
 * @author Matt
 * 
 */
public class BlockWorldApplication extends SimpleApplication implements AnalogListener, BlockWorldApplicationInterface {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockWorldApplication.class);

	public BlockWorldApplication(SettingsProvider provider) {
		setSettings(provider.getAppSettings());
	}

	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);
		flyCam.setMoveSpeed(3.0f);
		getCamera().setLocation(new Vector3f(10.0f, 50.0f, 10.0f));

		ChunkSetNode chunkSet = new ChunkSetNode(assetManager);
		
		rootNode.attachChild(chunkSet);
	}

	@Override
	public void simpleUpdate(final float tpf) {
		// geom.rotate(0.0f, 0.2f * tpf, 0.0f);
		super.simpleUpdate(tpf);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {

	}

}
