package com.unforbidable.tfc.bids.core.crafting.actions;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.function.Consumer;

public class DamageTool {

    protected final List<ItemStack> tools;
    protected final int damage;

    protected DamageTool(String oreName, int damage) {
        this.tools = OreDictionary.getOres(oreName, false);
        this.damage = damage;
    }

    public static Consumer<CraftingContext> damageTool(String oreName) {
        return damageTool(oreName, 1);
    }

    public static Consumer<CraftingContext> damageTool(String oreName, int damage) {
        return context -> new DamageTool(oreName, damage)
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        findAndDamageTools(context.event);
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
        ItemStack tool = event.craftMatrix.getStackInSlot(i);
        if (damageToolItem(event, tool) && (tool.getItemDamage() != 0 || event.player.capabilities.isCreativeMode)) {
            int stackSize = event.craftMatrix.getStackInSlot(i).stackSize;
            stackSize = Math.min(stackSize + 1, 2);
            event.craftMatrix.getStackInSlot(i).stackSize = stackSize;

            Bids.LOG.debug("Tool was damaged");
        }
    }

    protected boolean damageToolItem(ItemCraftedEvent event, ItemStack tool) {
        tool.damageItem(damage, event.player);

        return true;
    }

}
