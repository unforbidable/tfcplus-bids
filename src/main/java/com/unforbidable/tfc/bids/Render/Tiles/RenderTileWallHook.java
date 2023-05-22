package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.Common.Bounds.WallHookBounds;
import com.unforbidable.tfc.bids.Core.WallHook.WallHookHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWallHook;
import com.unforbidable.tfc.bids.api.Enums.EnumWallHookPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileWallHook extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        TileEntityWallHook wallHook = (TileEntityWallHook) tileentity;

        ItemStack itemStack = wallHook.getPlacedItem();
        if (itemStack != null) {
            int orientation = tileentity.getBlockMetadata();
            WallHookBounds bounds = WallHookBounds.getBoundsForOrientation(orientation);
            int angle = getOrientationAngle(orientation);
            renderItemStack(x, y, z, itemStack, bounds, angle);
        }
    }

    private int getOrientationAngle(int orientation) {
        return (4 - orientation) * 90;
    }

    private void renderItemStack(double x, double y, double z, ItemStack is, WallHookBounds bounds, int angle) {
        EntityItem customItem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
        customItem.hoverStart = 0f;

        float scale = 1f;
        Vec3 offset = bounds.getItemPos();

        EnumWallHookPos pos = WallHookHelper.getItemStackWallHookPosition(is);
        offset = offset.addVector(0, -pos.getOffset(), 0);

        GL11.glPushMatrix(); // start

        offset = offset.addVector(x, y, z);
        GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

        if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        }

        GL11.glScalef(scale, scale, scale);

        customItem.setEntityItemStack(is);
        itemRenderer.doRender(customItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix(); // end
    }

}

