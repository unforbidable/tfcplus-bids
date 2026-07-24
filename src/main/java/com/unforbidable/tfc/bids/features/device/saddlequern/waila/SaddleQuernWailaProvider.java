package com.unforbidable.tfc.bids.features.device.saddlequern.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntitySaddleQuern;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class SaddleQuernWailaProvider extends WailaDataProvider {

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
