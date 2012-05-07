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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.blockworld.math.Noise.NoiseType;
import org.blockworld.math.functions.Constant;
import org.blockworld.math.functions.Function;
import org.blockworld.math.functions.Gradient;
import org.blockworld.math.functions.PerlinNoiseFunction;
import org.blockworld.math.functions.ScalePoint;
import org.blockworld.math.functions.Selector;
import org.blockworld.math.functions.Turbulence;
import org.blockworld.util.Stopwatch;

/**
 * @author Matt Teeter
 * 
 */
public class NoiseTester extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private final ConfigPanel configPanel;
	private float caveThreshold = 0.05f;
	private float caveFrequency = 1.0f;
	private int caveOctaveCount = 1;

	public NoiseTester() {
		super("Noise Tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setLayout(new BorderLayout());
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new GridBagLayout());
		getRootPane().add(parentPanel, BorderLayout.CENTER);
		parentPanel.add(new PaintPanel());
		pack();

		configPanel = new ConfigPanel();
		configPanel.setCaveFreq(caveFrequency);
		configPanel.setCaveOctaveCount(caveOctaveCount);
		configPanel.setCaveThreshold(caveThreshold);

		configPanel.addPropertyChangeListener(this);

		JFrame optionsFrame = new JFrame();
		optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		optionsFrame.getRootPane().setLayout(new BorderLayout());
		optionsFrame.getRootPane().add(configPanel, BorderLayout.CENTER);
		optionsFrame.setSize(300, 500);
		optionsFrame.setLocation(100, 400);
		optionsFrame.setVisible(true);
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

		private Function groundBase;
		private Function caveFunction;
		
		public PaintPanel() {
			super();
			Dimension size = new Dimension(WIDTH, HEIGHT);
			setMaximumSize(size);
			setPreferredSize(size);
			setMinimumSize(size);
			// Terrain Noise
			Function constNeg1 = new Constant(-1.0f);
			Function const1 = new Constant(1.0f);
			Function groundShape = new PerlinNoiseFunction(NoiseType.PERLIN, 1.75f, 2, "Matt".hashCode());
			Function groundGradient = new Gradient(0.0f, 1.0f);
			Function groundTurbulence = new Turbulence(groundGradient, groundShape, 0.4f);
			Function groundSelector = new Selector(groundTurbulence, constNeg1, const1, 5.3f);
			groundBase = new ScalePoint(groundSelector, 0.02f);
			
			Function ridged = new PerlinNoiseFunction(NoiseType.RIDGED_MULTIFRACTAL, caveFrequency, caveOctaveCount, "Matt".hashCode());
			Function ridgedThreshold = new Selector(ridged, constNeg1, const1, caveThreshold);
			caveFunction = new ScalePoint(ridgedThreshold, 0.01f);
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;

			Stopwatch s = new Stopwatch(NoiseTester.class);
			s.start();
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					float mx = x * 1.0f;
					float my = y * 1.0f;
					float finalNoise = caveFunction.get(mx, my, 1.0f);
					finalNoise = Interpolation.scale(finalNoise, -1.0f, 1.0f, 0.0f, 1.0f);
					min = Math.min(min, finalNoise);
					max = Math.max(max, finalNoise);
					finalNoise = Interpolation.clamp(finalNoise, 0.0f, 1.0f);
					Color c = new Color(finalNoise, finalNoise, finalNoise);
					g2d.setColor(c);
					g2d.drawLine(x, y, x, y);
				}
			}
			s.stop("Generated Noise in %dms");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("cavethreshold".equalsIgnoreCase(evt.getPropertyName())) {
			this.caveThreshold = (Float) evt.getNewValue();
		}
		if ("caveoctaves".equalsIgnoreCase(evt.getPropertyName())) {
			this.caveOctaveCount = (Integer) evt.getNewValue();
		}
		if ("cavefrequency".equalsIgnoreCase(evt.getPropertyName())) {
			this.caveFrequency = (Float) evt.getNewValue();
		}
		this.repaint();
	}

}
