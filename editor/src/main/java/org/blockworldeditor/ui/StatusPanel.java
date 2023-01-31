/**
 * StatusPanel Author: Matt Teeter May 25, 2012
 */
package org.blockworldeditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class StatusPanel extends JPanel implements PropertyChangeListener {
   private static final long serialVersionUID = 1L;

   private final JPanel cursorPositionPanel;
   private final JLabel cursorPositionLabel;

   private final JPanel mapStatusPanel;
   private final JLabel mapStatusLabel;
   private final Icon loadingIcon;

   @SuppressWarnings("unused")
   private final Logger LOG = LoggerFactory.getLogger(StatusPanel.class);

   public StatusPanel() {
      setLayout(new BorderLayout());

      mapStatusPanel = new JPanel();
      mapStatusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      mapStatusLabel = new JLabel("Ready");
      mapStatusPanel.add(mapStatusLabel);
      add(mapStatusPanel, BorderLayout.EAST);

      cursorPositionPanel = new JPanel();
      cursorPositionPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      cursorPositionLabel = new JLabel("<Cursor Position>");
      cursorPositionPanel.add(cursorPositionLabel);
      final Dimension d = cursorPositionPanel.getSize();
      d.width += 100;
      cursorPositionPanel.setPreferredSize(d);
      add(cursorPositionPanel, BorderLayout.WEST);

      loadingIcon = new ImageIcon(StatusPanel.class.getResource("/loading.gif"));
      mapStatusLabel.setIcon(loadingIcon);
   }

   @Override
   public void propertyChange(final PropertyChangeEvent evt) {
      if ("CursorPosition".equalsIgnoreCase(evt.getPropertyName())) {
         final Point position = (Point) evt.getNewValue();
         cursorPositionLabel.setText(String.format("(%d, %d)", position.x - 400, position.y - 400));
      }
      if ("Loading".equalsIgnoreCase(evt.getPropertyName())) {
         final Boolean status = (Boolean) evt.getNewValue();
         setMapStatus(status);
      }
   }

   private void setMapStatus(final boolean status) {
      if (status) {
         mapStatusLabel.setIcon(loadingIcon);
         mapStatusLabel.setText("Loading");
      } else {
         mapStatusLabel.setIcon(null);
         mapStatusLabel.setText("Ready");
      }
   }

}
