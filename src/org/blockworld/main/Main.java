/**
 * Main
 * Author: Matt Teeter
 * Apr 19, 2012
 */
package org.blockworld.main;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.jme3.system.AppSettings;

/**
 * @author Matt Teeter
 * 
 */
public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	static {
		// JME uses java.util.logging - bridge to slf4 - see
		final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		final Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) {
		LOG.info("Blockworld Starts");
		final BlockWorldApplicationSettingsProvider settingsProvider = new BlockWorldApplicationSettingsProvider(new AppSettings(true));

		final BlockWorldApplication app = new BlockWorldApplication(settingsProvider);

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
}
