package com.unforbidable.tfc.bids.features.device.processingsurface.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ProcessingSurfaceWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityProcessingSurface) {
            TileEntityProcessingSurface te = (TileEntityProcessingSurface) accessor.getTileEntity();
            if (te.getInputItem() != null) {
                if (te.isWorkDone()) {
                    return te.getResultItem();
                } else {
                    return te.getInputItem();
                }
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityProcessingSurface) {
            TileEntityProcessingSurface te = (TileEntityProcessingSurface) accessor.getTileEntity();

            if (te.getInputItem() != null && !te.isWorkDone()) {
                ItemStack output = te.getResultItem();
                int progress = Math.round(te.getWorkProgress() * 100);
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                    + output.getDisplayName()
                    + (progress > 0 ? String.format(" (%d%%)", progress) : ""));
            }
        }
        return currenttip;
    }

}
