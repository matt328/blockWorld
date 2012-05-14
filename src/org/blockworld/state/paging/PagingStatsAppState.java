/**
 * PagingStatsAppState
 * Author: Matt Teeter
 * May 13, 2012
 */
package org.blockworld.state.paging;

import org.blockworld.world.node.WorldNode;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

/**
 * @author Matt Teeter
 * 
 */
public class PagingStatsAppState extends AbstractAppState {
	protected PagingStatsView pagingStatsView;
	protected boolean showStats = true;
	protected WorldNode world;
	protected Node guiNode;
	protected BitmapText text;
	protected BitmapFont font;

	public PagingStatsAppState(Node guiNode, BitmapFont guiFont, WorldNode w) {
		this.world = w;
		this.guiNode = guiNode;
		this.font = guiFont;
	}

	public void toggleVisible() {
		showStats = !showStats;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		if (app instanceof SimpleApplication) {
			SimpleApplication simpleApp = (SimpleApplication) app;
			if (guiNode == null) {
				guiNode = simpleApp.getGuiNode();
			}
		}

		if (text == null) {
			text = new BitmapText(font, false);
		}
		app.getContext().getSettings().getHeight();
		text.setLocalTranslation(0, app.getContext().getSettings().getHeight(), 0);
		text.setText("Paging Stats Here");
		text.setCullHint(CullHint.Never);
		guiNode.attachChild(text);

		pagingStatsView = new PagingStatsView("PagingStats", app.getAssetManager(), world.getStats());
		pagingStatsView.setLocalTranslation(0, app.getContext().getSettings().getHeight() - text.getLineHeight() - 60, 0);
		pagingStatsView.setEnabled(showStats);
		pagingStatsView.setCullHint(showStats ? CullHint.Never : CullHint.Always);
		guiNode.attachChild(pagingStatsView);
	}

}
