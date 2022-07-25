package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeAction;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ActionDamageTool extends RecipeAction {

    protected final List<ItemStack> tools = new ArrayList<ItemStack>();
    protected final int damage;

    public ActionDamageTool(int damage) {
        this.damage = damage;
    }

    public ActionDamageTool addTools(String... toolOreNames) {
        for (String toolOreName : toolOreNames) {
            List<ItemStack> list = OreDictionary.getOres(toolOreName, false);
            if (list != null)
                tools.addAll(list);
        }
        return this;
    }

    @Override
    public void onItemCrafted(ItemCraftedEvent event) {
        super.onItemCrafted(event);

        findAndDamageTools(event);
    }

    private void findAndDamageTools(ItemCraftedEvent event) {
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                for (ItemStack is : tools) {
                    if (event.craftMatrix.getStackInSlot(i).getItem() == is.getItem()) {
                        Bids.LOG.debug("Found matching tool to be damaged: " + is.getDisplayName());
                        damageToolInSlot(event, i);
                    }
                }
            }
        }
    }

    private void damageToolInSlot(ItemCraftedEvent event, int i) {
        ItemStack isUsedTool = event.craftMatrix.getStackInSlot(i).copy();
        if (isUsedTool != null
                && damageTool(event, isUsedTool)
                && (isUsedTool.getItemDamage() != 0 || event.player.capabilities.isCreativeMode)) {
            event.craftMatrix.setInventorySlotContents(i, isUsedTool);
            int stackSize = event.craftMatrix.getStackInSlot(i).stackSize;
            stackSize = Math.min(stackSize + 1, 2);
            event.craftMatrix.getStackInSlot(i).stackSize = stackSize;

            Bids.LOG.debug("Tool was damaged");
        }
    }

    protected boolean damageTool(ItemCraftedEvent event, ItemStack isUsedTool) {
        isUsedTool.damageItem(damage, event.player);

        return true;
    }

}
