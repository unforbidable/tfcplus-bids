package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileRenderHelper;
import com.unforbidable.tfc.bids.Render.RenderHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderWoodPile implements ISimpleBlockRenderingHandler {

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

    private float getFireHeight(TileEntityWoodPile te) {
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
