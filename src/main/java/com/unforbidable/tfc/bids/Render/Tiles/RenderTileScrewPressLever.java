package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Reference;
import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressDiscPosition;
import com.unforbidable.tfc.bids.Render.RenderHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressLever;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderTileScrewPressLever extends TESRBase {

    protected static final ResourceLocation WOOD_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/wood/WoodSheet/Oak.png");

    private final List<Vec3[]> leverVectors;
    private final List<Vec3[]> nutVectors;
    private final List<Vec3[]> boltVectors;

    public RenderTileScrewPressLever() {
        leverVectors = RenderHelper.boundsToVectors(ScrewPressBounds.getBounds().getLever(), 1.5, -0.5, 0);
        nutVectors = RenderHelper.boundsToVectors(ScrewPressBounds.getBounds().getNut(), -0.5, -0.5, 0);
        boltVectors = RenderHelper.boundsToVectors(ScrewPressBounds.getBounds().getBolt(), -0.5, -0.5, 0);
    }

    private void renderTileEntityScrewPressLeverAt(TileEntityScrewPressLever tileEntity, double posX, double posY, double posZ, float f) {
        ForgeDirection screwDirection = tileEntity.getScrewDirection();
        ScrewPressDiscPosition discPosition = tileEntity.getDiscPosition(f);

        GL11.glPushMatrix();

        GL11.glTranslatef((float)posX, (float)posY, (float)posZ);

        if (screwDirection == ForgeDirection.NORTH) {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotatef(90, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        } else if (screwDirection == ForgeDirection.SOUTH) {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        } else if (screwDirection == ForgeDirection.WEST) {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotatef(180, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        }

        bindTexture(WOOD_TEXTURE);

        if (discPosition.isActive()) {
            if (tileEntity.getScrewTileEntity() != null) {
                float nutOffset = discPosition.getNutOffset();
                for (Vec3[] vs : nutVectors) {
                    RenderHelper.renderBoxWithOffsetAndRotation(vs, 1.5, nutOffset + 0.5, 0, 0);
                }
            }
        } else {
            float holdingBoltOffset = discPosition.getHoldingBoltOffset();
            for (Vec3[] vs : boltVectors) {
                RenderHelper.renderBoxWithOffsetAndRotation(vs, -0.5, holdingBoltOffset, 0, 0);
            }
        }

        float leverAngle = discPosition.getLeverAngle();
        for (Vec3[] vs : leverVectors) {
            RenderHelper.renderBoxWithOffsetAndRotation(vs, -2.5, ScrewPressDiscPosition.getPivotBoltHeight(), 0, leverAngle);
        }

        for (Vec3[] vs : boltVectors) {
            RenderHelper.renderBoxWithOffsetAndRotation(vs, -2.5, ScrewPressDiscPosition.getPivotBoltHeight(), 0, 0);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f) {
        renderTileEntityScrewPressLeverAt((TileEntityScrewPressLever) tileEntity, posX, posY, posZ, f);
    }

}
