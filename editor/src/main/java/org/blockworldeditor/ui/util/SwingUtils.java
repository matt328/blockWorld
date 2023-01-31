/**
 * SwingUtils Author: Matt Teeter May 28, 2012
 */
package org.blockworldeditor.ui.util;

import java.awt.Component;
import java.awt.Container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matt Teeter
 */
public class SwingUtils {
   @SuppressWarnings("unused")
   private final Logger LOG = LoggerFactory.getLogger(SwingUtils.class);

   private SwingUtils() {

   }

   public static Component getComponentById(final Container container, final String componentId) {
      if (container.getComponents().length > 0) {
         for (final Component c : container.getComponents()) {
            if (componentId.equals(c.getName())) {
               return c;
            }
            if (c instanceof Container) {
               return getComponentById((Container) c, componentId);
            }
         }
      }
      return null;
   }

}
