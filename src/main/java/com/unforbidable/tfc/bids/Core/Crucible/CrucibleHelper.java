package com.unforbidable.tfc.bids.Core.Crucible;

import java.util.Arrays;
import java.util.List;

import com.dunk.tfc.Core.TFC_Achievements;
import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.Core.Metal.AlloyManager;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.Core.Metal.AlloyMetalCompare;
import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.dunk.tfc.Items.Pottery.ItemPotteryMoldBase;
import com.dunk.tfc.Items.Pottery.ItemPotterySheetMold;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrucibleHelper {

    static final List<Item> oreItems = Arrays
            .asList(new Item[] { TFCItems.oreChunk, TFCItems.smallOreChunk, BidsItems.oreBit });

    public static int getMetalReturnAmount(ItemStack itemstack) {
        return itemstack.getItem() instanceof ISmeltable
                ? ((ISmeltable) itemstack.getItem()).getMetalReturnAmount(itemstack)
                : 0;
    }

    public static float getHeatCapacity(ItemStack itemstack) {
        return TFC_ItemHeat.getSpecificHeat(itemstack);
    }

    public static boolean isMeltedAtTemp(ItemStack itemstack, float temp) {
        HeatRegistry manager = HeatRegistry.getInstance();
        if (itemstack != null && manager != null) {
            HeatIndex hi = manager.findMatchingIndex(itemstack);
            if (hi != null)
                return hi.meltTemp <= temp;
            else
                return false;
        } else {
            return false;
        }
    }

    public static boolean isMeltedAtTemp(Metal metal, float temp) {
        HeatRegistry manager = HeatRegistry.getInstance();
        if (manager != null && metal.getResultFromMold(TFCItems.ceramicMold) != null) {
            HeatIndex hi = manager.findMatchingIndex(new ItemStack(metal.getResultFromMold(TFCItems.ceramicMold)));
            if (hi != null)
                return hi.meltTemp <= temp;
            else
                return false;
        } else {
            return false;
        }
    }

    public static Metal getMetalFromLiquid(List<CrucibleLiquidItem> liquid) {
        if (liquid.size() == 0) {
            return null;
        } else if (liquid.size() == 1) {
            return liquid.get(0).getMetal();
        } else {
            float totalVolume = 0;
            for (CrucibleLiquidItem it : liquid) {
                totalVolume += it.volume;
            }

            Metal m = Global.UNKNOWN;

            // Get list of alloys from AlloyManager
            for (Alloy alloy : AlloyManager.INSTANCE.alloys) {
                // Only interested in actual alloys
                if (alloy.alloyIngred.size() > 1) {
                    int matchCount = 0;
                    float existingAlloyVolume = 0;

                    // First look for the alloy already melted in the mixture
                    for (CrucibleLiquidItem item : liquid) {
                        if (item.metal == alloy.outputType) {
                            // Presence of the output alloy is tolerated, so we ignore it
                            // just account for it when calculating part percentages
                            existingAlloyVolume = item.volume;
                            Bids.LOG.debug("Existing alloy " + alloy.outputType + " will be ignored");
                            break;
                        }
                    }

                    // Match every alloy ingredient
                    // with every item in our liquid mixture
                    for (AlloyMetal am : alloy.alloyIngred) {
                        AlloyMetalCompare amc = (AlloyMetalCompare) am;
                        for (CrucibleLiquidItem item : liquid) {
                            if (item.getMetal() == amc.metalType) {
                                float part = item.getVolume() / (totalVolume - existingAlloyVolume) * 100;
                                if (amc.getMetalMax() >= part && amc.getMetalMin() <= part) {
                                    matchCount++;
                                    break;
                                }
                            }
                        }
                    }

                    // Make sure we matched all components, no less and no more
                    // but ignore existing alloy
                    if (matchCount == alloy.alloyIngred.size()
                            && (matchCount == liquid.size() && existingAlloyVolume == 0
                                    || matchCount + 1 == liquid.size() && existingAlloyVolume > 0)) {
                        m = alloy.outputType;
                        break;
                    }
                }
            }

            return m;
        }
    }

    public static Metal getMetalFromItem(Item item) {
        return MetalRegistry.instance.getMetalFromItem(item);
    }

    public static Metal getMetalFromSmeltable(ItemStack itemstack) {
        return ((ISmeltable) itemstack.getItem()).getMetalType(itemstack);
    }

    public static boolean isValidMold(ItemStack liquidOutputStack, Metal metal) {
        return liquidOutputStack.getItem() instanceof ItemPotteryMoldBase
                && (metal == null || metal.isValidMold(liquidOutputStack))
                && ((ItemPotteryMoldBase) (liquidOutputStack.getItem())).isValidMold(liquidOutputStack);
    }

    public static boolean isFullToolMold(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemPotteryMold
                && !(itemStack.getItem() instanceof ItemPotterySheetMold)
                && itemStack.getItemDamage() > 1 /* Ceramic */
                && itemStack.getItemDamage() <= 5 /* Full of Metal */;
    }

    public static ItemStack fillMold(ItemStack liquidOutputStack, Metal metal, int units) {
        ItemPotteryMoldBase prevMoldItem = ((ItemPotteryMoldBase) (liquidOutputStack.getItem()));
        int prev = prevMoldItem.getUnits(liquidOutputStack);
        ItemStack mold = new ItemStack(metal.getResultFromMold(liquidOutputStack.getItem()), 1, 0);
        mold.setItemDamage(metal.getBaseValueForResult(liquidOutputStack.getItem()));
        mold = ((ItemPotteryMoldBase) (mold.getItem())).setToMinimumUnits(mold);
        mold = ((ItemPotteryMoldBase) (mold.getItem())).addUnits(mold, units + prev);
        return mold;
    }

    public static int getMoldUnits(ItemStack mold) {
        return ((ItemPotteryMoldBase)mold.getItem()).getUnits(mold);
    }

    public static boolean isOreIron(ItemStack is) {
        return oreItems.contains(is.getItem()) && getMetalFromSmeltable(is) == Global.PIGIRON;
    }

    public static void triggerCopperAgeAchievement(EntityPlayer player) {
        player.triggerAchievement(TFC_Achievements.achCopperAge);
        Bids.LOG.debug("Copper Age achievement triggered");
    }

    public static void triggerCrucibleAchievement(EntityPlayer player) {
        player.triggerAchievement(TFC_Achievements.achCrucible);
        Bids.LOG.debug("Crucible achievement triggered");
    }

}
