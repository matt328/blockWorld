/**
 * NoiseTester
 * Author: Matt Teeter
 * Apr 29, 2012
 */
package org.blockworld.math;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.blockworld.math.functions.Function;
import org.blockworld.math.functions.PerlinNoiseFunction;
import org.blockworld.math.functions.ScalePoint;
import org.blockworld.util.Stopwatch;

/**
 * @author Matt Teeter
 * 
 */
public class NoiseTester extends JFrame {

	private static final long serialVersionUID = 1L;

	public NoiseTester() {
		super("Noise Tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setLayout(new BorderLayout());
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new GridBagLayout());
		getRootPane().add(parentPanel, BorderLayout.CENTER);
		parentPanel.add(new PaintPanel());
		pack();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NoiseTester t = new NoiseTester();
		t.setVisible(true);
	}

	private class PaintPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private static final int HEIGHT = 256;
		private static final int WIDTH = 1500;

		public PaintPanel() {
			super();
			Dimension size = new Dimension(WIDTH, HEIGHT);
			setMaximumSize(size);
			setPreferredSize(size);
			setMinimumSize(size);
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			Function noise = new PerlinNoiseFunction(1.75f, 2, "Megan Rae".hashCode());
			Function scale = new ScalePoint(noise, 0.02f);
			Stopwatch s = new Stopwatch(NoiseTester.class);
			s.start();
			long numOutOfRange = 0;
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					float mx = x * 1.0f;
					float my = y * 1.0f;
					float finalNoise = Interpolation.scale(scale.get(mx, my, 0.0234f), -1.0f, 1.0f, 0.0f, 1.0f);
					finalNoise = Interpolation.clamp(finalNoise, 0.0f, 1.0f);
					Color c = new Color(finalNoise, finalNoise, finalNoise);
					g2d.setColor(c);
					g2d.drawLine(x, y, x, y);
				}
			}
			s.stop("Generated Noise in %dms");
			System.out.println("Num out of range: " + numOutOfRange + " out of " + (WIDTH * HEIGHT));
		}
	}

}
