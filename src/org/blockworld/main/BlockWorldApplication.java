/**
 * 
 */
package org.blockworld.main;

import org.blockworld.core.asset.ImageAtlasBlockWorldAssetManager;
import org.blockworld.world.Block;
import org.blockworld.world.control.WorldNodeUpdateControl;
import org.blockworld.world.loader.BlockLoader;
import org.blockworld.world.loader.TerasologyTerrainLoader;
import org.blockworld.world.node.WorldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * @author Matt
 * 
 */
public class BlockWorldApplication extends SimpleApplication implements AnalogListener {

	private final static float HORIZONT = 256;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockWorldApplication.class);

	private WorldNode worldNode;
	private ImageAtlasBlockWorldAssetManager blockFactory;
	private DirectionalLight sunDirectionalLight;
	
	public BlockWorldApplication(BlockWorldApplicationSettingsProvider provider) {
		setSettings(provider.getAppSettings());
	}

	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);
		flyCam.setMoveSpeed(3.0f);
		getCamera().setLocation(new Vector3f(10.0f, 50.0f, 10.0f));

		blockFactory = new ImageAtlasBlockWorldAssetManager(assetManager, renderManager);
		BlockLoader<Block> loader = new TerasologyTerrainLoader("mathilda-marie");
		worldNode = new WorldNode(blockFactory, loader);
		// worldNode = new WorldNode(blockFactory, new TerasologyTerrainLoader("mathilda-marie"), null, flyCam, 8.0f, HORIZONT);
		rootNode.attachChild(worldNode);
		worldNode.addControl(new WorldNodeUpdateControl(worldNode, this.getCamera()));
		initLight();
	}

	private void initLight() {
		sunDirectionalLight = new DirectionalLight();
		sunDirectionalLight.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		sunDirectionalLight.setColor(ColorRGBA.White);
		rootNode.addLight(sunDirectionalLight);
		final AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.Yellow.mult(2));
		rootNode.addLight(ambientLight);
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
