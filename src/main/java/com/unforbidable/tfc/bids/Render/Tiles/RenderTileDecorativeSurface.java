package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.Common.Metadata.DecorativeSurfaceMetadata;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDecorativeSurface;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderTileDecorativeSurface extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) tileentity;

        ItemStack itemStack = te.getItem();
        if (itemStack != null) {
            DecorativeSurfaceMetadata meta = DecorativeSurfaceMetadata.of(te);
            if (meta.isHorizontal()) {
                renderItemStackHorizontal(x, y, z, itemStack, meta.getHorizontalOrientation());
            } else {
                renderItemStackVertical(x, y, z, itemStack, meta.getVerticalFace());
            }

        }
    }

    private void renderItemStackVertical(double x, double y, double z, ItemStack itemStack, ForgeDirection face) {
        switch (face) {
            case NORTH:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5, 0.07, 1 - 0.03), 0, false);
                break;
            case SOUTH:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5 - 0.01, 0.07, 0.03), 180, false);
                break;
            case WEST:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(1 - 0.03, 0.07, 0.5 - 0.01), 90, false);
                break;
            case EAST:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.03, 0.07, 0.5), 270, false);
                break;
        }
    }

    private void renderItemStackHorizontal(double x, double y, double z, ItemStack itemStack, int orientation) {
        switch (orientation) {
            case 0:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5, 0.04, 0.07), 0, true);
                break;
            case 1:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(1 - 0.07, 0.04, 0.5 - 0.01), 270, true);
                break;
            case 2:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5, 0.04, 1 - 0.06), 180, true);
                break;
            case 3:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.07, 0.04, 0.5), 90, true);
                break;
        }
    }

    private void renderItemStack(double x, double y, double z, ItemStack is, Vec3 pos, int angle, boolean horizontal) {
        EntityItem customItem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
        customItem.hoverStart = 0f;

        float scale = 1.9f;

        GL11.glPushMatrix(); // start

        Vec3 offset = pos.addVector(x, y, z);
        GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);

        if (horizontal) {
            GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
        }

        GL11.glScalef(scale, scale, scale);

        customItem.setEntityItemStack(is);
        itemRenderer.doRender(customItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix(); // end
    }

}

