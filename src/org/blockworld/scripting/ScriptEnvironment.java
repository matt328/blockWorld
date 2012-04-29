/**
 * ScriptEnvironment
 * Author: Matt Teeter
 * Apr 24, 2012
 */
package org.blockworld.scripting;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.EvalError;
import bsh.Interpreter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * @author Matt Teeter
 * 
 */
public class ScriptEnvironment {
	private static final Logger LOG = LoggerFactory.getLogger(ScriptEnvironment.class);
	private final Interpreter bsh;
	private SwingConsole console;
	private final Set<String> registeredPackages;

	public ScriptEnvironment() {
		registeredPackages = Sets.newHashSet();
		console = new SwingConsole();
		bsh = new Interpreter(console.getConsole());
		new Thread(bsh).start();
	}

	public void setAccessibility(boolean flag) throws EvalError {
		if(flag) {
			bsh.eval("setAccessibility(true)");
		} else {
			bsh.eval("setAccessibility(false)");
		}
	}
	
	public void registerObject(String name, Object instance) throws EvalError {
		String packageName = getPackageName(instance.getClass().getName());
		registerPackage(packageName);
		LOG.debug(String.format("setting \"%s: %s\"", name, instance));
		bsh.set(name, instance);
	}

	public void registerPackageForClass(Class<?> clazz) throws EvalError {
		String packageName = getPackageName(clazz.getName());
		registerPackage(packageName);
	}
	
	public void registerPackage(String packageName) throws EvalError {
		if (registeredPackages.add(packageName)) {
			StringBuilder b = new StringBuilder("import ");
			b.append(packageName).append(".*");
			LOG.debug(String.format("evaluating \"%s\"", b.toString()));
			bsh.eval(b.toString());
		}
	}

	/**
	 * Get the package name of the specified class.
	 * 
	 * @param classname
	 *            Class name.
	 * @return Package name or "" if the classname is in the <i>default</i> package.
	 * 
	 * @throws EmptyStringException
	 *             Classname is an empty string.
	 */
	public static String getPackageName(final String classname) {
		Preconditions.checkNotNull(classname, "classname cannot be null");

		int index = classname.lastIndexOf(".");
		if (index != -1)
			return classname.substring(0, index);
		return "";
	}
}
