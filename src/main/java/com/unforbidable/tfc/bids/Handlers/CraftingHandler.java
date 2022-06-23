package com.unforbidable.tfc.bids.Handlers;

import java.util.List;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingHandler {

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent e) {
        IInventory iinventory = e.craftMatrix;
        if (iinventory != null) {
            ItemStack isOutput = e.crafting;
            Item itemOutput = isOutput.getItem();
            if (itemOutput == BidsItems.oreBit) {
                int toolDamage = 0;
                int baseDamage = CrucibleHelper.isOreIron(isOutput) ? 10 : 1;
                for (int i = 0; i < iinventory.getSizeInventory(); i++) {
                    if (iinventory.getStackInSlot(i) != null) {
                        ItemStack isIngred = iinventory.getStackInSlot(i);
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

                List<ItemStack> tools = OreDictionary.getOres("itemHammer", false);
                for (int i = 0; i < iinventory.getSizeInventory(); i++) {
                    if (iinventory.getStackInSlot(i) != null) {
                        for (ItemStack is : tools) {
                            if (iinventory.getStackInSlot(i).getItem() == is.getItem()) {
                                ItemStack isUsedTool = iinventory.getStackInSlot(i).copy();
                                if (isUsedTool != null) {
                                    // The more ore bits we create, the more the hammer is damaged
                                    isUsedTool.damageItem(toolDamage, e.player);
                                    if (isUsedTool.getItemDamage() != 0 || e.player.capabilities.isCreativeMode) {
                                        iinventory.setInventorySlotContents(i, isUsedTool);
                                        int stackSize = iinventory.getStackInSlot(i).stackSize;
                                        stackSize = Math.min(stackSize + 1, 2);
                                        iinventory.getStackInSlot(i).stackSize = stackSize;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
        if (event.crafting.getItem() == Item.getItemFromBlock(BidsBlocks.fireClayCrucible)) {
            CrucibleHelper.triggerCrucibleAchievement(event.player);
        }
    }

}
