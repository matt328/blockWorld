/**
 * SwingConsole
 * Author: Matt Teeter
 * Apr 24, 2012
 */
package org.blockworld.scripting;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bsh.util.JConsole;

/**
 * @author Matt Teeter
 * 
 */
public class SwingConsole extends JFrame {

	private static final long serialVersionUID = 1L;
	private JConsole console;

	public SwingConsole() {
		super("Console Window");
		console = new JConsole();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(console, BorderLayout.CENTER);
		panel.setBackground(Color.black);

		getRootPane().setLayout(new BorderLayout());
		getRootPane().add(panel, BorderLayout.CENTER);
		setSize(700, 400);
		setVisible(true);
	}

	public final JConsole getConsole() {
		return console;
	}
}
