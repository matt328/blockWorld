package org.blockworldeditor.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorScan {
   JPanel component;
   JLabel imageLabel;
   JLabel xLabel;
   JLabel yLabel;
   JLabel redLabel;
   JLabel greenLabel;
   JLabel blueLabel;

   private JPanel getContent() {
      final JPanel panel = new JPanel(new GridLayout(1, 0));
      panel.add(getComponent());
      imageLabel = new JLabel();
      imageLabel.addMouseMotionListener(mml);
      panel.add(imageLabel);
      return panel;
   }

   private JPanel getComponent() {
      component = new JPanel(new GridBagLayout()) {
         private static final long serialVersionUID = 1L;

         @Override
         protected void paintComponent(final Graphics g) {
            final Graphics2D g2 = (Graphics2D) g;
            final int w = getWidth();
            final int h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, new Color(200, 200, 130), w, h, new Color(200, 150, 200)));
            g2.fillRect(0, 0, w, h);
         }
      };
      component.setBorder(BorderFactory.createLineBorder(Color.red));
      final GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.weighty = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      component.add(new JTextField(12), gbc);
      component.add(new JButton("Button"), gbc);
      final JPanel child = new JPanel();
      child.setPreferredSize(new Dimension(160, 50));
      child.setBackground(Color.cyan);
      component.add(child, gbc);
      return component;
   }

   private void setImage() {
      // Give the gui a chance to be displayed and to
      // settle down before we take a snapshot of it.
      final Thread thread = new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               Thread.sleep(400);
            } catch (final InterruptedException e) {
               System.out.println("interrupted");
            }
            final BufferedImage image = getImage();
            imageLabel.setIcon(new ImageIcon(image));
         }
      });
      thread.setPriority(Thread.NORM_PRIORITY);
      thread.start();
   }

   private BufferedImage getImage() {
      final int w = component.getWidth();
      final int h = component.getHeight();
      final int type = BufferedImage.TYPE_INT_RGB;
      final BufferedImage image = new BufferedImage(w, h, type);
      final Graphics2D g2 = image.createGraphics();
      component.paint(g2);
      g2.dispose();
      return image;
   }

   private JPanel getLabelPanel() {
      final JPanel panel = new JPanel(new GridLayout(1, 0));
      panel.add(getPositionPanel());
      panel.add(getColorPanel());
      return panel;
   }

   private JPanel getPositionPanel() {
      xLabel = new JLabel(" ");
      yLabel = new JLabel(" ");
      final Dimension d = new Dimension(45, 25);
      final JPanel panel = new JPanel(new GridBagLayout());
      final GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(2, 2, 2, 2);
      gbc.weightx = 1.0;
      addComponents(new JLabel("x"), xLabel, panel, gbc, d);
      addComponents(new JLabel("y"), yLabel, panel, gbc, d);
      return panel;
   }

   private JPanel getColorPanel() {
      redLabel = new JLabel(" ");
      greenLabel = new JLabel(" ");
      blueLabel = new JLabel(" ");
      final Dimension d = new Dimension(45, 25);
      final JPanel panel = new JPanel(new GridBagLayout());
      final GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(2, 2, 2, 2);
      gbc.weightx = 1.0;
      addComponents(new JLabel("r"), redLabel, panel, gbc, d);
      addComponents(new JLabel("g"), greenLabel, panel, gbc, d);
      addComponents(new JLabel("b"), blueLabel, panel, gbc, d);
      return panel;
   }

   private void addComponents(final JComponent c1, final JComponent c2, final Container c, final GridBagConstraints gbc, final Dimension d) {
      gbc.anchor = GridBagConstraints.EAST;
      c.add(c1, gbc);
      c2.setPreferredSize(d);
      gbc.anchor = GridBagConstraints.WEST;
      c.add(c2, gbc);
   }

   public static void main(final String[] args) {
      final ColorScan test = new ColorScan();
      final JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(test.getContent());
      f.getContentPane().add(test.getLabelPanel(), "Last");
      f.setSize(500, 400);
      f.setLocation(100, 200);
      f.setVisible(true);
      test.setImage();
   }

   private final MouseMotionListener mml = new MouseMotionAdapter() {
      @Override
      public void mouseMoved(final MouseEvent e) {
         final Point p = e.getPoint();
         xLabel.setText(String.valueOf(p.x));
         yLabel.setText(String.valueOf(p.y));
         final int rgb = getColorValue(p);
         redLabel.setText(String.valueOf((rgb >> 16) & 0xff));
         greenLabel.setText(String.valueOf((rgb >> 8) & 0xff));
         blueLabel.setText(String.valueOf(rgb & 0xff));
      }
   };

   private int getColorValue(final Point p) {
      if (imageLabel.getIcon() == null) {
         return 0;
      }
      final BufferedImage image = (BufferedImage) ((ImageIcon) imageLabel.getIcon()).getImage();
      if ((p.x < image.getWidth()) && (p.y < image.getHeight())) {
         try {
            return image.getRGB(p.x, p.y);
         } catch (final ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage() + " for p = [" + p.x + ", " + p.y + "]");
         }
      }
      return 0;
   }
}
