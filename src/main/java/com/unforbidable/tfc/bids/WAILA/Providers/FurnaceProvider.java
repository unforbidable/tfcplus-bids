package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class FurnaceProvider extends WailaProvider {

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
