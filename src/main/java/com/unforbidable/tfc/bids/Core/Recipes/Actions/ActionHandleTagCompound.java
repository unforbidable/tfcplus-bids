package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Core.Recipes.RecipeAction;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ActionHandleTagCompound extends RecipeAction {

    List<Item> items = new ArrayList<Item>();

    public ActionHandleTagCompound() {
    }

    public ActionHandleTagCompound addHandledItem(Item item) {
        items.add(item);

        return this;
    }

    @Override
    public void onItemCrafted(ItemCraftedEvent event) {
        super.onItemCrafted(event);

        for (Item item : items) {
            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack is = event.craftMatrix.getStackInSlot(i);
                if (is != null && is.getItem() == item && is.hasTagCompound()) {
                    if (handleTagCompound(event.crafting, is)) {
                        break;
                    }
                }
            }
        }
    }

    // Note we can only handle NBT when a single item is removed
    // from the crafting output slot
    // When the player shift clicks to craft and transfer a whole stack
    // this does not work
    // TFC does this via the container's onCraftMatrixChanged event
    // but unfortunately we only have the forge's ItemCraftedEvent

    protected boolean handleTagCompound(ItemStack output, ItemStack ingredient) {
        return false;
    }

}
