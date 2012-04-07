/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.post.ssao;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.Filter.Pass;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.shader.VarType;
import com.jme3.texture.Image.Format;
import java.io.IOException;

/**
 *
 * @author nehon
 */
public class SSAOFilter extends Filter {

    private Pass normalPass;
    private Vector3f frustumCorner;
    private Vector2f frustumNearFar;
    private Vector2f[] samples = {new Vector2f(1.0f, 0.0f), new Vector2f(-1.0f, 0.0f), new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, -1.0f)};
    private float sampleRadius = 5.1f;
    private float intensity = 1.5f;
    private float scale = 0.2f;
    private float bias = 0.1f;
    private boolean useOnlyAo = false;
    private boolean useAo = true;

    /**
     * Create a Screen Space Ambiant Occlusion Filter
     */
    public SSAOFilter() {
        super("SSAOFilter");
    }

    /**
     *
     * @param vp
     * @param sampleRadius
     * @param intensity
     * @param scale
     * @param bias
     */
    public SSAOFilter(float sampleRadius, float intensity, float scale, float bias) {
        this();
        this.sampleRadius = sampleRadius;
        this.intensity = intensity;
        this.scale = scale;
        this.bias = bias;
    }

    @Override
    public boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    public void preRender(RenderManager renderManager, ViewPort viewPort) {
        Renderer r = renderManager.getRenderer();
        r.setFrameBuffer(normalPass.getRenderFrameBuffer());
        renderManager.getRenderer().clearBuffers(true, true, true);
        renderManager.setForcedTechnique("PreNormalPass");
        renderManager.renderViewPortQueues(viewPort, false);
        renderManager.setForcedTechnique(null);
        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }

    @Override
    public Material getMaterial() {

        material.setVector3("FrustumCorner", frustumCorner);
        material.setFloat("SampleRadius", sampleRadius);
        material.setFloat("Intensity", intensity);
        material.setFloat("Scale", scale);
        material.setFloat("Bias", bias);
        material.setBoolean("UseAo", useAo);
        material.setBoolean("UseOnlyAo", useOnlyAo);
        material.setVector2("FrustumNearFar", frustumNearFar);
        material.setParam("Samples", VarType.Vector2Array, samples);
        return material;
    }

    @Override
    public void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        int screenWidth = w;
        int screenHeight = h;
        normalPass = new Pass();
        normalPass.init(renderManager.getRenderer(), screenWidth, screenHeight, Format.RGBA8, Format.Depth);


        frustumNearFar = new Vector2f();

        float farY = (vp.getCamera().getFrustumTop() / vp.getCamera().getFrustumNear()) * vp.getCamera().getFrustumFar();
        float farX = farY * ((float) screenWidth / (float) screenHeight);
        frustumCorner = new Vector3f(farX, farY, vp.getCamera().getFrustumFar());
        frustumNearFar.x = vp.getCamera().getFrustumNear();
        frustumNearFar.y = vp.getCamera().getFrustumFar();
        material = new Material(manager, "Common/MatDefs/SSAO/ssao.j3md");
        material.setTexture("Normals", normalPass.getRenderedTexture());

    }

    public float getBias() {
        return bias;
    }

    public void setBias(float bias) {
        this.bias = bias;
        if (material != null) {
            material.setFloat("Bias", bias);
        }
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        if (material != null) {
            material.setFloat("Intensity", intensity);
        }

    }

    public float getSampleRadius() {
        return sampleRadius;
    }

    public void setSampleRadius(float sampleRadius) {
        this.sampleRadius = sampleRadius;
        if (material != null) {
            material.setFloat("SampleRadius", sampleRadius);
        }

    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        if (material != null) {
            material.setFloat("Scale", scale);
        }

    }

    public boolean isUseAo() {
        return useAo;
    }

    public void setUseAo(boolean useAo) {
        this.useAo = useAo;
        if (material != null) {
            material.setBoolean("UseAo", useAo);
        }

    }

    public boolean isUseOnlyAo() {
        return useOnlyAo;
    }

    public void setUseOnlyAo(boolean useOnlyAo) {
        this.useOnlyAo = useOnlyAo;
        if (material != null) {
            material.setBoolean("UseOnlyAo", useOnlyAo);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(sampleRadius, "sampleRadius", 5.1f);
        oc.write(intensity, "intensity", 1.5f);
        oc.write(scale, "scale", 0.2f);
        oc.write(bias, "bias", 0.1f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        sampleRadius = ic.readFloat("sampleRadius", 5.1f);
        intensity = ic.readFloat("intensity", 1.5f);
        scale = ic.readFloat("scale", 0.2f);
        bias = ic.readFloat("bias", 0.1f);
    }

    @Override
    public void cleanUpFilter(Renderer r) {
        if (normalPass != null) {
            normalPass.cleanup(r);
        }
    }
}
