package com.unforbidable.tfc.bids.WAILA.Providers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.api.TFCOptions;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.Core.Drying.DryingHelper;
import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.Core.Drying.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingEnvironment;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class DryingSurfaceProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingSurface) {
            TileEntityDryingSurface dryingSurface = (TileEntityDryingSurface) accessor.getTileEntity();
            DryingItem item = dryingSurface.getSelectedItem();
            if (item != null) {
                return item.getCurrentItem();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingSurface) {
            TileEntityDryingSurface dryingSurface = (TileEntityDryingSurface) accessor.getTileEntity();
            DryingItem dryingItem = dryingSurface.getSelectedItem();
            if (dryingItem != null) {
                if (dryingItem.inputItem.getItem() instanceof ItemClothing) {
                    // Only show dump/soaked for clothes, no progress
                    if (TFC_Core.isClothingDamp(dryingItem.inputItem, accessor.getPlayer())) {
                        currenttip.add(EnumChatFormatting.BLUE + TFC_Core.translate("gui.damp") + ": " + TFC_Core.getClothingWetness(dryingItem.inputItem));
                    } else if (TFC_Core.isClothingSoaked(dryingItem.inputItem, accessor.getPlayer())) {
                        currenttip.add(EnumChatFormatting.BLUE + TFC_Core.translate("gui.soaked") + ": " + TFC_Core.getClothingWetness(dryingItem.inputItem));
                    }
                } else if (dryingItem.failure < 1) {
                    DryingRecipe recipe = dryingSurface.getDryingRecipe(dryingItem);
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
                            String progress = dryingItem.paused
                                ? " (" + StatCollector.translateToLocal("gui.Paused") + ")"
                                : DryingHelper.getProgressInfoString(dryingItem.progress);

                            currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                                + ChatFormatting.WHITE + output + progress);

                            if (dryingItem.finishedTicks > 0 && !dryingItem.isComplete() && !dryingItem.paused) {
                                long ticksRemaining = dryingItem.finishedTicks - TFC_Time.getTotalTicks();
                                float hoursRemaining = (float) ticksRemaining / TFC_Time.HOUR_LENGTH;
                                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                                    + ChatFormatting.WHITE + (Math.ceil(hoursRemaining * 10) / 10f));
                            }
                        }
                    }
                }

                if (TFCOptions.enableDebugMode) {
                    IDryingEnvironment env = new StaticEnvironment(accessor.getWorld(), accessor.getTileEntity().xCoord, accessor.getTileEntity().yCoord, accessor.getTileEntity().zCoord)
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
