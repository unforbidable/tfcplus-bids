package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.SoakingSurface.SoakingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySoakingSurface;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileSoakingSurface extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TileEntitySoakingSurface soakingSurface = (TileEntitySoakingSurface) te;

        if (soakingSurface.getWorldObj() != null) {
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
            customitem.hoverStart = 0f;

            for (int i = 0; i < 4; i++) {
                ItemStack item = soakingSurface.getSlotActualItem(i);

                if (item != null) {
                    GL11.glPushMatrix(); // start

                    Vec3 offset = SoakingSurfaceHelper.getSoakingSurfaceItemVector(i);
                    offset = offset.addVector(x, y, z);

                    GL11.glScalef(1, 1, 1);

                    GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

                    float rotation = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
                    GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

                    customitem.setEntityItemStack(item);
                    itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                    GL11.glPopMatrix(); // end
                }
            }
        }
    }

}
