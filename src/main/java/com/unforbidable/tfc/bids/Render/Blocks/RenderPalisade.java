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

public class RenderPalisade implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 1, 1, 0.5);
        renderInvBlock(block, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        float width = 1f / 3;
        float minX = (1 - width) * 0.5f;
        float minZ = (1 - width) * 0.5f;
        float maxX = (1 + width) * 0.5f;
        float maxZ = (1 + width) * 0.5f;

        BlockPalisade fence = (BlockPalisade) block;
        FenceConnections fc = new FenceConnections(world, x, y, z);

        RenderBlocksWithMeta rendererMeta = new RenderBlocksWithMeta(renderer);
        rendererMeta.renderAllFaces = true;

        if (fc.isNorth() && fc.isSouth()) {
            if (!fc.isWest()) {
                Block west = world.getBlock(x - 1, y, z);
                if (fence.canFenceFillWithBlock(west)) {
                    rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z);
                    rendererMeta.setRenderBounds(0, 0, 0, minX, 1, 1);
                    renderFilledBlock(west, x, y, z, rendererMeta);
                }
            }
            if (!fc.isEast()) {
                Block east = world.getBlock(x + 1, y, z);
                if (fence.canFenceFillWithBlock(east)) {
                    rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z);
                    rendererMeta.setRenderBounds(maxX, 0, 0, 1, 1, 1);
                    renderFilledBlock(east, x, y, z, rendererMeta);
                }
            }
        }

        if (fc.isWest() && fc.isEast()) {
            if (!fc.isNorth()) {
                Block north = world.getBlock(x, y, z - 1);
                if (fence.canFenceFillWithBlock(north)) {
                    rendererMeta.overrideMetadata = world.getBlockMetadata(x, y, z - 1);
                    rendererMeta.setRenderBounds(0, 0, 0, 1, 1, minX);
                    renderFilledBlock(north, x, y, z, rendererMeta);
                }
            }
            if (!fc.isSouth()) {
                Block south = world.getBlock(x, y, z + 1);
                if (fence.canFenceFillWithBlock(south)) {
                    rendererMeta.overrideMetadata = world.getBlockMetadata(x, y, z + 1);
                    rendererMeta.setRenderBounds(0, 0, maxZ, 1, 1, 1);
                    renderFilledBlock(south, x, y, z, rendererMeta);
                }
            }
        }

        if (fc.isNorth() && fc.isWest()) {
            Block nw = world.getBlock(x - 1, y, z - 1);
            if (fence.canFenceFillWithBlock(nw)) {
                rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z - 1);
                rendererMeta.setRenderBounds(0, 0, 0, minX, 1, minZ);
                renderFilledBlock(nw, x, y, z, rendererMeta);
            }
        }

        if (fc.isNorth() && fc.isEast()) {
            Block ne = world.getBlock(x + 1, y, z - 1);
            if (fence.canFenceFillWithBlock(ne)) {
                rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z - 1);
                rendererMeta.setRenderBounds(maxX, 0, 0, 1, 1, minZ);
                renderFilledBlock(ne, x, y, z, rendererMeta);
            }
        }

        if (fc.isSouth() && fc.isWest()) {
            Block sw = world.getBlock(x - 1, y, z + 1);
            if (fence.canFenceFillWithBlock(sw)) {
                rendererMeta.overrideMetadata = world.getBlockMetadata(x - 1, y, z + 1);
                rendererMeta.setRenderBounds(0, 0, maxZ, minX, 1, 1);
                renderFilledBlock(sw, x, y, z, rendererMeta);
            }
        }

        if (fc.isSouth() && fc.isEast()) {
            Block sw = world.getBlock(x + 1, y, z + 1);
            if (fence.canFenceFillWithBlock(sw)) {
                rendererMeta.overrideMetadata = world.getBlockMetadata(x + 1, y, z + 1);
                rendererMeta.setRenderBounds(maxX, 0, maxZ, 1, 1, 1);
                renderFilledBlock(sw, x, y, z, rendererMeta);
            }
        }

        RenderBlocksWithRotation rendererWithRotation = new RenderBlocksWithRotation(renderer);
        rendererWithRotation.renderAllFaces = true;
        rendererWithRotation.staticTexture = true;
        rendererWithRotation.textureEndXS = 0.5f;
        rendererWithRotation.textureEndXN = 0.5f;
        rendererWithRotation.textureEndZE = 0.5f;
        rendererWithRotation.textureEndZW = 0.5f;
        rendererWithRotation.textureEndXT = 0.5f;
        rendererWithRotation.textureEndZT = 0.5f;
        rendererWithRotation.textureEndXB = 0.5f;
        rendererWithRotation.textureEndZB = 0.5f;

        float height = 1f;
        float heightNorth = 1f;
        float heightSouth = 1f;
        float heightWest = 1f;
        float heightEast = 1f;

        Block blockAbove = world.getBlock(x, y + 1, z);
        if (!FenceHelper.isFenceBlock(blockAbove) && !blockAbove.isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN) && !fc.canFenceFill()) {
            if (world.getBlock(x, y, z - 1) instanceof BlockPalisade) {
                heightNorth = getAdjustedHeightAt(world, x, y, z, 1);
            }
            if (world.getBlock(x, y, z + 1) instanceof BlockPalisade) {
                heightSouth = getAdjustedHeightAt(world, x, y, z, 2);
            }
            if (world.getBlock(x - 1, y, z) instanceof BlockPalisade) {
                heightWest = getAdjustedHeightAt(world, x, y, z, 3);
            }
            if (world.getBlock(x + 1, y, z) instanceof BlockPalisade) {
                heightEast = getAdjustedHeightAt(world, x, y, z, 4);
            }
        }

        if (fc.countConnections() == 1 && world.isAirBlock(x, y + 1, z)) {
            height = 0.5f;
        }

        rendererWithRotation.setRenderBounds(minX, 0, minZ, maxX, height, maxZ);
        rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);

        if (fc.isNorth()) {
            rendererWithRotation.setRenderBounds(minX, 0, 0, maxX, heightNorth, minZ);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        if (fc.isSouth()) {
            rendererWithRotation.setRenderBounds(minX, 0, maxZ, maxX, heightSouth, 1);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        if (fc.isWest()) {
            rendererWithRotation.setRenderBounds(0, 0, minZ, minX, heightWest, maxZ);
            rendererWithRotation.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
        }

        if (fc.isEast()) {
            rendererWithRotation.setRenderBounds(maxX, 0, minZ, 1, heightEast, maxZ);
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

    private float getAdjustedHeightAt(IBlockAccess world, int x, int y, int z, int segment) {
        long seed = (x * x * 4987142L) + (x * 5947611L) + (z * z * 4392871L + (z * 389711L) ^ segment);
        Random rand = new Random(seed);
        int step = rand.nextInt(2) + 1;
        return 1 - step * 1f / 16;
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
