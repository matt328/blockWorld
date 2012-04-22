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
		// http://www.slf4j.org/legacy.html#jul-to-slf4j
		final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		final Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) {
		LOG.info("Blockworld Starts");
		BlockWorldApplication app = new BlockWorldApplication(new SettingsProvider(new AppSettings(true)));
		app.setShowSettings(false);
		app.start();
	}
}
