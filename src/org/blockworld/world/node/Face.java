/**
 * Face
 * Author: Matt Teeter
 * Apr 22, 2012
 */
package org.blockworld.world.node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matt Teeter
 * 
 */
public enum Face {
	FACE_NO("none"), FACE_RIGHT("right"), FACE_LEFT("left"), FACE_UP("up"), FACE_DOWN("down"), FACE_FRONT("front"), FACE_BACK("back");
	private String str;
	private static Map<String, Face> faceMap = new HashMap<String, Face>(Face.values().length);

	static {
		for(Face f : Face.values()) {
			faceMap.put(f.getStr(), f);
		}
	}
	
	private Face(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}
	
	public static Face lookup(String str) {
		return faceMap.get(str);
	}
};