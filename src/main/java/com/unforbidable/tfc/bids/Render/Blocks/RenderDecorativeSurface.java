package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Core.Common.Metadata.DecorativeSurfaceMetadata;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderDecorativeSurface implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            DecorativeSurfaceMetadata meta = DecorativeSurfaceMetadata.at(world, x, y, z);
            if (meta.isHorizontal()) {
                RenderBlocksWithRotation renderBlocksWithRotation = new RenderBlocksWithRotation(renderer);
                renderBlocksWithRotation.setRenderAllFaces(true);

                switch (meta.getHorizontalOrientation()) {
                    case 0:
                        renderBlocksWithRotation.uvRotateTop = 0;
                        break;

                    case 1:
                        renderBlocksWithRotation.uvRotateTop = 1;
                        break;

                    case 2:
                        renderBlocksWithRotation.uvRotateTop = 3;
                        break;

                    case 3:
                        renderBlocksWithRotation.uvRotateTop = 2;
                        break;
                }

                renderBlocksWithRotation.setRenderBounds(0, 0, 0, 1, 0.01, 1);
                renderBlocksWithRotation.renderStandardBlock(block, x, y, z);
            } else {
                switch (meta.getVerticalFace()) {
                    case NORTH:
                        renderer.setRenderBounds(0, 0, 0.99, 1, 1, 1);
                        break;

                    case SOUTH:
                        renderer.setRenderBounds(0, 0, 0, 1, 1, 0.01);
                        break;

                    case WEST:
                        renderer.setRenderBounds(0.99, 0, 0, 1, 1, 1);
                        break;

                    case EAST:
                        renderer.setRenderBounds(0, 0, 0, 0.01, 1, 1);
                        break;
                }

                renderer.renderStandardBlock(block, x, y, z);
            }
        }

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
