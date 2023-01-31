/**
 * ChunkTester Author: Matt Teeter May 6, 2012
 */
package org.blockworld.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.apache.commons.lang3.Range;
import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.functions.ColorMaker;
import org.blockworld.math.functions.Function;
import org.blockworld.math.functions.PerlinNoise;
import org.blockworld.math.functions.ScalePoint;
import org.blockworld.math.functions.World;
import org.blockworld.screen.HudScreenController;
import org.blockworld.world.node.WorldController;
import org.blockworld.world.node.WorldNode;
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
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

/**
 * @author Matt Teeter
 */
public class BlockworldApplication extends SimpleApplication {

	/**
	 * Height of the display, in pixels.
	 */
	private static final int DISPLAY_HEIGHT = 768;

	/**
	 * Width of the display, in pixels.
	 */
	private static final int DISPLAY_WIDTH = 1024;

	private static final int CHUNK_RADIUS = 10;

	private ColorMaker colorMaker;

	private final Color skyBlue = new Color(76, 159, 255, 192);
	private final Color grassGreen = new Color(75, 132, 30, 192);
	private Function groundBase;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BlockworldApplication.class);

	private BufferedImage bufferedImage;
	static {
		final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		final Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
	}

	public static void main(final String[] args) {
		final BlockworldApplication chunkTester = new BlockworldApplication();
		final AppSettings appSettings = new AppSettings(true);
		appSettings.setVSync(true);
		appSettings.setWidth(DISPLAY_WIDTH);
		appSettings.setHeight(DISPLAY_HEIGHT);
		chunkTester.setSettings(appSettings);
		chunkTester.setShowSettings(false);
		chunkTester.start();
	}

	@Override
	public final void simpleInitApp() {

		colorMaker = new ColorMaker();
		colorMaker.addColor(Range.between(-1.0f, 0.9f), skyBlue);
		colorMaker.addColor(Range.between(0.0f, 1.0f), grassGreen);

		final Function zoneNoise = new PerlinNoise(NoiseType.PERLIN, 0.15f, 1, "Matt");
		final Function scaleZones = new ScalePoint(zoneNoise, 0.005f);

		groundBase = new World("M", 0.4f, scaleZones, 1.5f, 2, 0.02f);

		getCamera().setLocation(new Vector3f(4.8676667f, 128.0f, -8.6687975f));
		getCamera().setRotation(new Quaternion(0.107846506f, -0.69533557f, 0.10674867f, 0.7024829f));

		bufferedImage = new BufferedImage(600, 256, BufferedImage.TYPE_INT_ARGB);

		// final HudScreenController sc = new HudScreenController();
		// stateManager.attach(sc);

		// final NiftyJmeDisplay niftyDisplay = new
		// NiftyJmeDisplay(assetManager, inputManager, audioRenderer,
		// guiViewPort);
		// final Nifty nifty = niftyDisplay.getNifty();
		// nifty.fromXml("Interface/hud/screen.xml", "hud", sc);
		// guiViewPort.addProcessor(niftyDisplay);

		final DirectionalLight sunDirectionalLight = new DirectionalLight();
		sunDirectionalLight.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		sunDirectionalLight.setColor(ColorRGBA.White);
		rootNode.addLight(sunDirectionalLight);
		final AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.Yellow.mult(2));
		rootNode.addLight(ambientLight);

		final WorldNode w = new WorldNode(CHUNK_RADIUS, new Vector3f(16, 256, 16), assetManager);
		final WorldController controller = new WorldController(w, getCamera());
		w.addControl(controller);
		rootNode.attachChild(w);

		// TODO: Subclass AppSettings and add custom settings.
		flyCam.setMoveSpeed(5.0f);
	}

	@SuppressWarnings("unused")
	private final void updateMiniMap() {
		final Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

		final float xStart = getCamera().getLocation().x - 307;
		final float xEnd = getCamera().getLocation().x + 307;

		for (float x = xStart; x < xEnd; x++) {
			for (float y = 0; y < 192; y++) {
				final float finalNoise = groundBase.get(x, y, getCamera().getLocation().getZ());
				final Color c = colorMaker.getColorOrDefault(finalNoise, Color.BLACK);
				g2d.setColor(c);
				g2d.drawLine((int) x + 307, (int) y, (int) x + 307, (int) y);
			}
		}
		g2d.dispose();

		final Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/Gui/miniMap_a.png");
		final AWTLoader loader = new AWTLoader();
		Image imageJME = null;
		imageJME = loader.load(bufferedImage, true);
		tex.setImage(imageJME);
		stateManager.getState(HudScreenController.class).updateMiniMap(tex);
	}

}
