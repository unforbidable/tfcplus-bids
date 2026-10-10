package com.unforbidable.tfc.bids.features.device.processingsurface.render;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.features.device.processingsurface.main.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileProcessingSurface extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileEntityProcessingSurface processingSurface = (TileEntityProcessingSurface) tileEntity;

        if (processingSurface.getWorldObj() != null) {
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
            customitem.hoverStart = 0f;

            ItemStack itemStack = processingSurface.getCurrentItem();
            if (itemStack != null && !ProcessingSurfaceHelper.renderItemAsSurface(itemStack)) {
                GL11.glPushMatrix(); // start

                float scale = 1.0F;

                Vec3 offset = Vec3.createVectorHelper(0.5, 0, 0.5)
                    .addVector(x, y, z);
                GL11.glScalef(scale, scale, scale);
                GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

                GL11.glTranslated(0, 0.025, -0.25);
                GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);

                customitem.setEntityItemStack(itemStack);
                itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                GL11.glPopMatrix(); // end
            }
        }
    }

}
