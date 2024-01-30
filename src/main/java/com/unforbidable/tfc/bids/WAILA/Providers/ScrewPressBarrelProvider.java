package com.unforbidable.tfc.bids.WAILA.Providers;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ScrewPressBarrelProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityScrewPressBarrel) {
            TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) accessor.getTileEntity();

            float inputFullness = teBarrel.getInputFullness();
            if (inputFullness > 0) {
                currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Load") + " " + Math.round(inputFullness * 100) + "%");
            }

            FluidStack fluidStack = teBarrel.getOutputFluid();
            if (fluidStack != null) {
                currenttip.add(ChatFormatting.GRAY + fluidStack.getFluid().getLocalizedName(fluidStack) + " " + fluidStack.amount + " mB");
            }
        }

        return currenttip;
    }

}
