package com.unforbidable.tfc.bids.features.device.firepit.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class FirepitWailaProvider extends WailaDataProvider {

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
