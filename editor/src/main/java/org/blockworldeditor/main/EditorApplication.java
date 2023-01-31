/**
 * NoiseTester Author: Matt Teeter Apr 29, 2012
 */
package org.blockworldeditor.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.blockworldshared.math.functions.Function;
import org.blockworldshared.math.functions.loader.FunctionData;
import org.blockworldeditor.ui.SideViewPanel;
import org.blockworldeditor.ui.StatusPanel;
import org.blockworldeditor.ui.TopViewPanel;
import org.blockworldeditor.ui.function.FunctionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class EditorApplication extends JFrame {
   private final Logger LOG = LoggerFactory.getLogger(EditorApplication.class);
   private static final long serialVersionUID = 1L;
   private final SideViewPanel sideViewPanel;
   private final JButton applyButton;
   private final StatusPanel statusPanel;

   public EditorApplication() throws Exception {
      super("Blockworld Map Editor");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      getContentPane().setLayout(new BorderLayout());
      setResizable(false);
      final FunctionData functionData = new FunctionData("/function.xml");
      final Function zoneSelector = functionData.getFunctionById("ZoneSelector");

      applyButton = new JButton("Apply");
      final TopViewPanel mapPanel = new TopViewPanel(zoneSelector, applyButton);

      final Dimension leftPanelSize = new Dimension(800, 800);
      mapPanel.setPreferredSize(leftPanelSize);
      mapPanel.setMinimumSize(leftPanelSize);
      mapPanel.setMaximumSize(leftPanelSize);
      mapPanel.setBackground(Color.BLUE);

      final JPanel leftPanel = new JPanel();
      leftPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      leftPanel.setLayout(new BorderLayout());
      leftPanel.add(mapPanel, BorderLayout.CENTER);

      getContentPane().add(leftPanel, BorderLayout.CENTER);

      final JPanel rightPanel = new JPanel();
      rightPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

      sideViewPanel = new SideViewPanel(zoneSelector);
      sideViewPanel.setPosition(400, 400);

      mapPanel.addPropertyChangeListener(sideViewPanel);

      rightPanel.setLayout(new BorderLayout());
      rightPanel.add(sideViewPanel, BorderLayout.NORTH);

      final FunctionPanel configPanel = new FunctionPanel(functionData);

      rightPanel.add(configPanel, BorderLayout.CENTER);
      getContentPane().add(rightPanel, BorderLayout.EAST);

      statusPanel = new StatusPanel();

      mapPanel.addPropertyChangeListener(statusPanel);

      getContentPane().add(statusPanel, BorderLayout.SOUTH);

      pack();
      setLocation(10, 10);
   }

   /**
    * @param args
    */
   public static void main(final String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditorApplication t;
            try {
               t = new EditorApplication();
               t.setVisible(true);
               t.applyButton.doClick();
            } catch (final Exception e) {
               e.printStackTrace();
               System.exit(-1);
            }
         }
      });
   }

   @SuppressWarnings("unused")
   private void logOutputRange(final Function f, final float xStart, final float xEnd, final float yStart, final float yEnd, final float zStart, final float zEnd) {
      float min = Float.MAX_VALUE;
      float max = Float.MIN_VALUE;
      for (float x = xStart; x <= xEnd; x++) {
         for (float y = yStart; y <= yEnd; y++) {
            for (float z = zStart; z <= zEnd; z++) {
               final float value = f.get(x, y, z);
               min = Math.min(value, min);
               max = Math.max(value, max);
            }
         }
      }
      LOG.debug(String.format("Max: %f, Min: %f", max, min));
   }

}
