package com.unforbidable.tfc.bids.Handlers;

import java.util.List;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingHandler {

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent e) {
        if (e.crafting.getItem() == BidsItems.oreBit) {
            int toolDamage = 0;
            int baseDamage = CrucibleHelper.isOreIron(e.crafting) ? 10 : 1;
            for (int i = 0; i < e.craftMatrix.getSizeInventory(); i++) {
                if (e.craftMatrix.getStackInSlot(i) != null) {
                    ItemStack isIngred = e.craftMatrix.getStackInSlot(i);
                    if (isIngred.getItem() == TFCItems.smallOreChunk) {
                        toolDamage += baseDamage; // Small
                    } else if (isIngred.getItem() == TFCItems.oreChunk) {
                        if (isIngred.getItemDamage() < Global.oreGrade1Offset) {
                            toolDamage += baseDamage * 3; // Normal
                        } else if (isIngred.getItemDamage() < Global.oreGrade2Offset) {
                            toolDamage += baseDamage * 4; // Rich
                        } else {
                            toolDamage += baseDamage * 2; // Poor
                        }
                    }
                }
            }

            damageTool(e, toolDamage, "itemHammer");
        }

        if (e.crafting.getItem() == Item.getItemFromBlock(BidsBlocks.fireClayCrucible)) {
            CrucibleHelper.triggerCrucibleAchievement(e.player);
        }
    }

    private void damageTool(ItemCraftedEvent e, int toolDamage, String oreName) {
        List<ItemStack> tools = OreDictionary.getOres(oreName, false);
        for (int i = 0; i < e.craftMatrix.getSizeInventory(); i++) {
            if (e.craftMatrix.getStackInSlot(i) != null) {
                for (ItemStack is : tools) {
                    if (e.craftMatrix.getStackInSlot(i).getItem() == is.getItem()) {
                        ItemStack isUsedTool = e.craftMatrix.getStackInSlot(i).copy();
                        if (isUsedTool != null) {
                            // The more ore bits we create, the more the hammer is damaged
                            isUsedTool.damageItem(toolDamage, e.player);
                            if (isUsedTool.getItemDamage() != 0 || e.player.capabilities.isCreativeMode) {
                                e.craftMatrix.setInventorySlotContents(i, isUsedTool);
                                int stackSize = e.craftMatrix.getStackInSlot(i).stackSize;
                                stackSize = Math.min(stackSize + 1, 2);
                                e.craftMatrix.getStackInSlot(i).stackSize = stackSize;
                            }
                        }
                    }
                }
            }
        }
    }

}
