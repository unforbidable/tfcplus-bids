package com.unforbidable.tfc.bids.features.building.peg.render;

import com.unforbidable.tfc.bids.features.building.peg.render.model.ModelPegLeashKnot;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPegLeashKnot extends Render {

    private static final ResourceLocation leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
    private final ModelPegLeashKnot leashKnotModel = new ModelPegLeashKnot();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float scale = 0.035F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.bindEntityTexture(entity);
        this.leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return leashKnotTextures;
    }

}
