package com.unforbidable.tfc.bids.features.device.dryingframe.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class DryingPegsWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingPegs) {
            TileEntityDryingPegs te = (TileEntityDryingPegs) accessor.getTileEntity();
            if (te.hasItem()) {
                return te.getItem();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingPegs) {
            TileEntityDryingPegs te = (TileEntityDryingPegs) accessor.getTileEntity();
        }
        return currenttip;
    }

}
