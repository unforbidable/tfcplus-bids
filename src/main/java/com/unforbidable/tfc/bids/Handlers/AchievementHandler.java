package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Achievements;
import com.dunk.tfc.Items.ItemMeltedMetal;
import com.dunk.tfc.Items.Pottery.ItemPotteryBlowpipe;
import com.dunk.tfc.Items.Pottery.ItemPotteryMoldBase;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Events.AnvilCraftEvent;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Blocks.BlockAquifer;
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.OreDictionaryHelper;
import com.unforbidable.tfc.bids.Core.SaddleQuern.EnumWorkStoneType;
import com.unforbidable.tfc.bids.Items.ItemMetalBlowpipe;
import com.unforbidable.tfc.bids.Items.ItemNewCustomSeeds;
import com.unforbidable.tfc.bids.api.BidsAchievements;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsStats;
import com.unforbidable.tfc.bids.api.Events.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class AchievementHandler {

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        if (event.item.getEntityItem().getItem() == BidsItems.bark) {
            event.entityPlayer.triggerAchievement(BidsAchievements.BARK);
        }

        if (event.item.getEntityItem().getItem() == Item.getItemFromBlock(BidsBlocks.saddleQuernBaseSed)) {
            event.entityPlayer.triggerAchievement(BidsAchievements.SADDLE_QUERN);
        }

        if (event.item.getEntityItem().getItem() == Item.getItemFromBlock(BidsBlocks.roughStoneSed)) {
            event.entityPlayer.triggerAchievement(BidsAchievements.ROUGH_STONE);
        }

        if (event.item.getEntityItem().getItem() == Item.getItemFromBlock(BidsBlocks.cookingPot) && event.item.getEntityItem().getItemDamage() == 1) {
            event.entityPlayer.triggerAchievement(BidsAchievements.COOKING_POT);
        }

        if (event.item.getEntityItem().getItem() == Item.getItemFromBlock(BidsBlocks.clayCrucible) && event.item.getEntityItem().getItemDamage() == 0) {
            event.entityPlayer.triggerAchievement(BidsAchievements.CERAMIC_CRUCIBLE);
        }

        if (event.item.getEntityItem().getItem() instanceof ItemNewCustomSeeds) {
            List<ItemStack> cultivatedSeedOres = OreDictionary.getOres("seedCultivated");
            for (ItemStack ore : cultivatedSeedOres) {
                if (ItemStack.areItemStacksEqual(ore, event.item.getEntityItem())) {
                    event.entityPlayer.triggerAchievement(BidsAchievements.CULTIVATED_SEED);
                }
            }

            List<ItemStack> winterCerealSeed = OreDictionary.getOres("seedWinterCereal");
            for (ItemStack ore : winterCerealSeed) {
                if (ItemStack.areItemStacksEqual(ore, event.item.getEntityItem())) {
                    event.entityPlayer.triggerAchievement(BidsAchievements.WINTER_CEREAL_SEED);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlock(BlockEvent.BreakEvent event) {
        Block below = event.world.getBlock(event.x, event.y - 1, event.z);
        if (below instanceof BlockAquifer) {
            event.getPlayer().triggerAchievement(BidsAchievements.AQUIFER);
        }
    }

    @SubscribeEvent
    public void onCookingPotPlayer(CookingPotPlayerEvent event) {
        if (event.action == CookingPotPlayerEvent.Action.RETRIEVE_COOKED_MEAL) {
            event.entityPlayer.triggerAchievement(BidsAchievements.COOKED_MEAL);

            if (event.result.getItem() == BidsItems.stew) {
                Item mainIngredient = CookingMixtureHelper.getMainIngredient(event.result);
                if (mainIngredient != null && OreDictionaryHelper.itemMatchesOre(new ItemStack(mainIngredient), "foodMushroom", false)) {
                    event.entityPlayer.triggerAchievement(BidsAchievements.MUSHROOM_STEW);
                }
            }

            if (event.result.getItem() == BidsItems.porridge) {
                List<FluidStack> fluids = CookingMixtureHelper.getCookedMealFluids(event.result);
                for (FluidStack fluid : fluids) {
                    EnumFoodGroup foodGroup = CookingMixtureHelper.getFluidFoodGroup(fluid);
                    if (foodGroup == EnumFoodGroup.Dairy) {
                        event.entityPlayer.triggerAchievement(BidsAchievements.MILK_PORRIDGE);
                    }
                }
            }

            event.entityPlayer.triggerAchievement(BidsStats.MEALS_COOKED);
        }
    }

    @SubscribeEvent
    public void onSaddleQuernPlayer(SaddleQuernPlayerEvent event) {
        if (event.action == SaddleQuernPlayerEvent.Action.PLACE_WORK_STONE && event.workStoneType == EnumWorkStoneType.SADDLE_QUERN_CRUSHING) {
            event.entityPlayer.triggerAchievement(BidsAchievements.HAND_STONE);
        }

        if (event.action == SaddleQuernPlayerEvent.Action.USE_WORK_STONE && event.workStoneType == EnumWorkStoneType.SADDLE_QUERN_CRUSHING) {
            event.entityPlayer.triggerAchievement(BidsStats.SADDLE_QUERN_USED);
        }

        if (event.action == SaddleQuernPlayerEvent.Action.USE_WORK_STONE && event.workStoneType == EnumWorkStoneType.SADDLE_QUERN_PRESSING) {
            event.entityPlayer.triggerAchievement(BidsAchievements.PRESSING_STONE_USE);
        }

        if (event.action == SaddleQuernPlayerEvent.Action.PLACE_LEVER) {
            event.entityPlayer.triggerAchievement(BidsAchievements.STONE_PRESS_LEVER);
        }

        if (event.action == SaddleQuernPlayerEvent.Action.ATTACH_WEIGHT_STONE) {
            event.entityPlayer.triggerAchievement(BidsAchievements.WEIGHT_STONE_USE);
        }
    }

    @SubscribeEvent
    public void onQuarry(QuarryPlayerEvent event) {
        if (event.action == QuarryPlayerEvent.Action.QUARRY_FINISHED) {
            event.entityPlayer.triggerAchievement(BidsStats.BLOCKS_QUARRIED);
        }
    }

    @SubscribeEvent
    public void onCrucible(CruciblePlayerEvent event) {
        if (event.action == CruciblePlayerEvent.Action.FILL_BLOWPIPE) {
            event.entityPlayer.triggerAchievement(BidsAchievements.CRUCIBLE_GLASS);
        }

        if (event.action == CruciblePlayerEvent.Action.RETRIEVE_OUTPUT) {
            // Trigger the copper age achievement when a full
            // tool mold is removed from the output slot
            if (CrucibleHelper.isFullToolMold(event.result)) {
                CrucibleHelper.triggerCopperAgeAchievement(event.entityPlayer);
            }

            if (event.result.getItem() instanceof ItemMeltedMetal) {
                int units = ((ItemPotteryMoldBase) event.result.getItem()).getUnits(event.result);
                if (units == 100) {
                    event.entityPlayer.triggerAchievement(BidsAchievements.CRUCIBLE_INGOT);
                }
            }

            if (event.result.getItem() instanceof ItemPotteryBlowpipe ||
                event.result.getItem() instanceof ItemMetalBlowpipe) {
                int units = ((ItemPotteryMoldBase) event.result.getItem()).getUnits(event.result);
                if (units > 0) {
                    event.entityPlayer.triggerAchievement(BidsAchievements.CRUCIBLE_GLASS);
                }
            }
        }
    }

    @SubscribeEvent
    public void onChoppingBlock(ChoppingBlockPlayerEvent event) {
        if (event.action == ChoppingBlockPlayerEvent.Action.ITEM_CRAFTED) {
            if (event.result.getItem() == BidsItems.firewood || event.result.getItem() == BidsItems.firewoodSeasoned) {
                event.entityPlayer.triggerAchievement(BidsStats.FIREWOOD_CHOPPED);
            }
        }
    }

    @SubscribeEvent
    public void onAnvilCraft(AnvilCraftEvent event) {
        if (event.result != null) {
            if (event.result.getItem() == Item.getItemFromBlock(TFCBlocks.anvil) || event.result.getItem() == Item.getItemFromBlock(TFCBlocks.anvil2)) {
                ((EntityPlayer) event.entity).triggerAchievement(BidsAchievements.WELDED_ANVIL);
            }

            if (event.result.getItem() == Item.getItemFromBlock(TFCBlocks.anvil) && event.result.getItemDamage() == 2 ||
                event.result.getItem() == Item.getItemFromBlock(TFCBlocks.anvil2) && (event.result.getItemDamage() == 1 || event.result.getItemDamage() == 2)) {
                ((EntityPlayer) event.entity).triggerAchievement(TFC_Achievements.achBronzeAge);
            }
        }
    }

    @SubscribeEvent
    public void onAnimalMilk(AnimalMilkEvent.Milked event) {
        event.player.addStat(BidsStats.MILK_MILKED, event.result.amount);
    }

    @SubscribeEvent
    public void onChurnWaterskin(WaterskinChurnEvent event) {
        if (event.action == WaterskinChurnEvent.Action.DONE) {
            if (event.result.getItem() == BidsItems.butter) {
                ((EntityPlayer) event.entity).triggerAchievement(BidsAchievements.BUTTER);

                int weight = Math.round(Food.getWeight(event.result));
                ((EntityPlayer) event.entity).addStat(BidsStats.BUTTER_CHURNED, weight);
            }
        }
    }

    @SubscribeEvent
    public void onProcessingSurfaceProgress(ProcessingSurfaceEvent.Progress event) {
        if (event.progress == 1f) {
            event.player.addStat(BidsStats.MATERIAL_SCRAPED, Math.round(event.effort));
        }
    }

}
