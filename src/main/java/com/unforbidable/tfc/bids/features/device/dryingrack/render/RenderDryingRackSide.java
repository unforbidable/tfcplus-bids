package com.unforbidable.tfc.bids.features.device.dryingrack.render;

import com.unforbidable.tfc.bids.features.device.dryingrack.block.BlockDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackSideBounds;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderDryingRackSide implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        int orientation = world.getBlockMetadata(x, y, z);
        DryingRackSideBounds sideBounds = DryingRackSideBounds.fromOrientation(orientation);

        renderer.renderAllFaces = true;

        for (AxisAlignedBB bounds : sideBounds.legs) {
            renderPart(renderer, x, y, z, block, bounds);
        }

        ForgeDirection d = ForgeDirection.getOrientation(orientation * 2 + 2);
        ForgeDirection o = d.getOpposite();
        boolean connectedTwo = world.getBlock(x + d.offsetX, y, z + d.offsetZ) instanceof BlockDryingRack;
        boolean connectedOne = world.getBlock(x + o.offsetX, y, z + o.offsetZ) instanceof BlockDryingRack;

        if (connectedOne && connectedTwo) {
            for (AxisAlignedBB bounds : sideBounds.polesBoth) {
                renderPart(renderer, x, y, z, block, bounds);
            }

            renderPart(renderer, x, y, z, block, sideBounds.armOne);
            renderPart(renderer, x, y, z, block, sideBounds.armTwo);
        } else if (connectedOne) {
            for (AxisAlignedBB bounds : sideBounds.polesOne) {
                renderPart(renderer, x, y, z, block, bounds);
            }

            renderPart(renderer, x, y, z, block, sideBounds.armOne);
        } else if (connectedTwo) {
            for (AxisAlignedBB bounds : sideBounds.polesTwo) {
                renderPart(renderer, x, y, z, block, bounds);
            }

            renderPart(renderer, x, y, z, block, sideBounds.armTwo);
        }

        renderer.renderAllFaces = false;

        return true;
    }

    private void renderPart(RenderBlocks renderer, int x, int y, int z, Block block,
            final AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlock(block, x, y, z);
    }

    private void renderPartWithColorMultiplier(RenderBlocks renderer, int x, int y, int z, Block block,
            final AxisAlignedBB bounds, float color) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, color, color, color);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
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
