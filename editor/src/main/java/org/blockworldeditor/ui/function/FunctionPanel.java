/**
 * FunctionPanel Author: Matt Teeter May 28, 2012
 */
package org.blockworldeditor.ui.function;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.blockworldshared.math.functions.loader.FunctionData;
import org.blockworldshared.math.functions.loader.Functions.Function;
import org.blockworldshared.math.functions.loader.Functions.Function.FloatComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.FunctionComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.IntComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.StringComponent;
import org.blockworldeditor.ui.ApplyAction;
import org.blockworldeditor.ui.util.LabeledPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * @author Matt Teeter
 */
public class FunctionPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   private final Logger LOG = LoggerFactory.getLogger(FunctionPanel.class);

   private final Map<String, Component> floatComponents;
   private final Map<String, Component> intComponents;
   private final Map<String, Component> stringComponents;

   /**
    * @param functionData
    */
   public FunctionPanel(final FunctionData functionData) {
      floatComponents = Maps.newHashMap();
      intComponents = Maps.newHashMap();
      stringComponents = Maps.newHashMap();

      setLayout(new BorderLayout());
      final JPanel mainPanel = new JPanel();

      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      for (final Function fData : functionData.getAllFunctions()) {
         LOG.debug(String.format("Creating panel for function %s", fData.getId()));
         final JPanel fPanel = new JPanel();
         fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.Y_AXIS));
         fPanel.setBorder(BorderFactory.createTitledBorder(fData.getId()));

         // Set up Float Components
         for (final FloatComponent fc : fData.getFloatComponent()) {
            final JTextField tf = new JTextField(Float.toString(fc.getValue()));
            floatComponents.put(fData.getId() + "." + fc.getName(), tf);
            tf.setMaximumSize(new Dimension(50, 22));
            tf.setMinimumSize(new Dimension(50, 22));
            tf.setPreferredSize(new Dimension(50, 22));
            final LabeledPanel lp = new LabeledPanel(fc.getName() + "(float): ", tf);
            fPanel.add(lp);
            fPanel.add(Box.createVerticalStrut(5));
         }

         // Set up Int Components
         for (final IntComponent ic : fData.getIntComponent()) {
            final JTextField tf = new JTextField(Integer.toString(ic.getValue()));
            intComponents.put(fData.getId() + "." + ic.getName(), tf);
            tf.setMaximumSize(new Dimension(50, 22));
            tf.setMinimumSize(new Dimension(50, 22));
            tf.setPreferredSize(new Dimension(50, 22));
            final LabeledPanel lp = new LabeledPanel(ic.getName() + "(int): ", tf);
            fPanel.add(lp);
            fPanel.add(Box.createVerticalStrut(5));
         }

         // Set up String Components
         for (final StringComponent sc : fData.getStringComponent()) {
            final JTextField tf = new JTextField(sc.getValue());
            stringComponents.put(fData.getId() + "." + sc.getName(), tf);
            tf.setMaximumSize(new Dimension(80, 22));
            tf.setMinimumSize(new Dimension(80, 22));
            tf.setPreferredSize(new Dimension(80, 22));
            final LabeledPanel lp = new LabeledPanel(sc.getName() + "(String): ", tf);
            fPanel.add(lp);
            fPanel.add(Box.createVerticalStrut(5));
         }

         // Set up Function Components
         for (final FunctionComponent sc : fData.getFunctionComponent()) {

            final JComboBox comboBox = new JComboBox();
            comboBox.setMaximumRowCount(functionData.getAllFunctions().size());
            for (final Function f : functionData.getAllFunctions()) {
               comboBox.addItem(f.getId());
            }

            comboBox.setMaximumSize(new Dimension(140, 22));
            comboBox.setMinimumSize(new Dimension(140, 22));
            comboBox.setPreferredSize(new Dimension(140, 22));
            comboBox.setSelectedItem(sc.getFunctionRef());

            final LabeledPanel lp = new LabeledPanel(sc.getName() + ": ", comboBox);
            fPanel.add(lp);
            fPanel.add(Box.createVerticalStrut(5));
         }
         mainPanel.add(fPanel);
      }

      mainPanel.add(Box.createGlue());

      final JToolBar toolBar = new JToolBar("Function Configuration");
      toolBar.setFloatable(false);
      toolBar.add(new ApplyAction(functionData));
      add(toolBar, BorderLayout.NORTH);

      final JScrollPane scrollPane = new JScrollPane(mainPanel);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      add(scrollPane, BorderLayout.CENTER);
   }

   public Map<String, Component> getFloatComponents() {
      return Maps.newHashMap(floatComponents);
   }

   public Map<String, Component> getIntComponents() {
      return Maps.newHashMap(intComponents);
   }

   public Map<String, Component> getStringComponents() {
      return Maps.newHashMap(stringComponents);
   }

}
