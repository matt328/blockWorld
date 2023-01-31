/**
 * FunctionEditor Author: Matt Teeter May 28, 2012
 */
package org.blockworldeditor.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.blockworldshared.math.functions.loader.FunctionData;
import org.blockworldeditor.ui.function.FunctionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class FunctionEditor extends JFrame {
   private static final long serialVersionUID = 1L;
   private static final Logger LOG = LoggerFactory.getLogger(FunctionEditor.class);

   public FunctionEditor() throws Exception {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final FunctionData functionData = new FunctionData("src/function.xml");
      final FunctionPanel fp = new FunctionPanel(functionData);
      getContentPane().setLayout(new BorderLayout());
      final JScrollPane jsp = new JScrollPane(fp);
      jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      getContentPane().add(jsp, BorderLayout.CENTER);

      pack();

      setVisible(true);
   }

   /**
    * @param args
    * @throws Exception
    * @throws IllegalAccessException
    * @throws InstantiationException
    * @throws ClassNotFoundException
    */
   public static void main(final String[] args) throws Exception {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               new FunctionEditor();
            } catch (final Exception e) {
               LOG.error("Exception occurred", e);
            }
         }
      });
   }
}
