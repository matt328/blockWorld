/**
 * ApplyAction Author: Matt Teeter May 28, 2012
 */
package org.blockworldeditor.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.blockworldshared.math.functions.loader.FunctionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class ApplyAction extends AbstractAction {
   private static final long serialVersionUID = 1L;

   @SuppressWarnings("unused")
   private final Logger LOG = LoggerFactory.getLogger(ApplyAction.class);

   private final FunctionData functionData;

   public ApplyAction(final FunctionData newFunctionData) {
      super("Apply", new ImageIcon(StatusPanel.class.getResource("/apply.png")));
      putValue(SHORT_DESCRIPTION, "Apply current changes.");
      functionData = newFunctionData;
   }

   @Override
   public void actionPerformed(final ActionEvent e) {
      LOG.debug("Apply Action");
   }
}
