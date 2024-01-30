package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressBarrel;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderScrewPressBarrel implements ISimpleBlockRenderingHandler {

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        Block blockBottom = TFCBlocks.woodSupportH;
        Block blockSides = TFCBlocks.planks;

        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasinBottom()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderInvBlock(blockBottom, 0, renderer);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasinSides()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderInvBlock(blockSides, 0, renderer);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketBottom()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 1);
            renderInvBlock(blockBottom, 0, renderer);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketSides()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 1);
            renderInvBlock(blockSides, 0, renderer);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketHoops()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderInvBlockHoop(block, 0, renderer);
        }

        rotate(renderer, 0);
    }

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Block blockBottom = TFCBlocks.woodSupportH;
        Block blockSides = TFCBlocks.planks;

        renderer.renderAllFaces = true;

        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasinBottom()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderer.renderStandardBlock(blockBottom, x, y, z);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasinSides()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderer.renderStandardBlock(blockSides, x, y, z);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketBottom()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 1);
            renderer.renderStandardBlock(blockBottom, x, y, z);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketSides()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 1);
            renderer.renderStandardBlock(blockSides, x, y, z);
        }
        for (AxisAlignedBB bb : ScrewPressBounds.getBounds().getBasketHoops()) {
            renderer.setRenderBounds(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            rotate(renderer, 0);
            renderer.renderStandardBlock(block, x, y, z);
        }

        TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) world.getTileEntity(x, y, z);
        FluidStack fluidStack = teBarrel.getOutputFluid();
        if (fluidStack != null) {
            float fullnessRatio = (float) fluidStack.amount / (float) teBarrel.getMaxOutputFluidAmount();
            AxisAlignedBB fluidBounds = ScrewPressBounds.getBounds().getBasinFluid().copy();
            fluidBounds.maxY = fluidBounds.minY + (fluidBounds.maxY - fluidBounds.minY) * fullnessRatio;
            renderFluid(renderer, x, y, z, block, fluidStack, fluidBounds);
        }

        rotate(renderer, 0);
        renderer.renderAllFaces = false;

        return true;
	}

	@Override
	public int getRenderId() {
		return 0;
	}

    public static void rotate(RenderBlocks renderer, int i) {
        renderer.uvRotateEast = i;
        renderer.uvRotateWest = i;
        renderer.uvRotateNorth = i;
        renderer.uvRotateSouth = i;
    }

    private static void renderFluid(RenderBlocks renderer, int x, int y, int z, Block block, FluidStack fluid, AxisAlignedBB bounds) {
        int color = fluid.getFluid().getColor(fluid);
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        IIcon still = fluid.getFluid().getStillIcon();
        renderer.setOverrideBlockTexture(still);

        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);

        renderer.clearOverrideBlockTexture();
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

    public static void renderInvBlockHoop(Block block, int m, RenderBlocks renderer)
    {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var14.startDrawingQuads();
        var14.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(10, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(11, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(12, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(13, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(14, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(15, m));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

}
