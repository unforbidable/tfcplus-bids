package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CarvingProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCarving) {
            TileEntityCarving carving = (TileEntityCarving) accessor.getTileEntity();
            Block block = Block.getBlockById(carving.getCarvedBlockId());
            int metadata = carving.getCarvedBlockMetadata();
            return new ItemStack(block, 1, metadata);
        }

        return null;
    }

}
