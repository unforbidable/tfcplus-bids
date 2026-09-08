package com.unforbidable.tfc.bids.features.device.cookingpit.render;

import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderHideCookingPit implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntityHideCookingPit te = (TileEntityHideCookingPit) world.getTileEntity(x, y, z);
        if (!te.isClientDataLoaded()) {
            return false;
        }

        renderer.renderAllFaces = true;

        renderPart(renderer, x, y, z, block, te.getCachedBounds().getBottomBounds());
        for (int i = 0; i < te.getCachedBounds().getSidesBounds().length; i++) {
            renderPart(renderer, x, y, z, block, te.getCachedBounds().getSidesBounds()[i]);
        }

        if (te.hasFluid()) {
            FluidStack fluid = te.getFluidStack();
            float fullnessRatio = (float)te.getTotalLiquidVolume() / (float)te.getMaxFluidVolume();
            AxisAlignedBB contentBounds = te.getCachedBounds().getFluidBounds().copy();
            contentBounds.maxY = contentBounds.minY + te.getCachedBounds().getMaxFluidHeight() * fullnessRatio;
            renderFluid(renderer, x, y, z, block, fluid, contentBounds);
        }

        renderer.renderAllFaces = false;
        return true;
    }

    private void renderFluid(RenderBlocks renderer, int x, int y, int z, Block block, FluidStack fluid, AxisAlignedBB bounds) {
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

    private void renderPart(RenderBlocks renderer, int x, int y, int z, Block block,
                            final AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlock(block, x, y, z);
    }

    private void renderPartInv(RenderBlocks renderer, Block block, int meta,
                            final AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
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

    public static void renderInvBlock(Block block, int m, RenderBlocks renderer)
    {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
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
        GL11.glTranslatef(0.5F, 0.4F, 0.5F);
    }

}
