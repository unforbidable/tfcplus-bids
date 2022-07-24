package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.ItemStack;

public class ActionToolDamageOreBit extends ActionDamageTool {

    public ActionToolDamageOreBit() {
        super(1);
    }

    @Override
    protected boolean damageTool(ItemCraftedEvent event, ItemStack isUsedTool) {
        int accumulatedDamage = 0;
        int baseDamage = CrucibleHelper.isOreIron(event.crafting) ? 10 : 1;
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                ItemStack isIngred = event.craftMatrix.getStackInSlot(i);
                if (isIngred.getItem() == TFCItems.smallOreChunk) {
                    accumulatedDamage += baseDamage; // Small
                } else if (isIngred.getItem() == TFCItems.oreChunk) {
                    if (isIngred.getItemDamage() < Global.oreGrade1Offset) {
                        accumulatedDamage += baseDamage * 3; // Normal
                    } else if (isIngred.getItemDamage() < Global.oreGrade2Offset) {
                        accumulatedDamage += baseDamage * 4; // Rich
                    } else {
                        accumulatedDamage += baseDamage * 2; // Poor
                    }
                }
            }
        }

        isUsedTool.damageItem(accumulatedDamage, event.player);

        return true;
    }

}
