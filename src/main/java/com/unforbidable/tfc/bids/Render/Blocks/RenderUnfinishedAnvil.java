package com.unforbidable.tfc.bids.Render.Blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderUnfinishedAnvil implements ISimpleBlockRenderingHandler {

    private final int stage;

    public RenderUnfinishedAnvil(int stage) {
        this.stage = stage;
    }

    public static boolean renderAnvil(Block block, int i, int j, int k, RenderBlocks renderblocks) {
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        // top
        if (stage > 3) {
            renderer.setRenderBounds(0.3F, 0.4F, 0.1F, 0.7F, 0.5F, 0.9F);
            renderInvBlock(block, metadata, renderer);
        }

        // core 2
        if (stage > 2) {
            renderer.setRenderBounds(0.35F, 0.3F, 0.15F, 0.65F, 0.4F, 0.85F);
            renderInvBlock(block, metadata, renderer);
        }

        // core 1
        if (stage > 1) {
            renderer.setRenderBounds(0.35F, 0.2F, 0.15F, 0.65F, 0.3F, 0.85F);
            renderInvBlock(block, metadata, renderer);
        }

        // feet 2
        if (stage > 0) {
            renderer.setRenderBounds(0.25F, 0.1F, 0.1F, 0.75F, 0.2F, 0.90F);
            renderInvBlock(block, metadata, renderer);
        }

        // feet
        renderer.setRenderBounds(0.20F, 0.0F, 0.0F, 0.80F, 0.1F, 1.0F);
        renderInvBlock(block, metadata, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return renderAnvil(block,x,y,z,renderer);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return 0;
	}

	public static void renderInvBlock(Block block, int meta, RenderBlocks renderer) {
		Tessellator var14 = Tessellator.instance;
		var14.startDrawingQuads();
		var14.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
		var14.draw();
	}
}
