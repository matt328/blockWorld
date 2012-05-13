/**
 * Face
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum representing the faces of a cube.
 * 
 * @author Matt Teeter
 */
public enum Face {
	FACE_NO("none", 0),
	FACE_RIGHT("right", 1),
	FACE_LEFT("left", 2),
	FACE_UP("up", 4),
	FACE_DOWN("down", 8),
	FACE_FRONT("front", 16),
	FACE_BACK("back", 32);
	private String str;
	private int value;
	private static Map<String, Face> faceMap = new HashMap<String, Face>(Face.values().length);

	static {
		for(Face f : Face.values()) {
			faceMap.put(f.getStr(), f);
		}
	}
	
	private Face(String str, int value) {
		this.str = str;
		this.value = value;
	}

	public String getStr() {
		return str;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Face lookup(String str) {
		return faceMap.get(str);
	}
};