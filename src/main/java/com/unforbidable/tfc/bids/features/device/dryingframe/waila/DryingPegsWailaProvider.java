package com.unforbidable.tfc.bids.features.device.dryingframe.waila;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCOptions;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.api.features.drying.DryingEnvironment;
import com.unforbidable.tfc.bids.api.features.drying.DryingRecipe;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingHelper;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import com.unforbidable.tfc.bids.features.crafting.drying.main.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class DryingPegsWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingPegs) {
            TileEntityDryingPegs te = (TileEntityDryingPegs) accessor.getTileEntity();
            if (te.hasItem()) {
                return te.getItem().getCurrentItem();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingPegs) {
            TileEntityDryingPegs dryingPegs = (TileEntityDryingPegs) accessor.getTileEntity();
            DryingItem dryingItem = dryingPegs.getItem();
            if (dryingItem != null) {
                if (dryingItem.failure < 1) {
                    DryingRecipe recipe = dryingPegs.getDryingRecipe(dryingItem);
                    if (recipe != null) {
                        if (dryingItem.wetness > 0) {
                            WetnessInfo wetnessInfo = DryingHelper.getWetnessInfo(dryingItem.inputItem);
                            currenttip.add(EnumChatFormatting.BLUE + TFC_Core.translate("gui.damp") + ": " + (int)Math.ceil(dryingItem.wetness * wetnessInfo.capacity));
                        }

                        if (dryingItem.failure > 0 && dryingItem.failure < 1) {
                            String output = DryingHelper.getItemStackInfoString(DryingHelper.getDestroyedResultItem(dryingItem, recipe));
                            String progress = DryingHelper.getProgressInfoString(dryingItem.failure);

                            currenttip.add(ChatFormatting.RED + StatCollector.translateToLocal("gui.Ruined") + ": "
                                + ChatFormatting.WHITE + output + progress);
                        } else if (dryingItem.progress < 1) {
                            String output = DryingHelper.getItemStackInfoString(DryingHelper.getResultItem(dryingItem, recipe));
                            String progress = DryingHelper.getProgressInfoString(dryingItem.progress);
                            currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                                + ChatFormatting.WHITE + output + progress);

                            if (dryingItem.finishedTicks > 0) {
                                long ticksRemaining = dryingItem.finishedTicks - TFC_Time.getTotalTicks();
                                String hoursRemaining = DryingHelper.getHoursRemainingInfoString(ticksRemaining);
                                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                                    + ChatFormatting.WHITE + hoursRemaining);
                            }
                        }
                    }
                }

                if (TFCOptions.enableDebugMode) {
                    DryingEnvironment env = new StaticEnvironment(accessor.getWorld(), accessor.getTileEntity().xCoord, accessor.getTileEntity().yCoord, accessor.getTileEntity().zCoord)
                        .ofTicks(TFC_Time.getTotalTicks()).ofItem(dryingItem);

                    currenttip.add(ChatFormatting.DARK_GRAY + "Exposed: " + env.isExposed());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Heated: " + env.isHeated());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Airflow: " + env.getAirflow());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Sunlight: " + env.getSunlight());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Temperature: " + env.getTemperature());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Precipitation: " + env.getPrecipitation());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Humidity: " + env.getHumidity());
                    currenttip.add(ChatFormatting.DARK_GRAY + "Wetness: " + env.getWetness());
                }
            }
        }
        return currenttip;
    }

}
