/**
 * PaintPanel Author: Matt Teeter May 19, 2012
 */
package org.blockworldeditor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.blockworldshared.math.Interpolation;
import org.blockworldshared.math.functions.Function;
import org.blockworldshared.math.functions.ScalePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class SideViewPanel extends JPanel implements PropertyChangeListener {

   @SuppressWarnings("unused")
   private final Logger LOG = LoggerFactory.getLogger(SideViewPanel.class);
   private static final long serialVersionUID = 1L;
   private static final int HEIGHT = 256;
   private static final int WIDTH = 400;
   private float xStart = 0;
   private float xEnd = xStart + WIDTH;

   private int zPosition;

   private final Function groundBase;

   private final Color SkyBlue = new Color(76, 159, 255);
   private final Color GrassGreen = new Color(75, 132, 30);

   public SideViewPanel(final Function worldFunction) {
      super();
      final Dimension size = new Dimension(WIDTH, HEIGHT);
      setMaximumSize(size);
      setPreferredSize(size);
      setMinimumSize(size);

      groundBase = new ScalePoint(worldFunction, 0.05f);

      zPosition = 0;
   }

   @Override
   public void paint(final Graphics g) {
      final Graphics2D g2d = (Graphics2D) g;
      for (float x = xStart; x < xEnd; x++) {
         for (float y = 0; y < HEIGHT; y++) {
            float finalNoise = groundBase.get(x, 0.0f, zPosition);
            finalNoise = Interpolation.clamp(finalNoise, -1.0f, 1.0f);
            final float adjustedNoise = Interpolation.translate(finalNoise, -1.0f, 1.0f, 0.0f, 256.0f);
            if (y < adjustedNoise) {
               g2d.setColor(GrassGreen);
            } else if ((y > adjustedNoise) && (y < 128.0f)) {
               g2d.setColor(Color.blue);
            } else {
               g2d.setColor(SkyBlue);
            }
            final int mx = (int) (x - xStart);
            final int my = (int) (HEIGHT - y);
            g2d.drawLine(mx, my, mx, my);
            if ((x < -400) || (x > 400)) {
               final Color faded = new Color(0, 0, 0, 128);
               g2d.setColor(faded);
               g2d.drawLine(mx, my, mx, my);
            }
         }
      }
   }

   public final void setPosition(final int pos, final int zPos) {
      this.xStart = (pos - 400) - WIDTH / 2;
      this.xEnd = (pos - 400) + WIDTH / 2;
      this.zPosition = zPos - 400;
      repaint();
   }

   public final void rangeRight() {
      this.xStart += WIDTH / 2;
      this.xEnd += WIDTH / 2;
      repaint();
   }

   public final void rangeLeft() {
      this.xStart -= WIDTH / 2;
      this.xEnd -= WIDTH / 2;
      repaint();
   }

   /*
    * (non-Javadoc)
    * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
    * PropertyChangeEvent)
    */
   @Override
   public void propertyChange(final PropertyChangeEvent evt) {
      if ("MouseClicked".equalsIgnoreCase(evt.getPropertyName())) {
         final Point p = (Point) evt.getNewValue();
         setPosition((int) p.getX(), (int) p.getY());
      }
   }
}
