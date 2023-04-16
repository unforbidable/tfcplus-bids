package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileRenderHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderWoodPile implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        TileEntityWoodPile te = (TileEntityWoodPile) world.getTileEntity(x, y, z);

        RenderBlocksWithRotation rendererAlt = new RenderBlocksWithRotation(renderer);
        rendererAlt.renderAllFaces = true;
        rendererAlt.staticTexture = true;

        for (WoodPileItemBounds itemBounds : te.getItemBounds()) {
            final ItemStack item = itemBounds.getItemStack();
            final IWoodPileRenderProvider provider = itemBounds.getRenderProvider();
            final boolean isRowRotated = itemBounds.isRowRotated();
            final AxisAlignedBB bounds = itemBounds.getBounds();

            final WoodPileRenderHelper helper = new WoodPileRenderHelper(isRowRotated);
            provider.onWoodPileRender(item, isRowRotated, helper);
            helper.apply(rendererAlt);

            rendererAlt.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ,
                    bounds.maxX, bounds.maxY, bounds.maxZ);
            rendererAlt.renderStandardBlock(block, x, y, z);
        }

        rendererAlt.renderAllFaces = false;

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
