package com.unforbidable.tfc.bids.features.device.dryingframe.render;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileDryingPegs extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        TileEntityDryingPegs te = (TileEntityDryingPegs) tileentity;

        if (te.hasItem()) {
            ItemStack itemStack = te.getItem().inputItem;
            if (itemStack != null) {
                renderItemStackHorizontal(x, y, z, itemStack, 0);
            }
        }
    }

    private void renderItemStackHorizontal(double x, double y, double z, ItemStack itemStack, int orientation) {
        switch (orientation) {
            case 0:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5, 0.34, 0.07), 0);
                break;
            case 1:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(1 - 0.07, 0.34, 0.5 - 0.01), 270);
                break;
            case 2:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.5, 0.34, 1 - 0.06), 180);
                break;
            case 3:
                renderItemStack(x, y, z, itemStack, Vec3.createVectorHelper(0.07, 0.34, 0.5), 90);
                break;
        }
    }

    private void renderItemStack(double x, double y, double z, ItemStack is, Vec3 pos, int angle) {
        EntityItem customItem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
        customItem.hoverStart = 0f;

        float scale = 1.9f;

        GL11.glPushMatrix(); // start

        Vec3 offset = pos.addVector(x, y, z);
        GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(scale, scale, scale);

        customItem.setEntityItemStack(is);
        itemRenderer.doRender(customItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix(); // end
    }

}

