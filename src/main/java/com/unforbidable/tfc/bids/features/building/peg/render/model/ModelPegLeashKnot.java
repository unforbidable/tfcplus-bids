package com.unforbidable.tfc.bids.features.building.peg.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPegLeashKnot extends ModelBase {

    public ModelRenderer renderer;

    public ModelPegLeashKnot() {
        this(0, 0, 32, 32);
    }

    public ModelPegLeashKnot(int x, int y, int width, int height) {
        this.textureWidth = width;
        this.textureHeight = height;
        this.renderer = new ModelRenderer(this, x, y);
        this.renderer.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float time, float swingProgress, float prevTicks, float yaw, float pitch, float scale) {
        this.setRotationAngles(time, swingProgress, prevTicks, yaw, pitch, scale, entity);
        this.renderer.render(scale);
    }

    public void setRotationAngles(float time, float swingProgress, float prevTicks, float yaw, float pitch, float scale, Entity entity) {
        super.setRotationAngles(time, swingProgress, prevTicks, yaw, pitch, scale, entity);
        this.renderer.rotateAngleY = yaw / 57.295776F;
        this.renderer.rotateAngleX = pitch / 57.295776F;
    }

}
