package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileRenderHelper;
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

        if (te.isBurning()) {
            float fireOffsetY = te.getActualBlockHeight();
            renderBlockFireWithOffsetY(x, y, z, fireOffsetY, renderer);
        }

        return true;
    }

    private void renderBlockFireWithOffsetY(int x, int y, int z, float offsetY, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        IIcon iicon2 = iicon;

        if (renderer.hasOverrideBlockTexture())
        {
            iicon2 = renderer.overrideBlockTexture;
        }

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.setBrightness(Blocks.fire.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        double d0 = (double)iicon2.getMinU();
        double d1 = (double)iicon2.getMinV();
        double d2 = (double)iicon2.getMaxU();
        double d3 = (double)iicon2.getMaxV();
        float f = 1.4F;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;

        double d4 = (double)x + 0.5D + 0.2D;
        d5 = (double)x + 0.5D - 0.2D;
        d6 = (double)z + 0.5D + 0.2D;
        d7 = (double)z + 0.5D - 0.2D;
        d8 = (double)x + 0.5D - 0.3D;
        d9 = (double)x + 0.5D + 0.3D;
        d10 = (double)z + 0.5D - 0.3D;
        d11 = (double)z + 0.5D + 0.3D;
        tessellator.addVertexWithUV(d8, (double)((float)y + f + offsetY), (double)(z + 1), d2, d1);
        tessellator.addVertexWithUV(d4, (double)(y + 0 + offsetY), (double)(z + 1), d2, d3);
        tessellator.addVertexWithUV(d4, (double)(y + 0 + offsetY), (double)(z + 0), d0, d3);
        tessellator.addVertexWithUV(d8, (double)((float)y + f + offsetY), (double)(z + 0), d0, d1);
        tessellator.addVertexWithUV(d9, (double)((float)y + f + offsetY), (double)(z + 0), d2, d1);
        tessellator.addVertexWithUV(d5, (double)(y + 0 + offsetY), (double)(z + 0), d2, d3);
        tessellator.addVertexWithUV(d5, (double)(y + 0 + offsetY), (double)(z + 1), d0, d3);
        tessellator.addVertexWithUV(d9, (double)((float)y + f + offsetY), (double)(z + 1), d0, d1);
        d0 = (double)iicon1.getMinU();
        d1 = (double)iicon1.getMinV();
        d2 = (double)iicon1.getMaxU();
        d3 = (double)iicon1.getMaxV();
        tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + offsetY), d11, d2, d1);
        tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0 + offsetY), d7, d2, d3);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0 + offsetY), d7, d0, d3);
        tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + offsetY), d11, d0, d1);
        tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + offsetY), d10, d2, d1);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0 + offsetY), d6, d2, d3);
        tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0 + offsetY), d6, d0, d3);
        tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + offsetY), d10, d0, d1);
        d4 = (double)x + 0.5D - 0.5D;
        d5 = (double)x + 0.5D + 0.5D;
        d6 = (double)z + 0.5D - 0.5D;
        d7 = (double)z + 0.5D + 0.5D;
        d8 = (double)x + 0.5D - 0.4D;
        d9 = (double)x + 0.5D + 0.4D;
        d10 = (double)z + 0.5D - 0.4D;
        d11 = (double)z + 0.5D + 0.4D;
        tessellator.addVertexWithUV(d8, (double)((float)y + f + offsetY), (double)(z + 0), d0, d1);
        tessellator.addVertexWithUV(d4, (double)(y + 0 + offsetY), (double)(z + 0), d0, d3);
        tessellator.addVertexWithUV(d4, (double)(y + 0 + offsetY), (double)(z + 1), d2, d3);
        tessellator.addVertexWithUV(d8, (double)((float)y + f + offsetY), (double)(z + 1), d2, d1);
        tessellator.addVertexWithUV(d9, (double)((float)y + f + offsetY), (double)(z + 1), d0, d1);
        tessellator.addVertexWithUV(d5, (double)(y + 0 + offsetY), (double)(z + 1), d0, d3);
        tessellator.addVertexWithUV(d5, (double)(y + 0 + offsetY), (double)(z + 0), d2, d3);
        tessellator.addVertexWithUV(d9, (double)((float)y + f + offsetY), (double)(z + 0), d2, d1);
        d0 = (double)iicon.getMinU();
        d1 = (double)iicon.getMinV();
        d2 = (double)iicon.getMaxU();
        d3 = (double)iicon.getMaxV();
        tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + offsetY), d11, d0, d1);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0 + offsetY), d7, d0, d3);
        tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0 + offsetY), d7, d2, d3);
        tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + offsetY), d11, d2, d1);
        tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + offsetY), d10, d0, d1);
        tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0 + offsetY), d6, d0, d3);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0 + offsetY), d6, d2, d3);
        tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + offsetY), d10, d2, d1);
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
