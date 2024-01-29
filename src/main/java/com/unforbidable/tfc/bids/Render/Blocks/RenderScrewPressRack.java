package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Blocks.BlockScrewPressRackPart;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderScrewPressRack implements ISimpleBlockRenderingHandler
{
	static boolean enableBrightness = false;

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        BlockScrewPressRackPart part = (BlockScrewPressRackPart) block;

        for (AxisAlignedBB bb : part.getInventoryBlockBounds()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            renderInvBlock(block, renderer);
        }
    }

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;

        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        BlockScrewPressRackPart part = (BlockScrewPressRackPart) block;

        for (AxisAlignedBB bb : part.getIndividualBoundsForOrientation(orientation)) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            renderer.renderStandardBlock(block, x, y, z);
        }

        renderer.renderAllFaces = false;

        return true;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}


	public static void renderInvBlock(Block block, RenderBlocks renderer)
	{
		Tessellator var14 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		int brightness = 100 + (int) ((TFC_Time.getTotalTicks() % 50 > 25) ? (50 - (TFC_Time.getTotalTicks() % 50)) : TFC_Time.getTotalTicks() % 50);
		//System.out.println(brightness);
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 2));
		var14.draw();
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, 2));
		var14.draw();
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, 1));
		var14.draw();
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, 3));
		var14.draw();
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, 0));
		var14.draw();
		var14.startDrawingQuads();
		if (enableBrightness)
		{
			var14.setBrightness(brightness);
		}
		var14.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, 0));
		var14.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

	}

}
