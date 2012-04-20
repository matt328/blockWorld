/**
 * 
 */
package org.blockworld.main;

import org.blockworld.level.ChunkLoader;
import org.blockworld.level.ChunkSet;
import org.blockworld.level.ChunkSetControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * @author Matt
 * 
 */
public class BlockWorldApplication extends SimpleApplication implements AnalogListener, BlockWorldApplicationInterface {

	private static final int CHUNK_SIZE = 9;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockWorldApplication.class);
	private final ChunkLoader chunkLoader;

	public BlockWorldApplication(BlockWorldApplicationSettingsProvider provider, ChunkLoader chunkLoader) {
		setSettings(provider.getAppSettings());
		this.chunkLoader = chunkLoader;
	}

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

	@Override
	public void onAnalog(String name, float value, float tpf) {

	}

}
