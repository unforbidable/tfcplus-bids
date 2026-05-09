package com.unforbidable.tfc.bids.features.device.woodpile.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodPile;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

public class WoodPileWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) accessor.getTileEntity();
            return woodPile.getSelectedItem(true);
        }

        return super.getWailaStack(accessor, config);
    }

}
