package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.TileEntities.TileEntityClayLamp;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ClayLampProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityClayLamp) {
            TileEntityClayLamp clayLamp = (TileEntityClayLamp) accessor.getTileEntity();
            if (clayLamp.isClientDataLoaded()) {
                currentTip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                    + clayLamp.getFuelTimeLeft());
            }
        }

        return currentTip;
    }

}
