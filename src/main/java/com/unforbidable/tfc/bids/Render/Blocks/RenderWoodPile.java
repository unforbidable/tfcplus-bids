package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
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

        int i = 0;
        int row = 0;
        int rowLargeItemCount = 0;

        final int woodPileOrientation = Minecraft.getMinecraft().theWorld.getBlockMetadata(x, y, z) & 7;

        for (ItemStack item : te.getItems(false)) {
            if (row > 3) {
                Bids.LOG.warn("There is more wood pile items to render, but no room left in the block");
                break;
            }

            final IWoodPileRenderProvider provider = WoodPileRegistry.findItem(item.getItem());

            final int itemsPerRow = provider.isWoodPileLargeItem(item) ? 2 : 4;
            final double stride = 1f / itemsPerRow;

            final boolean isRowRotated = (row % 2) != (woodPileOrientation % 2);
            final int rowOrientation = (woodPileOrientation + (row % 2)) % 4;

            final float scaleBottom = provider.getWoodPileIconScale(item, 0, isRowRotated);
            final float scaleTop = provider.getWoodPileIconScale(item, 1, isRowRotated);
            final float scaleNorth = provider.getWoodPileIconScale(item, 2, isRowRotated);
            final float scaleSouth = provider.getWoodPileIconScale(item, 3, isRowRotated);
            final float scaleWest = provider.getWoodPileIconScale(item, 4, isRowRotated);
            final float scaleEast = provider.getWoodPileIconScale(item, 5, isRowRotated);

            // final float scaleX = isRowRotated ? 1f : scale;
            // final float scaleY = scale;
            // final float scaleZ = isRowRotated ? scale : 1f;

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

            switch (rowOrientation) {
                case 0:
                    rendererAlt.setRenderBounds((3 - i) * stride, row * stride, 0,
                            (4 - i) * stride, (row + 1) * stride, 1);
                    break;

                case 1:
                    rendererAlt.setRenderBounds(0, row * stride, (3 - i) * stride,
                            1, (row + 1) * stride, (4 - i) * stride);
                    break;

                case 2:
                    rendererAlt.setRenderBounds(i * stride, row * stride, 0,
                            (i + 1) * stride, (row + 1) * stride, 1);
                    break;

                case 3:
                    rendererAlt.setRenderBounds(0, row * stride, i * stride,
                            1, (row + 1) * stride, (i + 1) * stride);
                    break;
            }

            rendererAlt.renderStandardBlock(block, x, y, z);

            i++;

            if (provider.isWoodPileLargeItem(item)) {
                rowLargeItemCount++;

                // Skip one if large item rendered
                i++;
            }

            if (i > 3) {
                row++;
                i = 0;
                rowLargeItemCount = 0;

                if (rowLargeItemCount == 1) {
                    // If one large item then we continue next to it
                    i = 2;
                } else if (rowLargeItemCount == 2) {
                    // If two large items then we continue above
                    row++;
                }
            }
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
