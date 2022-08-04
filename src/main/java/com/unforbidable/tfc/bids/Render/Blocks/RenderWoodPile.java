package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
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

            final float scaleBottom = provider.getWoodPileIconScale(item, 0, isRowRotated);
            final float scaleTop = provider.getWoodPileIconScale(item, 1, isRowRotated);
            final float scaleNorth = provider.getWoodPileIconScale(item, 2, isRowRotated);
            final float scaleSouth = provider.getWoodPileIconScale(item, 3, isRowRotated);
            final float scaleWest = provider.getWoodPileIconScale(item, 4, isRowRotated);
            final float scaleEast = provider.getWoodPileIconScale(item, 5, isRowRotated);

            rendererAlt.setOverrideBlockTexture_y(provider.getWoodPileIcon(item, 0, isRowRotated));
            rendererAlt.setOverrideBlockTexture_Y(provider.getWoodPileIcon(item, 1, isRowRotated));
            rendererAlt.setOverrideBlockTexture_z(provider.getWoodPileIcon(item, 2, isRowRotated));
            rendererAlt.setOverrideBlockTexture_Z(provider.getWoodPileIcon(item, 3, isRowRotated));
            rendererAlt.setOverrideBlockTexture_x(provider.getWoodPileIcon(item, 4, isRowRotated));
            rendererAlt.setOverrideBlockTexture_X(provider.getWoodPileIcon(item, 5, isRowRotated));

            rendererAlt.uvRotateBottom = provider.getWoodPileIconRotation(item, 0, isRowRotated);
            rendererAlt.uvRotateTop = provider.getWoodPileIconRotation(item, 1, isRowRotated);
            rendererAlt.uvRotateNorth = provider.getWoodPileIconRotation(item, 2, isRowRotated);
            rendererAlt.uvRotateSouth = provider.getWoodPileIconRotation(item, 3, isRowRotated);
            rendererAlt.uvRotateWest = provider.getWoodPileIconRotation(item, 4, isRowRotated);
            rendererAlt.uvRotateEast = provider.getWoodPileIconRotation(item, 5, isRowRotated);

            rendererAlt.textureStartXT = 0;
            rendererAlt.textureEndXT = scaleTop;
            rendererAlt.textureStartZT = 0f;
            rendererAlt.textureEndZT = scaleTop;

            rendererAlt.textureStartXB = 0;
            rendererAlt.textureEndXB = scaleBottom;
            rendererAlt.textureStartZB = 0f;
            rendererAlt.textureEndZB = scaleBottom;

            rendererAlt.textureStartXN = 0;
            rendererAlt.textureEndXN = scaleNorth;
            rendererAlt.textureStartYN = 0;
            rendererAlt.textureEndYN = scaleNorth;

            rendererAlt.textureStartXS = 0;
            rendererAlt.textureEndXS = scaleSouth;
            rendererAlt.textureStartYS = 0;
            rendererAlt.textureEndYS = scaleSouth;

            rendererAlt.textureStartYW = 0;
            rendererAlt.textureEndYW = scaleWest;
            rendererAlt.textureStartZW = 0f;
            rendererAlt.textureEndZW = scaleWest;

            rendererAlt.textureStartYE = 0;
            rendererAlt.textureEndYE = scaleEast;
            rendererAlt.textureStartZE = 0f;
            rendererAlt.textureEndZE = scaleEast;

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
