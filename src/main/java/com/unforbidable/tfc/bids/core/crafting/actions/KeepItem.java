package com.unforbidable.tfc.bids.core.crafting.actions;

import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class KeepItem {

    protected final List<ItemStack> items;

    protected KeepItem(String oreName) {
        this.items = OreDictionary.getOres(oreName, false);
    }

    public static Consumer<CraftingContext> keepItem(String oreName) {
        return context -> new KeepItem(oreName)
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        findAndIncreaseItemStackSize(context.event);
    }

    private void findAndIncreaseItemStackSize(PlayerEvent.ItemCraftedEvent event) {
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                for (ItemStack is : items) {
                    if (OreDictionary.itemMatches(event.craftMatrix.getStackInSlot(i), is, false)) {
                        event.craftMatrix.getStackInSlot(i).stackSize = event.craftMatrix.getStackInSlot(i).stackSize + 1;
                        break;
                    }
                }
            }
        }
    }

}
