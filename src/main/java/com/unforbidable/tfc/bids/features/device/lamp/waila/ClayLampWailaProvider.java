package com.unforbidable.tfc.bids.features.device.lamp.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.lamp.tileentity.TileEntityClayLamp;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ClayLampWailaProvider extends WailaDataProvider {

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
                if (clayLamp.hasFuel()) {
                    currentTip.add("" + EnumChatFormatting.GRAY + clayLamp.getFuel().getLocalizedName());

                    int timeLeft = Math.round(clayLamp.getFuelTimeLeft());
                    currentTip.add("" + EnumChatFormatting.GRAY + timeLeft + " " + StatCollector.translateToLocal("gui.HoursRemaining"));
                }
            }
        }

        return currentTip;
    }

}
