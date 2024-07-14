package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.TileEntities.TEOre;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.StoneCracker;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderCrackedOre implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            IIcon rockTexture = getRockTexture(Minecraft.getMinecraft().theWorld, x, y, z);

			// render the background rock
            renderer.overrideBlockTexture = rockTexture;
			renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();

			// render the ore overlay
			renderer.renderStandardBlock(block, x, y, z);

            // render cracks
            renderer.overrideBlockTexture = StoneCracker.getCrackedStoneIcon(world, x, y, z);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();
		}

		return true;
	}

	public static IIcon getRockTexture(World worldObj, int xCoord, int yCoord, int zCoord) {
		TEOre te = (TEOre)worldObj.getTileEntity(xCoord, yCoord, zCoord);
		if(te!= null && te.baseBlockID > 0) {
			Block block = Block.getBlockById(te.baseBlockID);
            return block.getIcon(5, te.baseBlockMeta);
		}
		return null;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

	public static void renderInvBlock(Block block, int meta, RenderBlocks renderer)
	{
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
