package com.unforbidable.tfc.bids.Core.Crafting.Actions;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crafting.CraftingContext;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeHelper;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.Consumer;

public class ToolBinding {

    public static Consumer<CraftingContext> toolBinding() {
        return context -> new ToolBinding()
            .onItemCrafted(context);
    }

    public void onItemCrafted(CraftingContext context) {
        ItemCraftedEvent event = context.event;

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
