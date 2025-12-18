package com.unforbidable.tfc.bids.WAILA.Providers;

import com.unforbidable.tfc.bids.Core.SoakingSurface.SoakingSurfaceSlotProgress;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class SoakingSurfaceProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntitySoakingSurface) {
            TileEntitySoakingSurface soakingSurface = (TileEntitySoakingSurface) accessor.getTileEntity();
            return soakingSurface.getSelectedActualItem();
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntitySoakingSurface) {
            TileEntitySoakingSurface soakingSurface = (TileEntitySoakingSurface) accessor.getTileEntity();
            SoakingSurfaceSlotProgress progress = soakingSurface.getSelectedItemProgress();
            if (progress != null && progress.progress < 1) {
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": " + progress.result.getDisplayName());
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": " + ((int)Math.ceil(progress.hoursRemaining)));
            }
        }

        return currenttip;
    }

}
