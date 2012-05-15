/**
 * Stopwatch
 * Author: Matt Teeter
 * Apr 28, 2012
 */
package org.blockworld.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 * 
 */
public class Stopwatch {
	private final Logger log;
	private long start;

	private static boolean enabled = false;

	public static void enable() {
		Stopwatch.enabled = true;
	}
	
	public static void disable() {
		Stopwatch.enabled = false;
	}
	
	public Stopwatch(Class<?> sourceClass) {
		log = LoggerFactory.getLogger(sourceClass);
		start = -1;
	}

	public final void start() {
		if (enabled) {
			start = System.currentTimeMillis();
		}
	}

	public final void stop(String format) {
		if (enabled) {
			if (start == -1l) {
				throw new IllegalStateException("Must call start before calling stop");
			}
			long stop = System.currentTimeMillis();
			long duration = stop - start;
			log.debug(String.format(format, duration));
		}
	}
}
