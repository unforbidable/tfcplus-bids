package com.unforbidable.tfc.bids.features.resource.quarry.waila;

import java.util.List;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.resource.quarry.tileentity.TileEntityQuarry;
import com.unforbidable.tfc.bids.features.resource.quarry.main.QuarryHelper;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class QuarryWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityQuarry) {
            TileEntityQuarry quarry = (TileEntityQuarry) accessor.getTileEntity();
            currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Wedges") + ": "
                    + quarry.getWedgeCount() + "/" + quarry.getMaxWedgeCount());

            // Wait for the quarry to be initialized
            // before even considering showing readiness
            if (quarry.getMaxWedgeCount() > 0
                    && QuarryHelper.isQuarryReadyAt(accessor.getWorld(), accessor.getPosition().blockX,
                            accessor.getPosition().blockY, accessor.getPosition().blockZ)) {
                currenttip.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.QuarryReady"));
            }
        }

        return currenttip;
    }

}
