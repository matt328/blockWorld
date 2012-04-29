/**
 * ChunkSetController
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.world.node;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * @author Matt Teeter
 * 
 */
public class ChunkSetController extends AbstractControl {
	private final ChunkSetNode chunkSet;
	private final Camera camera;

	public ChunkSetController(ChunkSetNode chunkSet, Camera camera) {
		this.chunkSet = chunkSet;
		this.camera = camera;
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return new ChunkSetController(chunkSet, camera);
	}

	@Override
	protected void controlUpdate(float tpf) {
		chunkSet.update(camera.getLocation(), Vector3f.ZERO);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}
