/**
 * BlockWorldConfiguration
 * Author: Matt Teeter
 * Apr 18, 2012
 */
package org.blockworld.main;

import org.blockworld.level.ChunkLoader;
import org.blockworld.level.PerlinNoiseChunkLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.jme3.system.AppSettings;

/**
 * @author Matt Teeter
 * 
 */
@Configuration
public class BlockWorldConfiguration {

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public BlockWorldApplicationInterface bloxelApplication() {
		return new BlockWorldApplication(blockWorldApplicationSettingsProvider(), chunkLoader());
	}

	@Bean
	public ChunkLoader chunkLoader() {
		return new PerlinNoiseChunkLoader(342);
	}

	@Bean
	public BlockWorldApplicationSettingsProvider blockWorldApplicationSettingsProvider() {
		return new BlockWorldApplicationSettingsProvider(defaultSettings());
	}

	@Bean
	public AppSettings defaultSettings() {
		final AppSettings appSettings = new AppSettings(true);
		appSettings.setUseJoysticks(true);
		appSettings.setVSync(true);
		appSettings.setWidth(800);
		appSettings.setHeight(600);
		appSettings.setBitsPerPixel(24);
		appSettings.setTitle("Blockworld");
		return appSettings;
	}
}
