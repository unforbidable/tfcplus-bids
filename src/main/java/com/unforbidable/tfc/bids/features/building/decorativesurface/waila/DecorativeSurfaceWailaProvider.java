package com.unforbidable.tfc.bids.features.building.decorativesurface.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.building.decorativesurface.tileentity.TileEntityDecorativeSurface;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class DecorativeSurfaceWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDecorativeSurface) {
            TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) accessor.getTileEntity();
            if (te.getItem() != null) {
                return te.getItem();
            }
        }

        return super.getWailaStack(accessor, config);
    }

}
