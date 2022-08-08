package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class FirepitProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityNewFirepit) {
            // Remove tips from TFC firepit
            // which only seems to count logs
            currenttip.clear();

            TileEntityNewFirepit firepit = (TileEntityNewFirepit) accessor.getTileEntity();
            currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Fuel") + ": "
                    + firepit.getFuelCount() + "/" + firepit.getMaxFuelCount());
        }

        return currenttip;
    }

}
