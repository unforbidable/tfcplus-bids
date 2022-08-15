package com.unforbidable.tfc.bids.Render.Blocks;

import org.lwjgl.opengl.GL11;

import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackBounds;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager.TyingEquipment;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class RenderDryingRack implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        final DryingRackBounds rackBounds = DryingRackBounds.fromOrientation(1);

        for (AxisAlignedBB bounds : rackBounds.poles) {
            renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
            renderInvBlock(block, metadata, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        final int orientation = world.getBlockMetadata(x, y, z);
        final DryingRackBounds rackBounds = DryingRackBounds.fromOrientation(orientation);
        final TileEntityDryingRack dryingRack = (TileEntityDryingRack) world.getTileEntity(x, y, z);

        renderer.renderAllFaces = true;

        for (AxisAlignedBB bounds : rackBounds.poles) {
            renderPart(renderer, x, y, z, block, bounds);
        }

        for (int i = 0; i < rackBounds.knots.length; i++) {
            DryingRackItem item = dryingRack.getItem(i);

            if (item != null && item.tyingItem != null) {
                Block tyingEquipmentBlock = Blocks.wool;
                int tyingEquipmentBlockMetadata = 0;

                // Use block for rendering the knot
                // according to tying equipment specification
                final TyingEquipment tyingEquipment = DryingManager.findTyingEquipmnt(item.tyingItem);
                if (tyingEquipment != null && tyingEquipment.renderBlock != null) {
                    tyingEquipmentBlock = tyingEquipment.renderBlock;
                    tyingEquipmentBlockMetadata = tyingEquipment.renderBlockMetadata;
                }

                Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, tyingEquipmentBlockMetadata, 0);

                if (item.tyingItemUsedUp) {
                    float color = 0.5f;
                    renderPartWithColorMultiplier(renderer, x, y, z, tyingEquipmentBlock, rackBounds.knots[i], color);
                    renderPartWithColorMultiplier(renderer, x, y, z, tyingEquipmentBlock, rackBounds.strings[i], color);
                } else {
                    renderPart(renderer, x, y, z, tyingEquipmentBlock, rackBounds.knots[i]);
                    renderPart(renderer, x, y, z, tyingEquipmentBlock, rackBounds.strings[i]);
                }

                Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, orientation, 0);
            }
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
