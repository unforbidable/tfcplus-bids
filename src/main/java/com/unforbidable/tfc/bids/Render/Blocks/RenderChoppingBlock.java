package com.unforbidable.tfc.bids.Render.Blocks;

import org.lwjgl.opengl.GL11;

import com.unforbidable.tfc.bids.Blocks.BlockChoppingBlock;
import com.unforbidable.tfc.bids.Core.ChoppingBlock.ChoppingBlockHelper;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderChoppingBlock implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 1, ChoppingBlockHelper.getChoppingBlockBoundsHeight(), 1);
        renderInvBlock(((BlockChoppingBlock) block).materialBlock, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        final BlockChoppingBlock choppingBlock = (BlockChoppingBlock) block;

        renderer.renderAllFaces = true;

        renderer.setRenderBounds(0, 0, 0, 1, ChoppingBlockHelper.getChoppingBlockBoundsHeight(), 1);
        renderer.renderStandardBlock(choppingBlock.materialBlock, x, y, z);

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public static void renderInvBlock(Block block, int m, RenderBlocks renderer) {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var14.startDrawingQuads();
        var14.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

}
