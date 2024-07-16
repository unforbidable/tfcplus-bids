package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Render.RenderBlocksLightCacheSides;
import com.unforbidable.tfc.bids.Render.RenderIconProvider;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderCarving implements ISimpleBlockRenderingHandler {

    // RenderBlocksLightCacheSides is almost the same as
    // the original RenderBlocksLightCache from TFC
    // The only difference is that instead of rendering one icon
    // the carved block icon for the corresponding side
    // is rendered
    private static RenderBlocksLightCacheSides lightCacheRenderer;

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);

        if (lightCacheRenderer == null)
            lightCacheRenderer = new RenderBlocksLightCacheSides(renderer);
        else
            lightCacheRenderer.update(renderer);

        // Capture full block lighting data...
        lightCacheRenderer.disableRender();
        lightCacheRenderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        lightCacheRenderer.renderStandardBlock(block, x, y, z);
        lightCacheRenderer.enableRender();

        int blockId = te.getCarvedBlockId();
        int meta = te.getCarvedBlockMetadata();

        if (blockId == 0) {
            // We get here when rendering happens
            // before the client knows which block is being carved
            // This means the block won't render until an update
            // unless we let the client know some other way
            return true;
        }

        RenderIconProvider icons = new RenderIconProvider(Block.getBlockById(blockId), meta);

        for (int quadX = 0; quadX < 2; quadX++) {
            for (int quadY = 0; quadY < 2; quadY++) {
                for (int quadZ = 0; quadZ < 2; quadZ++) {
                    if (!te.isQuadUncarved(quadX, quadY, quadZ)) {
                        renderMiniBlock(block, x, y, z, quadX, quadY, quadZ, te, icons);
                    } else {
                        float minX = 0.5f * quadX;
                        float maxX = minX + 0.5f;
                        float minY = 0.5f * quadY;
                        float maxY = minY + 0.5f;
                        float minZ = 0.5f * quadZ;
                        float maxZ = minZ + 0.5f;

                        lightCacheRenderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
                        lightCacheRenderer.renderCachedBlock(block, x, y, z, icons);
                    }
                }
            }
        }
        lightCacheRenderer.clearOverrideBlockTexture();
        return true;
    }

    private static void renderMiniBlock(Block block, int x, int y, int z, int quadX, int quadY, int quadZ,
            TileEntityCarving te, RenderIconProvider icons) {

        int half = TileEntityCarving.CARVING_DIMENSION / 2;
        float stride = 0.5f / half;

        for (int bitX = quadX * half; bitX < half + quadX * half; bitX++) {
            for (int bitY = quadY * half; bitY < half + quadY * half; bitY++) {
                for (int bitZ = quadZ * half; bitZ < half + quadZ * half; bitZ++) {
                    boolean subExists = isOpaque(te, bitX, bitY, bitZ);
                    if (subExists) {
                        boolean isVisible = isTransparent(te, bitX - 1, bitY, bitZ) ||
                                isTransparent(te, bitX + 1, bitY, bitZ) ||
                                isTransparent(te, bitX, bitY - 1, bitZ) ||
                                isTransparent(te, bitX, bitY + 1, bitZ) ||
                                isTransparent(te, bitX, bitY, bitZ - 1) ||
                                isTransparent(te, bitX, bitY, bitZ + 1);

                        if (isVisible) {
                            float minX = stride * bitX;
                            float maxX = minX + stride;
                            float minY = stride * bitY;
                            float maxY = minY + stride;
                            float minZ = stride * bitZ;
                            float maxZ = minZ + stride;

                            lightCacheRenderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
                            lightCacheRenderer.renderCachedBlock(block, x, y, z, icons);
                        }
                    }
                }
            }
        }
    }

    public static boolean isOpaque(TileEntityCarving te, int bitX, int bitY, int bitZ) {
        return !te.isBitCarved(bitX, bitY, bitZ);
    }

    public static boolean isTransparent(TileEntityCarving te, int bitX, int bitY, int bitZ) {
        int dimension = TileEntityCarving.CARVING_DIMENSION;
        if (bitX < 0 || bitX >= dimension || bitY < 0 || bitY >= dimension || bitZ < 0 || bitZ >= dimension)
            return true;

        return te.isBitCarved(bitX, bitY, bitZ);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
