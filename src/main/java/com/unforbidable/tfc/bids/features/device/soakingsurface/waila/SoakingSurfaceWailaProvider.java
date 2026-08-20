package com.unforbidable.tfc.bids.features.device.soakingsurface.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceSlotProgress;
import com.unforbidable.tfc.bids.features.device.soakingsurface.tileentity.TileEntitySoakingSurface;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class SoakingSurfaceWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntitySoakingSurface) {
            TileEntitySoakingSurface soakingSurface = (TileEntitySoakingSurface) accessor.getTileEntity();
            return soakingSurface.getSelectedItemStack();
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntitySoakingSurface) {
            TileEntitySoakingSurface soakingSurface = (TileEntitySoakingSurface) accessor.getTileEntity();
            SoakingSurfaceSlotProgress progress = soakingSurface.getSelectedItemProgress();
            if (progress != null) {
                ItemStack result = progress.recipe.getResult(soakingSurface.getSelectedItemStack());
                if (result != null) {
                    currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": " + result.getDisplayName());
                    currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": " + ((int) Math.ceil(Math.max(0, progress.hoursRemaining))));
                }
            }
        }

        return currenttip;
    }

}
