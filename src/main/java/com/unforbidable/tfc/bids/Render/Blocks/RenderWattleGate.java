package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Blocks.BlockWattleGate;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderWattleGate implements ISimpleBlockRenderingHandler
{

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);
        int dir = BlockWattleGate.getDirection(meta);
        boolean isOpen = BlockWattleGate.isFenceGateOpen(meta);

        float postW = 1f / 16 * 2;

        float minYDoor = 0.0625F;
        float maxYDoor = 0.9375F;
        float minYPost = 0F;
        float maxYPost = 1.0F;
        float minX;
        float minZ;
        float maxX;
        float maxZ;

		if (dir != 3 && dir != 1)
		{
			minX = 0.0F;
			maxX = postW;
			minZ = 0.4375F;
			maxZ = 0.5625F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
			minX = 1 - postW;
			maxX = 1.0F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else
		{
			renderer.uvRotateTop = 1;
			minX = 0.4375F;
			maxX = 0.5625F;
			minZ = 0.0F;
			maxZ = postW;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
			minZ = 1 - postW;
			maxZ = 1.0F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.uvRotateTop = 0;
		}

		if (isOpen)
		{
			if (dir == 2 || dir == 0)
				renderer.uvRotateTop = 1;

			if (dir == 3)
			{
				renderer.setRenderBounds(0.5625D, minYDoor, 0.0D, 1D, maxYDoor, 0.125D);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.5625D, minYDoor, 0.875D, 1D, maxYDoor, 1.0D);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (dir == 1)
			{
				renderer.setRenderBounds(0D, minYDoor, 0.0D, 0.4375D, maxYDoor, 0.125D);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0D, minYDoor, 0.875D, 0.4375D, maxYDoor, 1.0D);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (dir == 0)
			{
				renderer.setRenderBounds(0.0D, minYDoor, 0.5625D, 0.125D, maxYDoor, 1D);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.875D, minYDoor, 0.5625D, 1.0D, maxYDoor, 1D);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (dir == 2)
			{
				renderer.setRenderBounds(0.0D, minYDoor, 0D, 0.125D, maxYDoor, 0.4375D);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.875D, minYDoor, 0D, 1.0D, maxYDoor, 0.4375D);
				renderer.renderStandardBlock(block, x, y, z);
			}
		}
		else if (dir != 3 && dir != 1)
		{
			minX = 0.5F;
			maxX = 0.875F;
			renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
			minX = 0.125F;
			maxX = 0.5F;
			renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else
		{
			renderer.uvRotateTop = 1;
			minZ = 0.5F;
			maxZ = 0.875F;
			renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
			minZ = 0.125F;
			maxZ = 0.5F;
			renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
		}

		renderer.uvRotateTop = 0;
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		int l = 1;
		int i1 = BlockWattleGate.getDirection(l);

		float minYDoor = 0.0625F;
		float maxYDoor = 0.9375F;
		float minYPost = 0F;
		float maxYPost = 1.0F;
		float minX;
		float minZ;
		float maxX;
		float maxZ;

		if (i1 != 3 && i1 != 1)
		{
			minX = 0.0F;
			maxX = 0.125F;
			minZ = 0.4375F;
			maxZ = 0.5625F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderInvBlock2(block, metadata, renderer);
			minX = 0.875F;
			maxX = 1.0F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderInvBlock2(block, metadata, renderer);
		}
		else
		{
			renderer.uvRotateTop = 1;
			minX = 0.4375F;
			maxX = 0.5625F;
			minZ = 0.0F;
			maxZ = 0.125F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderInvBlock2(block, metadata, renderer);
			minZ = 0.875F;
			maxZ = 1.0F;
			renderer.setRenderBounds(minX, minYPost, minZ, maxX, maxYPost, maxZ);
			renderInvBlock2(block, metadata, renderer);
			renderer.uvRotateTop = 0;
		}

		renderer.uvRotateTop = 1;
		minX = 0.4375F;
		maxX = 0.5625F;
		minZ = 0.5F;
		maxZ = 0.875F;
		renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
		renderInvBlock2(block, metadata, renderer);
		minZ = 0.125F;
		maxZ = 0.5F;
		renderer.setRenderBounds(minX, minYDoor, minZ, maxX, maxYDoor, maxZ);
		renderInvBlock2(block, metadata, renderer);

		renderer.uvRotateTop = 0;
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

	public static void renderInvBlock(Block block, int m, RenderBlocks renderer)
	{
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

	public static void renderInvBlock2(Block block, int m, RenderBlocks renderer)
	{
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
