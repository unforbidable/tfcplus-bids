package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderProcessingSurface implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;

        if (renderer.overrideBlockTexture != null) {
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y, z);
            if (te.getInputItem() != null) {
                IIcon inputIcon = ProcessingSurfaceHelper.getIconForItem(te.getInputItem());
                IIcon resultIcon = ProcessingSurfaceHelper.getIconForItem(te.getResultItem());

                int n = 0;
                for (int k = 0; k < 4; k++) {
                    for (int i = 0; i < 4; i++) {
                        if (n++ >= te.getWorkCounter()) {
                            renderer.overrideBlockTexture = inputIcon;
                        } else {
                            renderer.overrideBlockTexture = resultIcon;
                        }

                        renderer.setRenderBounds(0.25 * i, 0, 0.25 * k, 0.25 * i + 0.25, 0.001, 0.25 * k + 0.25);
                        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
                    }
                }

                renderer.clearOverrideBlockTexture();
            }
        }

        renderer.renderAllFaces = false;
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
