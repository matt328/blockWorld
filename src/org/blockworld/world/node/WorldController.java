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
 */
public class WorldController extends AbstractControl {
	private final WorldNode worldNode;
	private final Camera camera;

	public WorldController(WorldNode world, Camera camera) {
		this.worldNode = world;
		this.camera = camera;
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return new WorldController(worldNode, camera);
	}

	@Override
	protected void controlUpdate(float tpf) {
		worldNode.update(camera.getLocation(), Vector3f.ZERO);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}
