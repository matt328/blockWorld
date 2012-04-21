/**
 * BlockWorldApplicationSettingsProvider
 * Author: Matt Teeter
 * Apr 19, 2012
 */
package org.blockworld.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;

/**
 * @author Matt Teeter
 * 
 */
public class BlockWorldApplicationSettingsProvider {
	private static final Logger LOG = LoggerFactory.getLogger(BlockWorldApplicationSettingsProvider.class);
	private static final File settingsFile = new File("blockworld.settings");
	private final AppSettings defaultSettings;

	public BlockWorldApplicationSettingsProvider(final AppSettings theDefaultSettings) {
		defaultSettings = theDefaultSettings;
	}

	public AppSettings getAppSettings() {
		if (!settingsFile.exists()) {
			return fromDialogAndSave();
		} else {
			return fromFile();
		}
	}

	private AppSettings fromDialogAndSave() {
		if (!JmeSystem.showSettingsDialog(defaultSettings, false)) {
			throw new IllegalStateException("Settings dialog aborted.");
		}
		// this is necessary to force LWJGL
		defaultSettings.setRenderer(AppSettings.LWJGL_OPENGL2);
		FileOutputStream settingOutputStream = null;
		try {
			settingsFile.createNewFile();
			settingOutputStream = FileUtils.openOutputStream(settingsFile);
			defaultSettings.save(settingOutputStream);
			return defaultSettings;
		} catch (final IOException e) {
			LOG.error("Unable to write settings file '{}'", settingsFile, e);
			return null;
		} finally {
			try {
				settingOutputStream.close();
			} catch (IOException x) {
				LOG.warn("Error closing settings file: ", x);
			}
		}
	}

	private AppSettings fromFile() {
		FileInputStream settingsInputStream = null;
		try {
			settingsInputStream = FileUtils.openInputStream(settingsFile);
			final AppSettings loadedAppSettings = new AppSettings(false);
			loadedAppSettings.load(settingsInputStream);
			return loadedAppSettings;
		} catch (final IOException e) {
			LOG.error("Unable to read settings file '{}'", settingsFile, e);
			return null;
		} finally {
			try {
				settingsInputStream.close();
			} catch (IOException x) {
				LOG.warn("Error closing settings file: ", x);
			}
		}
	}
}
