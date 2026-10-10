package com.unforbidable.tfc.bids.features.device.cookingpit.render;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileHideCookingPit extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TileEntityCookingPit cookingPit = (TileEntityCookingPit) te;

        if (cookingPit.getWorldObj() != null) {
            if (cookingPit.hasInputItem()) {
                EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
                customitem.hoverStart = 0f;

                ItemStack item = cookingPit.getInputItemStack();
                GL11.glPushMatrix(); // start

                Vec3 offset = Vec3.createVectorHelper(x, y, z);

                // Place in the cooking pot
                Vec3 pos = cookingPit.getCachedBounds().getItemPos();
                offset = offset.addVector(pos.xCoord, pos.yCoord, pos.zCoord);

                // Raise above the liquid
                float fullnessRatio = (float)cookingPit.getTotalLiquidVolume() / (float)cookingPit.getMaxFluidVolume();
                offset = offset.addVector(0, cookingPit.getCachedBounds().getMaxFluidHeight() * fullnessRatio, 0);

                float scale = 0.6f;

                GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

                float rotation = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
                GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

                GL11.glScalef(scale, scale, scale);

                customitem.setEntityItemStack(item);
                itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                GL11.glPopMatrix(); // end
            }
        }
    }

}
