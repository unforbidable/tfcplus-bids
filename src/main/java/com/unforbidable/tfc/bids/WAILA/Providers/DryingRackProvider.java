package com.unforbidable.tfc.bids.WAILA.Providers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCOptions;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.Core.Drying.DryingHelper;
import com.unforbidable.tfc.bids.Core.Drying.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingEnvironment;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingFoodRecipe;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

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
            if (item != null) {
                return item.getCurrentItem();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityDryingRack) {
            TileEntityDryingRack dryingRack = (TileEntityDryingRack) accessor.getTileEntity();
            DryingRackItem dryingItem = dryingRack.getSelectedItem();
            if (dryingItem != null) {
                if (dryingItem.inputItem.getItem() instanceof ItemClothing) {
                    // Only show dump/soaked for clothes, no progress
                    if (TFC_Core.isClothingDamp(dryingItem.inputItem, accessor.getPlayer())) {
                        currenttip.add(EnumChatFormatting.BLUE + TFC_Core.translate("gui.damp") + ": " + TFC_Core.getClothingWetness(dryingItem.inputItem));
                    } else if (TFC_Core.isClothingSoaked(dryingItem.inputItem, accessor.getPlayer())) {
                        currenttip.add(EnumChatFormatting.BLUE + TFC_Core.translate("gui.soaked") + ": " + TFC_Core.getClothingWetness(dryingItem.inputItem));
                    }
                } else if (dryingItem.failure < 1) {
                    DryingRecipe recipe = dryingRack.getDryingRecipe(dryingItem);
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
                        } else {
                            if (recipe instanceof IDryingFoodRecipe) {
                                // Show drying output and progress only when in progress
                                if (dryingItem.finishedTicks > 0) {
                                    // Output is fully dried, on top of existing tags
                                    ItemStack result = dryingItem.inputItem.copy();
                                    Food.setDried(result, Food.DRYHOURS);

                                    String output = DryingHelper.getItemStackInfoString(result);
                                    String progress = DryingHelper.getProgressInfoString(dryingItem.progress);
                                    currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                                        + ChatFormatting.WHITE + output + progress);

                                    long ticksRemaining = dryingItem.finishedTicks - TFC_Time.getTotalTicks();
                                    String hoursRemaining = DryingHelper.getHoursRemainingInfoString(ticksRemaining);
                                    currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                                        + ChatFormatting.WHITE + hoursRemaining);
                                }

                                // Show smoking output and progress only when in progress
                                if (dryingItem.smokedTicks > 0) {
                                    // Output is fully smoked, on top of existing tags, with an arbitrary fuel taste profile
                                    ItemStack result = dryingItem.inputItem.copy();
                                    Food.setSmokeCounter(result, Food.SMOKEHOURS);
                                    Food.setFuelProfile(result, EnumFuelMaterial.OAK.tasteProfile);

                                    String output = DryingHelper.getItemStackInfoString(result);
                                    String progress = DryingHelper.getProgressInfoString(dryingItem.smoke);
                                    currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                                        + ChatFormatting.WHITE + output + progress);

                                    // smoking is in progress
                                    long ticksRemaining = dryingItem.smokedTicks - TFC_Time.getTotalTicks();
                                    String hoursRemaining = DryingHelper.getHoursRemainingInfoString(ticksRemaining);
                                    currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.HoursRemaining") + ": "
                                        + ChatFormatting.WHITE + hoursRemaining);
                                }
                            } else {
                                if (dryingItem.progress < 1) {
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
                    currenttip.add(ChatFormatting.DARK_GRAY + "Smoke: " + env.isSmoked());
                }
            }
        }

        return currenttip;
    }

}
