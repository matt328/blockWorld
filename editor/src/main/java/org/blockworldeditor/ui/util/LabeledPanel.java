/**
 * LabeledPanel Author: Matt Teeter May 28, 2012
 */
package org.blockworldeditor.ui.util;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class LabeledPanel extends JPanel {
   private static final long serialVersionUID = 1L;

   @SuppressWarnings("unused")
   private final Logger LOG = LoggerFactory.getLogger(LabeledPanel.class);
   private final JLabel label;
   private final JComponent component;

   public LabeledPanel(final String text, final JComponent newComponent) {
      this.label = new JLabel(text);
      this.component = newComponent;
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      add(label);
      add(Box.createGlue());
      add(component);
   }

   public JComponent getComponent() {
      return component;
   }
}
