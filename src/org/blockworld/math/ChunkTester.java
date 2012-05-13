/**
 * ChunkTester
 * Author: Matt Teeter
 * May 6, 2012
 */
package org.blockworld.math;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.blockworld.asset.BlockTextureAtlas;
import org.blockworld.asset.TextureAtlas;
import org.blockworld.main.Main;
import org.blockworld.world.loader.BlockworldBlockLoader;
import org.blockworld.world.node.WorldController;
import org.blockworld.world.node.WorldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkTester extends SimpleApplication {

	private static final int CHUNK_RADIUS = 10;
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	static {
		final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		final Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChunkTester chunkTester = new ChunkTester();
		AppSettings appSettings = new AppSettings(true);
		appSettings.setVSync(true);
		chunkTester.setSettings(appSettings);
		chunkTester.setShowSettings(false);
		chunkTester.start();
	}

	@Override
	public void simpleInitApp() {
		DirectionalLight sunDirectionalLight = new DirectionalLight();
		sunDirectionalLight.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		sunDirectionalLight.setColor(ColorRGBA.White);
		rootNode.addLight(sunDirectionalLight);
		final AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.Yellow.mult(2));
		rootNode.addLight(ambientLight);
		

		// for(int x = -CHUNK_RADIUS; x < CHUNK_RADIUS; x++) {
		// for(int z = -CHUNK_RADIUS; z < CHUNK_RADIUS; z++) {
		// BasicChunk chunk = new BasicChunk(new Vector3f(16, 256, 16), new
		// Vector3f(x, 0.0f, z));
		// loader.fill(chunk);
		// MeshChunkNode node = new MeshChunkNode(chunk, atlas, loader);
		// node.calculate();
		// rootNode.attachChild(node);
		// }
		// }
		
		StatsAppState s = stateManager.getState(StatsAppState.class);
		WorldNode w = new WorldNode(CHUNK_RADIUS, new Vector3f(16, 256, 16), assetManager);
		WorldController controller = new WorldController(w, getCamera());
		w.addControl(controller);
		rootNode.attachChild(w);
		
		getCamera().setLocation(new Vector3f(4.8676667f, 128.0f, -8.6687975f));
		getCamera().setRotation(new Quaternion(0.27165264f, -0.2043419f, 0.05914175f, 0.93859017f));
		flyCam.setMoveSpeed(5.0f);

	}

}
