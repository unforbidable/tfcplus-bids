package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Blocks.BlockPalisade;
import com.unforbidable.tfc.bids.Core.Fences.FenceConnections;
import com.unforbidable.tfc.bids.Core.Fences.FenceHelper;
import com.unforbidable.tfc.bids.Render.RenderBlocksWithMeta;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderRoughStoneFence implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 1, 1, 0.5);
        renderInvBlock(block, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        float width = 1f / 3;
        float minX = (1 - width) * 0.5f;
        float minZ = (1 - width) * 0.5f;
        float maxX = (1 + width) * 0.5f;
        float maxZ = (1 + width) * 0.5f;

        BlockPalisade fence = (BlockPalisade) block;
        FenceConnections fc = new FenceConnections(world, x, y, z);

        RenderBlocksWithMeta rendererMeta = new RenderBlocksWithMeta(renderer);
        rendererMeta.renderAllFaces = true;

        if (fc.isFillNorth()) {
            Block north = world.getBlock(x, y, z - 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x, y, z - 1);
            if (fc.isFillWest()) {
                rendererMeta.setRenderBounds(minX, 0, 0, 1, 1, minX);
            } else if (fc.isFillEast()) {
                rendererMeta.setRenderBounds(0, 0, 0, maxX, 1, minX);
            } else {
                rendererMeta.setRenderBounds(0, 0, 0, 1, 1, minX);
            }
            renderFilledBlock(north, x, y, z, rendererMeta);
        }

        if (fc.isFillSouth()) {
            Block south = world.getBlock(x, y, z + 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x, y, z + 1);
            if (fc.isFillWest()) {
                rendererMeta.setRenderBounds(minX, 0, maxZ, 1, 1, 1);
            } else if (fc.isFillEast()) {
                rendererMeta.setRenderBounds(0, 0, maxZ, maxX, 1, 1);
            } else {
                rendererMeta.setRenderBounds(0, 0, maxZ, 1, 1, 1);
            }
            renderFilledBlock(south, x, y, z, rendererMeta);
        }

        if (fc.isFillWest()) {
            Block west = world.getBlock(x - 1, y, z);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z);
            rendererMeta.setRenderBounds(0, 0, 0, minX, 1, 1);
            renderFilledBlock(west, x, y, z, rendererMeta);
        }

        if (fc.isFillEast()) {
            Block east = world.getBlock(x + 1, y, z);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z);
            rendererMeta.setRenderBounds(maxX, 0, 0, 1, 1, 1);
            renderFilledBlock(east, x, y, z, rendererMeta);
        }

        if (fc.isFillCornerNW()) {
            Block nw = world.getBlock(x - 1, y, z - 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z - 1);
            rendererMeta.setRenderBounds(0, 0, 0, minX, 1, minZ);
            renderFilledBlock(nw, x, y, z, rendererMeta);
        }

        if (fc.isFillCornerNE()) {
            Block ne = world.getBlock(x + 1, y, z - 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z - 1);
            rendererMeta.setRenderBounds(maxX, 0, 0, 1, 1, minZ);
            renderFilledBlock(ne, x, y, z, rendererMeta);
        }

        if (fc.isFillCornerSW()) {
            Block sw = world.getBlock(x - 1, y, z + 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z + 1);
            rendererMeta.setRenderBounds(0, 0, maxZ, minX, 1, 1);
            renderFilledBlock(sw, x, y, z, rendererMeta);
        }

        if (fc.isFillCornerSE()) {
            Block se = world.getBlock(x + 1, y, z + 1);
            rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z + 1);
            rendererMeta.setRenderBounds(maxX, 0, maxZ, 1, 1, 1);
            renderFilledBlock(se, x, y, z, rendererMeta);
        }

        RenderBlocksWithRotation rendererWithRotation = new RenderBlocksWithRotation(renderer);
        rendererWithRotation.renderAllFaces = true;
        rendererWithRotation.staticTexture = true;

        float height = 1f;

        rendererWithRotation.textureStartYE = 1f;
        rendererWithRotation.textureStartYW = 1f;
        rendererWithRotation.textureStartYS = 1f;
        rendererWithRotation.textureStartYN = 1f;
        rendererWithRotation.textureEndYE = 0f;
        rendererWithRotation.textureEndYW = 0f;
        rendererWithRotation.textureEndYS = 0f;
        rendererWithRotation.textureEndYN = 0f;

        if (fc.getConnectionCount() == 1 && world.isAirBlock(x, y + 1, z)) {
            height = 0.5f;

            rendererWithRotation.textureEndYE = 0.5f;
            rendererWithRotation.textureEndYW = 0.5f;
            rendererWithRotation.textureEndYS = 0.5f;
            rendererWithRotation.textureEndYN = 0.5f;
        }

        rendererWithRotation.textureStartXS = 1f / 3;
        rendererWithRotation.textureStartXN = 1f / 3;
        rendererWithRotation.textureStartZE = 1f / 3;
        rendererWithRotation.textureStartZW = 1f / 3;
        rendererWithRotation.textureEndXS = 2f / 3;
        rendererWithRotation.textureEndXN = 2f / 3;
        rendererWithRotation.textureEndZE = 2f / 3;
        rendererWithRotation.textureEndZW = 2f / 3;

        rendererWithRotation.textureStartZT = 1f / 3;
        rendererWithRotation.textureStartXT = 1f / 3;
        rendererWithRotation.textureStartZB = 1f / 3;
        rendererWithRotation.textureStartXB = 1f / 3;
        rendererWithRotation.textureEndZT = 2f / 3;
        rendererWithRotation.textureEndXT = 2f / 3;
        rendererWithRotation.textureEndZB = 2f / 3;
        rendererWithRotation.textureEndXB = 2f / 3;

        rendererWithRotation.setRenderBounds(minX, 0, minZ, maxX, height, maxZ);
        rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);


        if (fc.isNorth()) {
            rendererWithRotation.textureStartZE = 2f / 3;
            rendererWithRotation.textureStartZW = 1;
            rendererWithRotation.textureEndZE = 1;
            rendererWithRotation.textureEndZW = 2f / 3;
            rendererWithRotation.textureStartZT = 2f / 3;
            rendererWithRotation.textureStartZB = 2f / 3;
            rendererWithRotation.textureEndZT = 1;
            rendererWithRotation.textureEndZB = 1;

            rendererWithRotation.setRenderBounds(minX, 0, 0, maxX, height, minZ);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        if (fc.isSouth()) {
            rendererWithRotation.textureStartZE = 0;
            rendererWithRotation.textureStartZW = 1f / 3;
            rendererWithRotation.textureEndZE = 1f / 3;
            rendererWithRotation.textureEndZW = 0;
            rendererWithRotation.textureStartZT = 0;
            rendererWithRotation.textureStartZB = 0;
            rendererWithRotation.textureEndZT = 1f / 3;
            rendererWithRotation.textureEndZB = 1f / 3;

            rendererWithRotation.setRenderBounds(minX, 0, maxZ, maxX, height, 1);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        rendererWithRotation.textureStartZT = 1f / 3;
        rendererWithRotation.textureStartZB = 1f / 3;
        rendererWithRotation.textureEndZT = 2f / 3;
        rendererWithRotation.textureEndZB = 2f / 3;

        if (fc.isWest()) {
            rendererWithRotation.textureStartXS = 0;
            rendererWithRotation.textureStartXN = 1f / 3;
            rendererWithRotation.textureEndXS = 1f / 3;
            rendererWithRotation.textureEndXN = 0;
            rendererWithRotation.textureStartXT = 1f / 3;
            rendererWithRotation.textureStartXB = 1f / 3;
            rendererWithRotation.textureEndXT = 0;
            rendererWithRotation.textureEndXB = 0;

            rendererWithRotation.setRenderBounds(0, 0, minZ, minX, height, maxZ);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        if (fc.isEast()) {
            rendererWithRotation.textureStartXS = 2f / 3;
            rendererWithRotation.textureStartXN = 1;
            rendererWithRotation.textureEndXS = 1;
            rendererWithRotation.textureEndXN = 2f / 3;
            rendererWithRotation.textureStartXT = 1;
            rendererWithRotation.textureStartXB = 1;
            rendererWithRotation.textureEndXT = 2f / 3;
            rendererWithRotation.textureEndXB = 2f / 3;

            rendererWithRotation.setRenderBounds(maxX, 0, minZ, 1, height, maxZ);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        return true;
    }

    private static void renderFilledBlock(Block block, int x, int y, int z, RenderBlocksWithMeta renderer) {
        // Grass blocks render as dirt/clay/peat first following grass overlay
        if (block == TFCBlocks.grass || block == TFCBlocks.dryGrass) {
            renderer.renderStandardBlock(TFCBlocks.dirt, x, y, z);
        } else if (block == TFCBlocks.grass2 || block == TFCBlocks.dryGrass2) {
            renderer.renderStandardBlock(TFCBlocks.dirt2, x, y, z);
        } else if (block == TFCBlocks.clayGrass) {
            renderer.renderStandardBlock(TFCBlocks.clay, x, y, z);
        } else if (block == TFCBlocks.clayGrass2) {
            renderer.renderStandardBlock(TFCBlocks.clay2, x, y, z);
        } else if (block == TFCBlocks.peatGrass) {
            renderer.renderStandardBlock(TFCBlocks.peat, x, y, z);
        }

        renderer.renderStandardBlock(block, x, y, z);
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
        Tessellator tes = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.25F);
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, m));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, m));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

}
