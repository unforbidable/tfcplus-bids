package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.Core.DryingSurface.DryingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingItemRenderInfo;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public class RenderDryingSurface implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        renderer.renderAllFaces = true;

        TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y, z);

        for (int i = 0; i < TileEntityDryingSurface.MAX_STORAGE; i++) {
            DryingItem dryingItem = te.getItem(i);
            if (dryingItem != null) {
                IDryingItemRenderInfo renderInfo = BidsRegistry.DRYING_ITEM_RENDER_INFO.get(dryingItem.getCurrentItem().getItem());
                if (renderInfo != null) {
                    Vec3 pos = DryingSurfaceHelper.getDryingSurfaceItemVector(i);
                    AxisAlignedBB bounds = renderInfo.getRenderBounds(dryingItem);
                    renderer.setRenderBounds(bounds.minX * 0.5 + pos.xCoord - 0.25, bounds.minY * 0.5, bounds.minZ * 0.5 + pos.zCoord - 0.25,
                        bounds.maxX * 0.5 + pos.xCoord - 0.25, bounds.maxY * 0.5, bounds.maxZ * 0.5 + pos.zCoord - 0.25);

                    IIcon icon = renderInfo.getRenderIcon(dryingItem);
                    renderer.setOverrideBlockTexture(icon);

                    int color = renderInfo.getRenderColor(dryingItem);
                    float r = (color >> 16 & 255) / 255.0F;
                    float g = (color >> 8 & 255) / 255.0F;
                    float b = (color & 255) / 255.0F;
                    renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);

                    renderer.clearOverrideBlockTexture();
                }
            }
        }

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
