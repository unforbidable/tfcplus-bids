package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItemInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class DryingRackProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingRack) {
            TileEntityDryingRack dryingRack = (TileEntityDryingRack) accessor.getTileEntity();
            DryingRackItem item = dryingRack.getSelectedItem();

            return item != null ? item.dryingItem : null;
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingRack) {
            TileEntityDryingRack dryingRack = (TileEntityDryingRack) accessor.getTileEntity();
            DryingRackItemInfo info = dryingRack.getSelectedItemInfo();

            if (info != null) {
                float hoursRemaining = (info.dryingTicksRemaining * 10 / (int) TFC_Time.HOUR_LENGTH) / 10f;
                String hoursRemainingFractions = "" + hoursRemaining;
                String hoursRemainingRounded = "" + Math.round(hoursRemaining);

                // Actually showing fractions only when less then 1 hour
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                        + (hoursRemaining < 0.95f ? hoursRemainingFractions : hoursRemainingRounded));

                if (info.recipe != null) {
                    ItemStack output = info.recipe.getCraftingResult(info.item.dryingItem);
                    currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                            + output.getDisplayName());
                }
            }
        }

        return currenttip;
    }

}
