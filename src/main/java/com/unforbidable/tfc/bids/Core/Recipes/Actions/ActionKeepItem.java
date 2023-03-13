package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeAction;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ActionKeepItem extends RecipeAction {

    protected final List<ItemStack> items = new ArrayList<ItemStack>();

    public ActionKeepItem addItems(String... itemOreNames) {
        for (String toolOreName : itemOreNames) {
            List<ItemStack> list = OreDictionary.getOres(toolOreName, false);
            if (list != null)
                items.addAll(list);
        }
        return this;
    }

    @Override
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        super.onItemCrafted(event);

        findAndIncreaseItemStackSize(event);
    }

    private void findAndIncreaseItemStackSize(PlayerEvent.ItemCraftedEvent event) {
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                for (ItemStack is : items) {
                    if (event.craftMatrix.getStackInSlot(i).getItem() == is.getItem()) {
                        Bids.LOG.info("Found matching item to be kept: " + event.craftMatrix.getStackInSlot(i).getDisplayName());
                        increaseItemStackSizeInSlot(event, i);
                    }
                }
            }
        }
    }

    private void increaseItemStackSizeInSlot(PlayerEvent.ItemCraftedEvent event, int i) {
        ItemStack isUsedTool = event.craftMatrix.getStackInSlot(i);
        if (isUsedTool != null) {
            int stackSize = event.craftMatrix.getStackInSlot(i).stackSize;
            stackSize++;
            event.craftMatrix.getStackInSlot(i).stackSize = stackSize;

            Bids.LOG.info("Item was kept");
        }
    }
}
