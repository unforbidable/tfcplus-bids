package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPrep;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileCookingPrep extends TESRBase {

    private final static Vec3[] itemPos = new Vec3[5];

    static {
        for (int i = 0; i < 5; i++) {
            itemPos[i] = getItemVector(i);
        }
    }

    public static Vec3 getItemVector(int slot) {
        if (slot == 0) {
            return Vec3.createVectorHelper(0.5, 0, 0.5);
        } else {
            final int row = (slot - 1) % 2;
            final int col = (slot - 1) / 2;

            final float x = 0.25f + col * 0.5f;
            final float y = 0;
            final float z = 0.25f + row * 0.5f;

            return Vec3.createVectorHelper(x, y, z);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TileEntityCookingPrep cookingPrep = (TileEntityCookingPrep) te;

        if (cookingPrep.getWorldObj() != null) {
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
            customitem.hoverStart = 0f;

            float scale = 0.6F;
            float rotation = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            for (int i = 0; i < 5; i++) {
                ItemStack item = cookingPrep.getStackInSlot(i);

                if (item != null) {
                    GL11.glPushMatrix(); // start

                    Vec3 offset = itemPos[i];
                    offset = offset.addVector(x, y, z);

                    GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);
                    GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(scale, scale, scale);

                    customitem.setEntityItemStack(item);
                    itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                    GL11.glPopMatrix(); // end
                }
            }
        }
    }

}
