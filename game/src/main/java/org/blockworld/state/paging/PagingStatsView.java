/**
 * PagingStatsView
 * Author: Matt Teeter
 * May 13, 2012
 */
package org.blockworld.state.paging;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 * @author Matt Teeter
 * 
 */
public class PagingStatsView extends Node implements Control {

	private BitmapText[] labels;
	private String[] statLabels;
	private String[] statData;
	
	private PagingStats stats;
	private boolean enabled = true;
	
	private StringBuilder builder;

	public PagingStatsView(String name, AssetManager assetManager, PagingStats stats) {
		super(name);
		builder = new StringBuilder();
		setQueueBucket(Bucket.Gui);
		setCullHint(CullHint.Never);
		this.stats = stats;

		statLabels = stats.getLabels();
		statData = new String[statLabels.length];
		labels = new BitmapText[statLabels.length];
		
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Console.fnt");
        for (int i = 0; i < labels.length; i++){
            labels[i] = new BitmapText(font);
            labels[i].setLocalTranslation(0, labels[i].getLineHeight() * (i+1), 0);
            attachChild(labels[i]);
        }

        addControl(this);
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return (Control)spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {

	}

	@Override
	public void update(float tpf) {
		if(!isEnabled()) {
			return;
		}
		
		stats.getData(statData);
		for(int i = 0; i < labels.length; i++) {
			builder.setLength(0);
			builder.append(statLabels[i]).append(": ").append(statData[i]);
			labels[i].setText(builder);
		}
	}

	@Override
	public void render(RenderManager rm, ViewPort vp) {

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
