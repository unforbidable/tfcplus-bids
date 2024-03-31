package com.unforbidable.tfc.bids.Render.Blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderStrawNest implements ISimpleBlockRenderingHandler {
    private static final float MIN = 0.2F;
    private static final float MAX = 0.8F;

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;

        renderer.setRenderBounds(MIN + 0.05F, 0 + 0.15f, MIN + 0.05F, MAX - 0.05F, 0.025F + 0.15f, MAX - 0.05F);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(MIN, 0.1f, MIN + 0.05F, MIN + 0.05F, 0.3f, MAX - 0.05F);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(MAX - 0.05F, 0.1f, MIN + 0.05F, MAX, 0.3f, MAX - 0.05F);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(MIN, 0.1f, MIN, MAX, 0.3f, MIN + 0.05F);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(MIN, 0.1f, MAX - 0.05F, MAX, 0.3f, MAX);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(MIN + 0.05f, 0, MIN + 0.05f, MAX - 0.05f, 0.15f, MAX - 0.05f);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
        renderer.setRenderBounds(MIN + 0.05F, 0, MIN + 0.05F, MAX - 0.05F, 0.05F, MAX - 0.05F);
        renderInvBlock(block, meta, renderer);

        renderer.setRenderBounds(MIN, 0F, MIN + 0.05F, MIN + 0.05F, 0.3F, MAX - 0.05F);
        renderInvBlock(block, meta, renderer);

        renderer.setRenderBounds(MAX - 0.05F, 0F, MIN + 0.05F, MAX, 0.3F, MAX - 0.05F);
        renderInvBlock(block, meta, renderer);

        renderer.setRenderBounds(MIN, 0F, MIN, MAX, 0.3F, MIN + 0.05F);
        renderInvBlock(block, meta, renderer);

        renderer.setRenderBounds(MIN, 0F, MAX - 0.05F, MAX, 0.3F, MAX);
        renderInvBlock(block, meta, renderer);

        renderer.setRenderBounds(MIN + 0.05F, 0.05, MIN + 0.05F, MAX - 0.05F, 0.1f, MAX - 0.05F);
        renderInvBlock(block, meta, renderer);
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
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
