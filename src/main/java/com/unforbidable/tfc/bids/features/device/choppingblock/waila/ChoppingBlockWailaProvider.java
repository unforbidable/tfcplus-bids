package com.unforbidable.tfc.bids.features.device.choppingblock.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.choppingblock.tileentity.TileEntityChoppingBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class ChoppingBlockWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityChoppingBlock) {
            TileEntityChoppingBlock choppingBlock = (TileEntityChoppingBlock) accessor.getTileEntity();
            return choppingBlock.getSelectedItem();
        }

        return super.getWailaStack(accessor, config);
    }

}
