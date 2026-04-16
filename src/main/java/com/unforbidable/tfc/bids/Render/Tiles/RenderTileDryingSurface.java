package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.DryingSurface.DryingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileDryingSurface extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        TileEntityDryingSurface te = (TileEntityDryingSurface) tileentity;

        for (int i = 0; i < te.getSizeInventory(); i++) {
            ItemStack itemStack = te.getSlotActualItem(i);
            if (itemStack != null && !BidsRegistry.ITEM_DRYING_RENDER_INFO.has(itemStack)) {
                Vec3 pos = DryingSurfaceHelper.getDryingSurfaceItemVector(i);
                renderItemStack(x, y, z, itemStack, pos);
            }
        }
    }

    private void renderItemStack(double x, double y, double z, ItemStack is, Vec3 pos) {
        EntityItem customItem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
        customItem.hoverStart = 0f;

        float scale = 0.85f;

        GL11.glPushMatrix(); // start

        Vec3 offset = pos.addVector(x, y, z);
        GL11.glTranslated(offset.xCoord, offset.yCoord - 1 + 0.03, offset.zCoord - 0.2);

        GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(scale, scale, scale);

        customItem.setEntityItemStack(is);
        itemRenderer.doRender(customItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix(); // end
    }

}
