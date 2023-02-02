/**
 * TopViewPanel Author: Matt Teeter May 19, 2012
 */
package org.blockworldeditor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import org.apache.commons.lang3.Range;
import org.blockworldshared.math.Interpolation;
import org.blockworldshared.math.functions.ColorMaker;
import org.blockworldshared.math.functions.Function;
import org.blockworldshared.math.functions.ScalePoint;

/**
 * @author Matt Teeter
 */
public class TopViewPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private final ScalePoint function;
    private final float xStart;
    private final float xEnd;
    private final float zStart;
    private final float zEnd;
    private float scale;
    private final ColorMaker colorMaker;
    private final BufferedImage image;
    private final JButton applyButton;
    private final Color waterBlue = new Color(0, 51, 255, 128);
    private final Color landGreen = new Color(75, 132, 30, 128);
    private final Timer repaintTimer;
    private final PropertyChangeSupport pcs;

    public TopViewPanel(final Function f, final JButton applyButton) {
        this.applyButton = applyButton;
        pcs = new PropertyChangeSupport(this);
        addMouseMotionListener(this);
        addMouseListener(this);

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.function = new ScalePoint(f, 1.0f);
        xStart = -400.0f;
        xEnd = 400.0f;

        zStart = -400.0f;
        zEnd = 400.0f;
        colorMaker = new ColorMaker();
        colorMaker.addColor(Range.between(-1.0f, 0.0f), waterBlue);
        colorMaker.addColor(Range.between(0.0f, 1.0f), landGreen);

        image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        repaintTimer = new Timer(16, this);
    }

    public void startTimer() {
        repaintTimer.start();
    }

    public void stopTimer() {
        repaintTimer.stop();
    }

    public void updateImage() {
        pcs.firePropertyChange("Loading", null, Boolean.TRUE);
        new SwingWorker<Image, Object>() {
            @Override
            protected Image doInBackground() throws Exception {
                final Graphics2D g2d = image.createGraphics();
                try {
                    g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
                    for (float x = xStart; x < xEnd; x++) {
                        for (float y = zStart; y < zEnd; y++) {
                            var x2 = (int) (x - xStart);
                            var y2 = (int) (y - zStart);

                            var finalNoise = function.get(x, 0.0f, y);
                            finalNoise = Interpolation.clamp(finalNoise, -1.0f, 1.0f);
                            final var adjustedNoise = Interpolation.translate(finalNoise, -1.0f, 1.0f,
                                    0.0f, 1.0f);
                            final var terrainColor = new Color(adjustedNoise, adjustedNoise,
                                    adjustedNoise);

                            g2d.setColor(terrainColor);
                            g2d.drawLine(x2, y2, x2, y2);

                            final var zoneColor = colorMaker.getColor(finalNoise);
                            g2d.setColor(zoneColor);
                            g2d.drawLine(x2, y2, x2, y2);

                        }
                    }
                } finally {
                    g2d.dispose();
                }
                return image;
            }

            @Override
            protected void done() {
                repaint();
                applyButton.setEnabled(true);
                repaintTimer.stop();
                pcs.firePropertyChange("Loading", null, Boolean.FALSE);
            }
        }.execute();
    }

    @Override
    public void paint(final Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(final float scale) {
        this.scale = scale;
        function.setScale(scale);
        updateImage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e) {
        pcs.firePropertyChange("CursorPosition", null, e.getPoint());
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        pcs.firePropertyChange("MouseClicked", null, e.getPoint());
        updateImage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
