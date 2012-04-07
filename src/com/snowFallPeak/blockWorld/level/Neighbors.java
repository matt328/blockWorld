/**
 * 
 */
package com.snowFallPeak.blockWorld.level;

import com.jme3.math.Vector3f;

/**
 * @author Matt
 * 
 */
public enum Neighbors {

	TOP(new Vector3f(0.0f, 1.0f, 0.0f)),
	BOTTOM(new Vector3f(0.0f, -1.0f, 0.0f)),
	LEFT(new Vector3f(-1.0f, 0.0f, 0.0f)),
	RIGHT(new Vector3f(1.0f, 0.0f, 0.0f)),
	FRONT(new Vector3f(0.0f, 0.0f, 1.0f)),
	BACK(new Vector3f(0.0f, 0.0f, -1.0f));

	Neighbors(Vector3f direction) {
		this.direction = direction;
	}

	private Vector3f direction;

	public Vector3f getDirection() {
		return direction;
	}

}
