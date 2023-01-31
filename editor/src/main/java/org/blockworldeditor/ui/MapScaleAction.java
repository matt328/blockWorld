/**
 * MapScaleAction
 * Author: Matt Teeter
 * May 20, 2012
 */
package org.blockworldeditor.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 *
 */
public class MapScaleAction extends AbstractAction {
   private final Logger LOG = LoggerFactory.getLogger(MapScaleAction.class);
   private static final long serialVersionUID = 1L;
   private final JTextField scaleTextField;
   private final TopViewPanel mapPanel;

   public MapScaleAction(final JTextField scaleTextField, final TopViewPanel mapPanel) {
      super("Apply");
      this.scaleTextField = scaleTextField;
      this.mapPanel = mapPanel;
   }

   @Override
   public void actionPerformed(final ActionEvent e) {
      try {
         final float scale = Float.parseFloat(scaleTextField.getText());
         ((JButton)e.getSource()).setEnabled(false);
         mapPanel.startTimer();
         mapPanel.setScale(scale);
      } catch (final Exception ex) {
         LOG.warn(String.format("%s is not a valid floating point number", scaleTextField.getText()));
      }
   }

}
