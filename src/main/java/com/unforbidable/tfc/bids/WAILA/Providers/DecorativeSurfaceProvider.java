package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.TileEntities.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class DecorativeSurfaceProvider extends WailaProvider {

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
