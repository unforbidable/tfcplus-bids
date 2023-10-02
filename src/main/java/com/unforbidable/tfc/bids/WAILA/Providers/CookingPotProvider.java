package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CookingPotProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCookingPot) {
            TileEntityCookingPot tileEntityCookingPot = (TileEntityCookingPot) accessor.getTileEntity();

            CookingHelper.getCookingPotInfo(tileEntityCookingPot, currenttip, true);
        }

        return currenttip;
    }

}
