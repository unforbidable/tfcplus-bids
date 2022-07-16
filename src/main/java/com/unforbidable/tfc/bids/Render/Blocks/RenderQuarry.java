package com.unforbidable.tfc.bids.Render.Blocks;

import java.util.Map;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryWedgeBounds;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryWedgeModeller;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderQuarry implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {

        TileEntityQuarry quarry = (TileEntityQuarry) world.getTileEntity(x, y, z);
        ForgeDirection front = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z) & 7);
        ForgeDirection back = front.getOpposite();
        Block materialBlock = TFCBlocks.woodAxle;

        Map<ForgeDirection, Integer> wedges = quarry.getWedges();
        if (wedges != null) {
            renderer.renderAllFaces = true;

            for (ForgeDirection edge : ForgeDirection.VALID_DIRECTIONS) {
                // Skip front and back relative to quarry location
                if (edge != front && edge != back) {
                    if (wedges.containsKey(edge)) {
                        int count = wedges.get(edge);
                        if (count > 0) {
                            QuarryWedgeBounds[] b = QuarryWedgeModeller.getEdgeWedgeBounds(front, edge);
                            for (int i = 0; i < count; i++) {
                                renderer.setRenderBounds(b[i].x1, b[i].y1, b[i].z1, b[i].x2, b[i].y2, b[i].z2);
                                renderer.renderStandardBlock(materialBlock, x, y, z);
                            }
                        }
                    }
                }
            }

            renderer.renderAllFaces = false;
        } else {
            Bids.LOG.debug("Wedges not initialized yet, won't render");
        }

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
