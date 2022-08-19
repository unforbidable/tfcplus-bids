package com.unforbidable.tfc.bids.Render.Tiles;

import org.lwjgl.opengl.GL11;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.ChoppingBlock.ChoppingBlockHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChoppingBlock;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockManager;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class TileRenderChoppingBlock extends TESRBase {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TileEntityChoppingBlock choppingBlock = (TileEntityChoppingBlock) te;

        if (choppingBlock.getWorldObj() != null) {
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
            customitem.hoverStart = 0f;

            for (int i = 0; i < 4; i++) {
                ItemStack item = choppingBlock.getItem(i);

                if (item != null) {
                    GL11.glPushMatrix(); // start

                    final boolean isLargeItem = i == 0 && ChoppingBlockHelper.isLargeItem(item);
                    final boolean isTool = ChoppingBlockManager.isChoppingBlockTool(choppingBlock.getWorkbenchId(),
                            item);

                    float scale = isLargeItem ? 2.0F : 1.0F;

                    Vec3 offset = isLargeItem || isTool
                            ? ChoppingBlockHelper.getChoppingBlockCenterVector()
                            : ChoppingBlockHelper.getChoppingBlockItemVector(i);

                    offset = offset.addVector(x, y, z);

                    GL11.glScalef(scale, scale, scale);

                    GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);

                    if (isTool) {
                        GL11.glTranslated(0, 0.025, -0.25);
                        GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
                    } else {
                        float rotation = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
                        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
                    }

                    customitem.setEntityItemStack(item);
                    itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                    GL11.glPopMatrix(); // end
                }
            }
        }
    }

}
