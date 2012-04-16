/**
 * 
 */
package org.blockworld.level;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.control.UpdateControl;

/**
 * @author matt
 * 
 */
public class ChunkSetControl extends UpdateControl {

	private Camera camera;

	public ChunkSetControl(Camera camera) {
		this.camera = camera;

	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		Control c = new ChunkSetControl(this.camera);
		return c;
	}

	@Override
	protected void controlUpdate(float tpf) {
		super.controlUpdate(tpf);
		if (spatial instanceof ChunkSet) {
			ChunkSet chunkSet = (ChunkSet) spatial;
			chunkSet.updateLoadedChunks(camera.getLocation());
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		super.controlRender(rm, vp);
	}

}
