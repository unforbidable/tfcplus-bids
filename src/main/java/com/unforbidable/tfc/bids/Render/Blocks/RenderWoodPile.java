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
            float height = te.getActualBlockHeight();
            float fireHeight = Math.max(0, height - 0.25f);

            // An idea could be not to render fire on top when the wood pile is full
            // because when wood pile is full, a Blocks.fire will be placed on top
            // however the Block.fire is not placed immediately
            // and there can be a second or two when no fire is rendered
            // so for now we will render two fires that partially overlap
            renderBlockFireSideUp(x, y, z, renderer, fireHeight);

            renderBlockFireSides(x, y, z, renderer, fireHeight);
        }

        return true;
    }

    private void renderBlockFireSideUp(int x, int y, int z, RenderBlocks renderer, float fireHeight) {
        float offsetY = renderer.blockAccess.getBlock(x, y + 1, z).isOpaqueCube() ? 0 : fireHeight;

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
        double d0 = iicon2.getMinU();
        double d1 = iicon2.getMinV();
        double d2 = iicon2.getMaxU();
        double d3 = iicon2.getMaxV();
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
        tessellator.addVertexWithUV(d8, (float)y + f + offsetY, z + 1, d2, d1);
        tessellator.addVertexWithUV(d4, y + 0 + offsetY, z + 1, d2, d3);
        tessellator.addVertexWithUV(d4, y + 0 + offsetY, z, d0, d3);
        tessellator.addVertexWithUV(d8, (float)y + f + offsetY, z, d0, d1);
        tessellator.addVertexWithUV(d9, (float)y + f + offsetY, z, d2, d1);
        tessellator.addVertexWithUV(d5, y + 0 + offsetY, z, d2, d3);
        tessellator.addVertexWithUV(d5, y + 0 + offsetY, z + 1, d0, d3);
        tessellator.addVertexWithUV(d9, (float)y + f + offsetY, z + 1, d0, d1);
        d0 = iicon1.getMinU();
        d1 = iicon1.getMinV();
        d2 = iicon1.getMaxU();
        d3 = iicon1.getMaxV();
        tessellator.addVertexWithUV(x + 1, (float)y + f + offsetY, d11, d2, d1);
        tessellator.addVertexWithUV(x + 1, y + 0 + offsetY, d7, d2, d3);
        tessellator.addVertexWithUV(x, y + 0 + offsetY, d7, d0, d3);
        tessellator.addVertexWithUV(x, (float)y + f + offsetY, d11, d0, d1);
        tessellator.addVertexWithUV(x, (float)y + f + offsetY, d10, d2, d1);
        tessellator.addVertexWithUV(x, y + 0 + offsetY, d6, d2, d3);
        tessellator.addVertexWithUV(x + 1, y + 0 + offsetY, d6, d0, d3);
        tessellator.addVertexWithUV(x + 1, (float)y + f + offsetY, d10, d0, d1);
        d4 = (double)x + 0.5D - 0.5D;
        d5 = (double)x + 0.5D + 0.5D;
        d6 = (double)z + 0.5D - 0.5D;
        d7 = (double)z + 0.5D + 0.5D;
        d8 = (double)x + 0.5D - 0.4D;
        d9 = (double)x + 0.5D + 0.4D;
        d10 = (double)z + 0.5D - 0.4D;
        d11 = (double)z + 0.5D + 0.4D;
        tessellator.addVertexWithUV(d8, (float)y + f + offsetY, z, d0, d1);
        tessellator.addVertexWithUV(d4, y + 0 + offsetY, z, d0, d3);
        tessellator.addVertexWithUV(d4, y + 0 + offsetY, z + 1, d2, d3);
        tessellator.addVertexWithUV(d8, (float)y + f + offsetY, z + 1, d2, d1);
        tessellator.addVertexWithUV(d9, (float)y + f + offsetY, z + 1, d0, d1);
        tessellator.addVertexWithUV(d5, y + 0 + offsetY, z + 1, d0, d3);
        tessellator.addVertexWithUV(d5, y + 0 + offsetY, z, d2, d3);
        tessellator.addVertexWithUV(d9, (float)y + f + offsetY, z, d2, d1);
        d0 = iicon.getMinU();
        d1 = iicon.getMinV();
        d2 = iicon.getMaxU();
        d3 = iicon.getMaxV();
        tessellator.addVertexWithUV(x, (float)y + f + offsetY, d11, d0, d1);
        tessellator.addVertexWithUV(x, y + 0 + offsetY, d7, d0, d3);
        tessellator.addVertexWithUV(x + 1, y + 0 + offsetY, d7, d2, d3);
        tessellator.addVertexWithUV(x + 1, (float)y + f + offsetY, d11, d2, d1);
        tessellator.addVertexWithUV(x + 1, (float)y + f + offsetY, d10, d0, d1);
        tessellator.addVertexWithUV(x + 1, y + 0 + offsetY, d6, d0, d3);
        tessellator.addVertexWithUV(x, y + 0 + offsetY, d6, d2, d3);
        tessellator.addVertexWithUV(x, (float)y + f + offsetY, d10, d2, d1);
    }

    private boolean isFireRenderedSide(IBlockAccess world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        return !block.isOpaqueCube() && !block.isSideSolid(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite());
    }

    private void renderBlockFireSides(int x, int y, int z, RenderBlocks renderer, float fireHeight) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        IIcon iicon2 = iicon;

        if (renderer.hasOverrideBlockTexture()) {
            iicon2 = renderer.overrideBlockTexture;
        }

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.setBrightness(Blocks.fire.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        double d0 = iicon2.getMinU();
        double d1 = iicon2.getMinV();
        double d2 = iicon2.getMaxU();
        double d3 = iicon2.getMaxV();
        float f = 1.4F;
        double d5;

        float f2 = 0.2F;
        float f1 = 0.0625F;

        if ((x + y + z & 1) == 1) {
            d0 = iicon1.getMinU();
            d1 = iicon1.getMinV();
            d2 = iicon1.getMaxU();
            d3 = iicon1.getMaxV();
        }

        if ((x / 2 + y / 2 + z / 2 & 1) == 1) {
            d5 = d2;
            d2 = d0;
            d0 = d5;
        }

        if (fireHeight > 0 && isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.EAST)) {
            x++;
            tessellator.addVertexWithUV((float) x + f2, (float) y + f + f1, z + 1, d2, d1);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV((float) x + f2, (float) y + f + f1, z, d0, d1);
            tessellator.addVertexWithUV((float) x + f2, (float) y + f + f1, z, d0, d1);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV((float) x + f2, (float) y + f + f1, z + 1, d2, d1);
            x--;
        }

        if (fireHeight > 0 && isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.WEST)) {
            x--;
            tessellator.addVertexWithUV((float) (x + 1) - f2, (float) y + f + f1, z, d0, d1);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV((float) (x + 1) - f2, (float) y + f + f1, z + 1, d2, d1);
            tessellator.addVertexWithUV((float) (x + 1) - f2, (float) y + f + f1, z + 1, d2, d1);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV((float) (x + 1) - f2, (float) y + f + f1, z, d0, d1);
            x++;
        }

        if (fireHeight > 0 && isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.SOUTH)) {
            z++;
            tessellator.addVertexWithUV(x, (float) y + f + f1, (float) z + f2, d2, d1);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z, d2, d3);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV(x + 1, (float) y + f + f1, (float) z + f2, d0, d1);
            tessellator.addVertexWithUV(x + 1, (float) y + f + f1, (float) z + f2, d0, d1);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z, d0, d3);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z, d2, d3);
            tessellator.addVertexWithUV(x, (float) y + f + f1, (float) z + f2, d2, d1);
            z--;
        }

        if (fireHeight > 0 && isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.NORTH)) {
            z--;
            tessellator.addVertexWithUV(x + 1, (float) y + f + f1, (float) (z + 1) - f2, d0, d1);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z + 1, d0, d3);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV(x, (float) y + f + f1, (float) (z + 1) - f2, d2, d1);
            tessellator.addVertexWithUV(x, (float) y + f + f1, (float) (z + 1) - f2, d2, d1);
            tessellator.addVertexWithUV(x, (float) (y) + f1, z + 1, d2, d3);
            tessellator.addVertexWithUV(x + 1, (float) (y) + f1, z + 1, d0, d3);
            tessellator.addVertexWithUV(x + 1, (float) y + f + f1, (float) (z + 1) - f2, d0, d1);
            //z++;
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
