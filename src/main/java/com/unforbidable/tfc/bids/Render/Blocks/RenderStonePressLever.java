package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.SaddleQuern.LeverBounds;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileRenderHelper;
import com.unforbidable.tfc.bids.Items.ItemPeeledLog;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class RenderStonePressLever implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        final int orientation = world.getBlockMetadata(x, y, z);
        final LeverBounds bounds = LeverBounds.fromOrientation(orientation);
        final TileEntityStonePressLever lever = (TileEntityStonePressLever) world.getTileEntity(x, y, z);

        final boolean rotated = orientation % 2 == 1;
        final ItemStack log = lever.getLogItem();
        final WoodPileRenderHelper helper = new WoodPileRenderHelper(rotated);
        final IWoodPileRenderProvider provider = new ItemPeeledLog();

        provider.onWoodPileRender(log, rotated, helper);

        RenderBlocksWithRotation rendererAlt = new RenderBlocksWithRotation(renderer);
        rendererAlt.renderAllFaces = true;
        rendererAlt.staticTexture = true;

        helper.apply(rendererAlt);

        renderBlock(x, y, z, rendererAlt, block, bounds.getLogBounds());

        renderer.renderAllFaces = false;

        return true;
    }

    private void renderBlock(int x, int y, int z, RenderBlocks renderer, Block block, AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlock(block, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
