/**
 * 
 */
package org.blockworld.level;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * @author matt
 * 
 */
public class ChunkSetControl extends AbstractControl {
	
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
		if (spatial instanceof ChunkSet) {
			ChunkSet chunkSet = (ChunkSet) spatial;
			chunkSet.updateLoadedChunks(camera.getLocation());
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

}
