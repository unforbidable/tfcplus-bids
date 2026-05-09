package com.unforbidable.tfc.bids.features.device.crucible.waila;

import com.unforbidable.tfc.bids.util.chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityCrucible;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class FurnaceWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (ChimneyHelper.isChimney(accessor.getTileEntity())) {
            TileEntityCrucible crucible = ChimneyHelper.findActiveFurnaceCrucible(accessor.getTileEntity());
            if (crucible != null)
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Glassmaking") + ": "
                        + crucible.getGlassMakingRemainingHours() + " "
                        + StatCollector.translateToLocal("gui.HoursRemaining"));
        }

        return currenttip;
    }

}
