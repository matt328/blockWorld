/**
 * ConfigPanel
 * Author: Matt Teeter
 * May 4, 2012
 */
package org.blockworld.math;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matt Teeter
 * 
 */
public class ConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTextField caveThresholdField;
	private final JTextField caveOctavesField;
	private final JTextField caveFreqField;
	private final PropertyChangeSupport pcs;
	private final JButton applyButton;

	public ConfigPanel() {
		pcs = new PropertyChangeSupport(this);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		FormLayout layout = new FormLayout("pref, 4dlu, 50dlu, 4dlu, min", "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref");

		layout.setRowGroups(new int[][] { { 1, 3, 5 } });
		setLayout(layout);

		CellConstraints cc = new CellConstraints();

		caveThresholdField = new JTextField();
		caveOctavesField = new JTextField();
		caveFreqField = new JTextField();

		applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					float threshold = Float.parseFloat(caveThresholdField.getText());
					pcs.firePropertyChange("caveThreshold", null, threshold);
				} catch (NumberFormatException nfe) {
					// Do Nothing
				}
				try {
					int caveOctaves = Integer.parseInt(caveOctavesField.getText());
					pcs.firePropertyChange("caveoctaves", null, caveOctaves);
				} catch (NumberFormatException nfe) {
					// Do Nothing
				}
				try {
					float freq = Float.parseFloat(caveFreqField.getText());
					pcs.firePropertyChange("cavefrequency", null, freq);
				} catch (NumberFormatException nfe) {
					// Do Nothing
				}
			}
		});
		add(new JLabel("Cave Threshold"), cc.xy(1, 1));
		add(caveThresholdField, cc.xyw(3, 1, 3));
		add(new JLabel("Cave Octaves"), cc.xy(1, 3));
		add(caveOctavesField, cc.xyw(3, 3, 3));
		add(new JLabel("Cave Frequency"), cc.xy(1, 5));
		add(caveFreqField, cc.xyw(3, 5, 3));
		add(applyButton, cc.xyw(3, 7, 2));
	}

	public void setCaveFreq(float freq) {
		caveFreqField.setText(Float.toString(freq));
	}

	public void setCaveThreshold(Float threshold) {
		caveThresholdField.setText(Float.toString(threshold));
	}
	
	public void setCaveOctaveCount(int octaveCount) {
		caveOctavesField.setText(Integer.toString(octaveCount));
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
