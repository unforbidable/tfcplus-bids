package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Render.RenderHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.api.BidsOptions;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderNewFirepit implements ISimpleBlockRenderingHandler {

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

        renderer.renderAllFaces = true;

        renderer.setRenderBounds(0, 0, 0, 1, block.getBlockBoundsMaxY(), 1);
        renderer.renderStandardBlock(block, x, y, z);

        TileEntityNewFirepit te = (TileEntityNewFirepit) world.getTileEntity(x, y, z);
        if (te.fireTemp > 100) {
            // Only render fire at all when temp sufficient
            // 100+ represent the temperature when cooking pot at the edge of the fire pit observes ANY temperature
            // Also torches and candles can be lit, and so on
            // The scale of the fire is directly tied to the temp
            float offsetY = (float) block.getBlockBoundsMaxY();
            float scaleY = te.fireTemp / 2000;
            float color = 0.5f + Math.min(te.fireTemp / 1000, 0.5f);
            if (world.getBlock(x, y + 1, z) == TFCBlocks.vessel && BidsOptions.Firepit.replaceFirepitTFC) {
                // When vessel is above
                // but only when TFC fire pit was replaced - when the vessel is rendered just above the fire pit
                // the central fire block is very low
                RenderHelper.renderBlockFire(x, y, z, renderer, offsetY, 0.5f, 0.2f - offsetY, color);
                RenderHelper.renderBlockFireSides(x, y, z, renderer, offsetY, 0.625f, scaleY * 0.5f + 0.1f, color, true);
            } else {
                RenderHelper.renderBlockFire(x, y, z, renderer, offsetY, 0.5f, scaleY, color);
                if (te.fireTemp > 500) {
                    // Otherwise the sides are low
                    // and only rendered when the temp is high enough
                    // 500+ represent the temperature when cooking pot at the edge of the fire pit observes MED temperature
                    RenderHelper.renderBlockFireSides(x, y, z, renderer, offsetY, 0.5f, scaleY * 0.5f, color, true);
                }
            }
        }

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
