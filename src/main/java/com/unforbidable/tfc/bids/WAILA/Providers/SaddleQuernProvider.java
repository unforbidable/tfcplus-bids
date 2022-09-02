package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class SaddleQuernProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntitySaddleQuern) {
            TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) accessor.getTileEntity();
            return saddleQuern.getSelectedItemStack();
        }

        return null;
    }

}
