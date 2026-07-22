package com.unforbidable.tfc.bids.core.stats;

import com.dunk.tfc.Core.TFC_Achievements;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.features.utility.compositetools.main.CompositeToolHelper;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StatsCraftingHandler {

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent e) {
        if (e.crafting.getItem() == Item.getItemFromBlock(BidsBlocks.fireClayCrucible)) {
            e.player.triggerAchievement(TFC_Achievements.achCrucible);
        }

        if (CompositeToolHelper.isCompositeTool(e.crafting)) {
            for (int i = 0; i < e.craftMatrix.getSizeInventory(); i++) {
                ItemStack is = e.craftMatrix.getStackInSlot(i);
                if (OreDictionaryHelper.itemMatchesOre(is, "materialBinding", false)) {
                    e.player.triggerAchievement(BidsAchievements.COMPOSITE_TOOL);
                    break;
                }
            }
        }

        if (OreDictionaryHelper.itemMatchesOre(e.crafting, "itemAdzeStone", false)) {
            e.player.triggerAchievement(BidsAchievements.STONE_ADZE);
        }

        if (OreDictionaryHelper.itemMatchesOre(e.crafting, "itemDrillStone", false)) {
            e.player.triggerAchievement(BidsAchievements.STONE_DRILL);
        }

        if (OreDictionaryHelper.itemMatchesOre(e.crafting, "itemDrillMetal", false)) {
            e.player.triggerAchievement(BidsAchievements.METAL_DRILL);
        }

        if (OreDictionaryHelper.itemMatchesOre(e.crafting, "itemHandAxe", false)) {
            e.player.triggerAchievement(TFC_Achievements.achStoneAge);
        }

        if (e.crafting.getItem() == TFCItems.quern) {
            List<ItemStack> stoneQuernOres = OreDictionary.getOres("stoneQuern");

            for (int i = 0; i < e.craftMatrix.getSizeInventory(); i++) {
                ItemStack is = e.craftMatrix.getStackInSlot(i);
                 if (is != null && OreDictionaryHelper.itemMatchesOre(is, stoneQuernOres, false)) {
                    e.player.triggerAchievement(BidsAchievements.HARD_QUERN);
                    break;
                }
            }
        }
    }

}
