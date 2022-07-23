package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class CrucibleProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCrucible) {
            TileEntityCrucible tileEntityCrucible = (TileEntityCrucible) accessor.getTileEntity();

            for (String line : tileEntityCrucible
                    .getLiquidInfo(StatCollector.translateToLocal("gui.symbol.BulletPoint") + " ")) {
                currenttip.add(EnumChatFormatting.GRAY + line);
            }

            int liquidTemp = tileEntityCrucible.getCombinedTemp();
            if (liquidTemp > 0) {
                String liquidTempString = TFC_ItemHeat.getHeatColor(liquidTemp, Integer.MAX_VALUE);
                currenttip.add(liquidTempString);
            }

            if (tileEntityCrucible.isRuined()) {
                currenttip.add(EnumChatFormatting.DARK_RED
                        + StatCollector.translateToLocal("gui.Ruined"));
            }
        }

        return currenttip;
    }

}
