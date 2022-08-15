package com.unforbidable.tfc.bids.Render.Tiles;

import org.lwjgl.opengl.GL11;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackBounds;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class TileRenderDryingRack extends TESRBase {

    static final Vec3[] northSouthItemOffsets = new Vec3[] {
            DryingRackBounds.getRenderItemOffset(0, 0, false),
            DryingRackBounds.getRenderItemOffset(0, 1, false),
            DryingRackBounds.getRenderItemOffset(0, 2, false),
            DryingRackBounds.getRenderItemOffset(0, 3, false)
    };

    static final Vec3[] westEastItemOffsets = new Vec3[] {
            DryingRackBounds.getRenderItemOffset(1, 0, false),
            DryingRackBounds.getRenderItemOffset(1, 1, false),
            DryingRackBounds.getRenderItemOffset(1, 2, false),
            DryingRackBounds.getRenderItemOffset(1, 3, false)
    };

    static final Vec3[] northSouthItemTiedOffsets = new Vec3[] {
            DryingRackBounds.getRenderItemOffset(0, 0, true),
            DryingRackBounds.getRenderItemOffset(0, 1, true),
            DryingRackBounds.getRenderItemOffset(0, 2, true),
            DryingRackBounds.getRenderItemOffset(0, 3, true)
    };

    static final Vec3[] westEastItemTiedOffsets = new Vec3[] {
            DryingRackBounds.getRenderItemOffset(1, 0, true),
            DryingRackBounds.getRenderItemOffset(1, 1, true),
            DryingRackBounds.getRenderItemOffset(1, 2, true),
            DryingRackBounds.getRenderItemOffset(1, 3, true)
    };

    static final float tiedItemOffsetY = 1f / 32 * -2;

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TileEntityDryingRack dryingRack = (TileEntityDryingRack) te;

        if (dryingRack.getWorldObj() != null) {
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
            customitem.hoverStart = 0f;
            float blockScale = 1.0F;

            int orientation = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
            final boolean isNorthSouthOrientation = orientation % 2 == 0;

            for (int i = 0; i < 4; i++) {
                DryingRackItem item = dryingRack.getItem(i);
                if (item != null && item.dryingItem != null) {
                    final boolean tied = item.tyingItem != null;

                    GL11.glPushMatrix(); // start
                    GL11.glScalef(blockScale, blockScale, blockScale);

                    if (isNorthSouthOrientation) {
                        Vec3 offset = tied ? northSouthItemTiedOffsets[i] : northSouthItemOffsets[i];
                        offset = offset.addVector(x, y, z);

                        GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);
                        GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                    } else {
                        Vec3 offset = tied ? westEastItemTiedOffsets[i] : westEastItemOffsets[i];
                        offset = offset.addVector(x, y, z);

                        GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);
                    }

                    customitem.setEntityItemStack(item.dryingItem);
                    itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

                    GL11.glPopMatrix(); // end
                }
            }
        }
    }

}
