package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Render.RenderBlocksWithMeta;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySoakingSurface;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderSoakingSurface implements ISimpleBlockRenderingHandler {

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);

            return true;
        }

        RenderBlocksWithMeta rendererMeta = new RenderBlocksWithMeta(renderer);
        rendererMeta.renderAllFaces = true;
        rendererMeta.setRenderBounds(0, 0, 0, 1, 1, 1);

        TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
        if (!te.isClientDataLoaded()) {
            return false;
        }

        Block originalBlock = Block.getBlockById(te.getOriginalBlockId());
        rendererMeta.overrideMetadata = te.getOriginalBlockMetadata();
        renderOriginalBlock(originalBlock, x, y, z, rendererMeta);

        rendererMeta.renderAllFaces = false;

        return true;
    }

    private static void renderOriginalBlock(Block block, int x, int y, int z, RenderBlocksWithMeta renderer) {
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
    public int getRenderId() {
        return 0;
    }

}
