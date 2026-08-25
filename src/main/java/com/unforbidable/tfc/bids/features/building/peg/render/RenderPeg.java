package com.unforbidable.tfc.bids.features.building.peg.render;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderPeg implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        RenderBlocksWithRotation rendererWithRotation = new RenderBlocksWithRotation(renderer);
        rendererWithRotation.renderAllFaces = true;

        rendererWithRotation.setRenderBounds(0.45, 0, 0.45, 0.55, 0.8, 0.55);
        rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);

        rendererWithRotation.renderAllFaces = false;

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
