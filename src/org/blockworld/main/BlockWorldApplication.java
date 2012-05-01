/**
 * 
 */
package org.blockworld.main;

import org.blockworld.scripting.ScriptEnvironment;
import org.blockworld.world.node.ChunkSetController;
import org.blockworld.world.node.ChunkSetNode;
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

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockWorldApplication.class);

	private ScriptEnvironment scripting;

	public BlockWorldApplication(SettingsProvider provider) {
		setSettings(provider.getAppSettings());
		// scripting = new ScriptEnvironment();
		// try {
		// scripting.registerPackage("org.blockworld.main");
		// scripting.setAccessibility(true);
		// } catch (EvalError e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void simpleInitApp() {
		AppState startupAppState = new StartupAppState();
		stateManager.attach(startupAppState);
		flyCam.setMoveSpeed(6.0f);
		getCamera().setLocation(new Vector3f(0.0f, 10.0f, 0.0f));
		// getCamera().setRotation(new Quaternion(0.07764202f, 0.91964334f, -0.3065641f, 0.23290835f));

		DirectionalLight sunDirectionalLight = new DirectionalLight();
		sunDirectionalLight.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		sunDirectionalLight.setColor(ColorRGBA.White);
		rootNode.addLight(sunDirectionalLight);
		final AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.Yellow.mult(2));
		rootNode.addLight(ambientLight);

		try {
			ChunkSetNode chunkSet = new ChunkSetNode(assetManager);
			ChunkSetController controller = new ChunkSetController(chunkSet, getCamera());
			chunkSet.addControl(controller);
//			scripting.registerObject("chunkSet", chunkSet);
//			scripting.registerPackageForClass(Vector3f.class);
			rootNode.attachChild(chunkSet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
