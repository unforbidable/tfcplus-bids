package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.SaddleQuern.LeverBounds;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileRenderHelper;
import com.unforbidable.tfc.bids.Items.ItemPeeledLog;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
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

        if (lever.getLogItem() != null && lever.getLeverPart() != TileEntityStonePressLever.PART_UNDEFINED) {
            final boolean rotated = orientation % 2 == 1;
            final IWoodPileRenderProvider provider = new ItemPeeledLog();

            RenderBlocksWithRotation rendererAlt = new RenderBlocksWithRotation(renderer);
            rendererAlt.renderAllFaces = true;
            rendererAlt.staticTexture = true;

            final WoodPileRenderHelper helper = new WoodPileRenderHelper(rotated);
            provider.onWoodPileRender(lever.getLogItem(), rotated, helper);
            helper.apply(rendererAlt);

            renderBlock(x, y, z, rendererAlt, block, bounds.getLog());

            if (lever.getLeverPart() == TileEntityStonePressLever.PART_BASE) {
                final WoodPileRenderHelper helperPivot = new WoodPileRenderHelper(!rotated);
                provider.onWoodPileRender(lever.getLogItem(), !rotated, helperPivot);
                helperPivot.apply(rendererAlt);

                renderBlock(x, y, z, rendererAlt, block, bounds.getPivot());
            }

            if (lever.getLeverPart() == TileEntityStonePressLever.PART_WEIGHT) {
                if (lever.hasRope()) {
                    // Brown wool (lighter)
                    renderRopes(renderer, x, y, z, Blocks.wool, 12, 8f, bounds.getRopes());
                }
            }
            renderer.renderAllFaces = false;

        }

        return true;
    }

    private void renderRopes(RenderBlocks renderer, int x, int y, int z, Block block, int meta, float color, AxisAlignedBB[] boundsForRopes) {
        int origMetaData = Minecraft.getMinecraft().theWorld.getBlockMetadata(x, y, z);
        Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, meta, 0);

        for (AxisAlignedBB bounds : boundsForRopes) {
            renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
            renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, color, color, color);
        }

        Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, origMetaData, 0);
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
