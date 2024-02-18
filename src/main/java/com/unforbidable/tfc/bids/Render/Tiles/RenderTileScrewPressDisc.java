package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Reference;
import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressDiscPosition;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.Render.RenderHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressDisc;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderTileScrewPressDisc extends TESRBase {

    protected static final ResourceLocation WOOD_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/wood/WoodSheet/Oak.png");

    private final List<Vec3[]> discVectors;

    public RenderTileScrewPressDisc() {
        discVectors = RenderHelper.boundsToVectors(ScrewPressBounds.getBounds().getDiscRotated(), -0.5, -0.5, 0);
    }

    private void renderTileEntityScrewPressDiscAt(TileEntityScrewPressDisc tileEntity, double posX, double posY, double posZ, float f) {
        ScrewPressDiscPosition discPosition = tileEntity.getDiscPosition(f);

        GL11.glPushMatrix();

        GL11.glTranslatef((float)posX, (float)posY, (float)posZ);

        int meta = tileEntity.getBlockMetadata();
        int orientation = ScrewPressHelper.getOrientationFromMetadata(meta);

        GL11.glTranslated(0.5, 0.5, 0.5);
        if (orientation == 1) {
            GL11.glRotatef(90, 0, 1, 0);
            GL11.glRotatef(90, 0, 0, 1);
        } else {
            GL11.glRotatef(90, 0, 0, 1);
        }
        GL11.glTranslated(-0.5, -0.5, -0.5);

        bindTexture(WOOD_TEXTURE);

        float discOffset = discPosition.getDiscOffset();
        for (Vec3[] vs : discVectors) {
            RenderHelper.renderBoxWithOffsetAndRotation(vs, discOffset - 0.5, 0.5, 0, 0f);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f) {
        renderTileEntityScrewPressDiscAt((TileEntityScrewPressDisc) tileEntity, posX, posY, posZ, f);
    }

}
