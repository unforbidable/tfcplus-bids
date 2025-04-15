package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeAction;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeHelper;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ActionToolBinding extends RecipeAction {

    public ActionToolBinding() {
    }

    @Override
    public void onItemCrafted(ItemCraftedEvent event) {
        super.onItemCrafted(event);

        int bindingOreId = OreDictionary.getOreID("materialBinding");

        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            ItemStack is = event.craftMatrix.getStackInSlot(i);
            if (is != null) {
                for (int id : OreDictionary.getOreIDs(is)) {
                    if (id == bindingOreId) {
                        RecipeHelper.applyCompositeToolBindingBonus(event.crafting, is);
                    }
                }
            }
        }

        if (AnvilManager.getDurabilityBuff(event.crafting) == 0) {
            Bids.LOG.warn("Composite tool was crafted without binding that matches predefined ORE lists");
        }
    }

}
