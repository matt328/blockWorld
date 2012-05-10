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
import org.blockworld.util.Stopwatch;
import org.blockworld.world.BasicChunk;
import org.blockworld.world.loader.BlockworldBlockLoader;
import org.blockworld.world.node.MeshChunkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.jme3.app.SimpleApplication;
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
		chunkTester.setSettings(new AppSettings(true));
		chunkTester.setShowSettings(false);
		chunkTester.start();
	}

	@Override
	public void simpleInitApp() {
		BasicChunk chunk = new BasicChunk(new Vector3f(16, 256, 16), Vector3f.ZERO);
		BasicChunk chunk2 = new BasicChunk(new Vector3f(16, 256, 16), new Vector3f(1.0f, 0.0f, 0.0f));
		BlockworldBlockLoader loader = new BlockworldBlockLoader();

		loader.fill(chunk);
		loader.fill(chunk2);
		
		TextureAtlas atlas = new BlockTextureAtlas(this.assetManager);

		MeshChunkNode node = new MeshChunkNode(chunk, atlas);
		MeshChunkNode node2 = new MeshChunkNode(chunk2, atlas);
		node.calculate();
		node2.calculate();
		
		DirectionalLight sunDirectionalLight = new DirectionalLight();
		sunDirectionalLight.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		sunDirectionalLight.setColor(ColorRGBA.White);
		rootNode.addLight(sunDirectionalLight);
		final AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.Yellow.mult(2));
		rootNode.addLight(ambientLight);

		rootNode.attachChild(node);
		rootNode.attachChild(node2);
		getCamera().setLocation(new Vector3f(4.8676667f, 499.2639f, -8.6687975f));
		getCamera().setRotation(new Quaternion(0.27165264f, -0.2043419f, 0.05914175f, 0.93859017f));

	}

}
