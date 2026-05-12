package com.unforbidable.tfc.bids.features.device.woodpile.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class WoodpileWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityWoodpile) {
            TileEntityWoodpile woodPile = (TileEntityWoodpile) accessor.getTileEntity();
            return woodPile.getSelectedItem(true);
        }

        return super.getWailaStack(accessor, config);
    }

}
