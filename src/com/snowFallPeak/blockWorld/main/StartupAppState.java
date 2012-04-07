/**
 * 
 */
package com.snowFallPeak.blockWorld.main;

import org.apache.log4j.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

/**
 * @author Matt
 *
 */
public class StartupAppState extends AbstractAppState {

   private final Logger log = Logger.getLogger(getClass());
   
   @Override
   public void initialize(AppStateManager stateManager, Application app) {
      log.debug("StartupAppState.initialize()");
      super.initialize(stateManager, app);
   }

   @Override
   public void update(float tpf) {
      //log.debug("StartupAppState.update()");
      super.update(tpf);
   }

   @Override
   public void render(RenderManager rm) {
      //log.debug("StartupAppState.render()");
      super.render(rm);
   }

}
