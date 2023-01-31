/**
 * ColorMaker
 * Author: Matt Teeter
 * May 16, 2012
 */
package org.blockworldshared.math.functions;

import java.awt.Color;
import java.util.Map;
import org.apache.commons.lang3.Range;
import com.google.common.collect.Maps;

/**
 * @author Matt Teeter
 *
 */
public class ColorMaker {
   private final Map<Range<Float>, Color> rangeMap;
   private Color defaultColor = Color.white;

   public ColorMaker(final Color newDefault) {
      this();
      this.defaultColor = newDefault;
   }

   public ColorMaker() {
      rangeMap = Maps.newHashMap();
   }

   public void addColor(final Range<Float> range, final Color color) {
      rangeMap.put(range, color);
   }

   public Color getColorOrDefault(final float value, final Color defaultColor) {
      for(final Map.Entry<Range<Float>, Color> entry : rangeMap.entrySet()) {
         if(entry.getKey().contains(value)) {
            return entry.getValue();
         }
      }
      return defaultColor;
   }

   public Color getColor(final float value) {
      for(final Map.Entry<Range<Float>, Color> entry : rangeMap.entrySet()) {
         if(entry.getKey().contains(value)) {
            return entry.getValue();
         }
      }
      return defaultColor;
   }
}
