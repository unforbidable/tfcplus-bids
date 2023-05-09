package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Bounds.ClayLampBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayLamp;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderClayLamp implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        ClayLampBounds bounds = ClayLampBounds.getBoundsForOrientation(2);
        renderPartInv(renderer, block, metadata, bounds.getBottomBounds());
        for (int i = 0; i < 4; i++) {
            renderPartInv(renderer, block, metadata, bounds.getSidesBounds()[i]);
        }
        renderPartInv(renderer, block, metadata, bounds.getSpoutBounds());

        renderPartInv(renderer, Blocks.wool, 13, bounds.getFuelsBounds()[1]);

        for (int i = 0; i < 2; i++) {
            renderPartInv(renderer, Blocks.wool, 0, bounds.getWicksBounds()[i]);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;

        TileEntityClayLamp te = (TileEntityClayLamp) world.getTileEntity(x, y, z);
        if (!te.isClientDataLoaded()) {
            return false;
        }

        int orientation = world.getBlockMetadata(x, y, z) & 3;
        ClayLampBounds bounds = ClayLampBounds.getBoundsForOrientation(orientation);

        renderPart(renderer, x, y, z, block, bounds.getBottomBounds());
        for (int i = 0; i < 4; i++) {
            renderPart(renderer, x, y, z, block, bounds.getSidesBounds()[i]);
        }

        renderPart(renderer, x, y, z, block, bounds.getSpoutBounds());
        renderWick(renderer, x, y, z, bounds.getWicksBounds());

        if (te.hasFuel()) {
            FluidStack fuel = te.getFuel();
            int fuelLevel = TileEntityClayLamp.getFuelLevelForAmount(fuel.amount) - 1;
            renderFuel(renderer, x, y, z, block, fuel, bounds.getFuelsBounds()[fuelLevel]);
        }

        renderer.renderAllFaces = false;
        return true;
    }

    private void renderFuel(RenderBlocks renderer, int x, int y, int z, Block block, FluidStack fluid, AxisAlignedBB bounds) {
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

    private void renderWick(RenderBlocks renderer, int x, int y, int z,
                            final AxisAlignedBB[] wicksBounds) {
        int origMetaData = Minecraft.getMinecraft().theWorld.getBlockMetadata(x, y, z);
        Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, 0, 0);

        for (int i = 0; i < 2; i++) {
            AxisAlignedBB bounds = wicksBounds[i];
            renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
            renderer.renderStandardBlock(Blocks.wool, x, y, z);
        }

        Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, origMetaData, 0);
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
