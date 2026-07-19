package com.unforbidable.tfc.bids.features.device.woodpile.render;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.features.device.woodpile.main.DefaultWoodpileRenderConfigurator;
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileItemBounds;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.util.render.RenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class RenderWoodpile implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        TileEntityWoodpile te = (TileEntityWoodpile) world.getTileEntity(x, y, z);

        RenderBlocksWithRotation rendererAlt = new RenderBlocksWithRotation(renderer);
        rendererAlt.renderAllFaces = true;
        rendererAlt.staticTexture = true;

        for (WoodpileItemBounds itemBounds : te.getItemBounds()) {
            final ItemStack item = itemBounds.getItemStack();
            final WoodpileRenderable provider = itemBounds.getRenderProvider();
            final boolean isRowRotated = itemBounds.isRowRotated();
            final AxisAlignedBB bounds = itemBounds.getBounds();

            final DefaultWoodpileRenderConfigurator helper = new DefaultWoodpileRenderConfigurator(isRowRotated);
            provider.configureWoodpileRenderer(item, isRowRotated, helper);
            helper.apply(rendererAlt);

            rendererAlt.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ,
                    bounds.maxX, bounds.maxY, bounds.maxZ);
            rendererAlt.renderStandardBlock(block, x, y, z);
        }

        rendererAlt.renderAllFaces = false;

        if (te.isBurning()) {
            float fireHeight = getFireHeight(te);
            float offsetY = world.getBlock(x, y + 1, z).isOpaqueCube() ? 0 : fireHeight;

            RenderHelper.renderBlockFire(x, y, z, renderer, offsetY, 1, 1, 1f);
            if (fireHeight > 0) {
                // 0.0625 is the original fire block sides offset
                RenderHelper.renderBlockFireSides(x, y, z, renderer, 0.0625f, 1, 1, 1f, false);
            }
        }

        return true;
    }

    private float getFireHeight(TileEntityWoodpile te) {
        if (te.isFull()) {
            return 1;
        } else {
            float height = te.getActualBlockHeight();
            return Math.max(0, height - 0.25f);
        }
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
