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

	public Stopwatch(Class<?> sourceClass) {
		log = LoggerFactory.getLogger(sourceClass);
	}

	public final void start() {
		start = System.currentTimeMillis();
	}

	public final void stop(String format) {
		long stop = System.currentTimeMillis();
		long duration = stop - start;
		log.debug(String.format(format, duration));
	}
}
