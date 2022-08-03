package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class WoodPileProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) accessor.getTileEntity();
            for (ItemStack item : woodPile.getItems(true)) {
                currenttip.add(EnumChatFormatting.GRAY + item.getDisplayName() + ": "
                        + item.stackSize);
            }
        }

        return currenttip;
    }

}
