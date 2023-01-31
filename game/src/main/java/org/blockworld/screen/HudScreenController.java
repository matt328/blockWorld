/**
 * HudScreenController Author: Matt Teeter May 18, 2012
 */
package org.blockworld.screen;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.state.AbstractAppState;
import com.jme3.niftygui.RenderImageJme;
import com.jme3.texture.Texture2D;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * @author Matt Teeter
 */
public class HudScreenController extends AbstractAppState implements ScreenController {

    private static final Logger LOG = LoggerFactory.getLogger(HudScreenController.class);

    private Screen screen;
    private Nifty nifty;

    @Override
    public final void bind(final @Nonnull Nifty nifty, final @Nonnull Screen screen) {
        this.screen = screen;
        this.nifty = nifty;
    }

    public final void updateMiniMap(final Texture2D t) {
        final NiftyImage n = new NiftyImage(nifty.getRenderEngine(), new RenderImageJme(t));

        Optional.ofNullable(screen.findElementById("testImage"))
                .map(element -> element.getRenderer(ImageRenderer.class))
                .get().setImage(n);
    }

    @Override
    public void update(final float tpf) {
        super.update(tpf);
    }

    @Override
    public final void onStartScreen() {
        LOG.debug("OnStartScreen");
    }

    @Override
    public final void onEndScreen() {
        LOG.debug("OnEndScreen");
    }

}
