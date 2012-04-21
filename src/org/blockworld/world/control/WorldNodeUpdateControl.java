/**
 * WorldNodeUpdateControl
 * Author: Matt Teeter
 * Apr 21, 2012
 */
package org.blockworld.world.control;

import org.blockworld.world.node.WorldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Ray;
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
public class WorldNodeUpdateControl extends AbstractControl {
	private static final Logger LOG = LoggerFactory.getLogger(WorldNodeUpdateControl.class);

	private final WorldNode world;
	private final Camera camera;
	private Ray lastRay;

	/**
	 * @param world
	 * @param camera
	 */
	public WorldNodeUpdateControl(WorldNode world, Camera camera) {
		this.world = world;
		this.camera = camera;
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		final Vector3f location = camera.getLocation();
		final Vector3f direction = camera.getDirection();
		if (world.needUpdate()) {
			LOG.debug("World needs update");
			world.update(location, direction);
			return;
		}
		final Ray ray = new Ray(location, direction);
		if (needUpdate(ray)) {
			LOG.debug("Player Position changed, need world update");
			world.update(location, direction);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

	private boolean needUpdate(final Ray ray) {
		if (lastRay == null) {
			lastRay = ray.clone();
			return true;
		}
		if (lastRay.origin.distance(ray.origin) > world.getChunkSize() / 2 || !lastRay.direction.equals(ray.direction)) {
			lastRay = ray.clone();
			return true;
		}
		return false;
	}
}
