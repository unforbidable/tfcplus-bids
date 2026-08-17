package com.unforbidable.tfc.bids.core.crafting.actions;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DamageTool {

    protected final List<ItemStack> tools;
    protected final int damage;
    protected final Function<ItemStack, Integer> damageProvider;

    protected DamageTool(String oreName, int damage, Function<ItemStack, Integer> damageProvider) {
        this.tools = OreDictionary.getOres(oreName, false);
        this.damage = damage;
        this.damageProvider = damageProvider;
    }

    public static Consumer<CraftingContext> damageTool(String oreName) {
        return damageTool(oreName, 1);
    }

    public static Consumer<CraftingContext> damageTool(String oreName, int damage) {
        return context -> new DamageTool(oreName, damage, null)
            .onItemCrafted(context);
    }

    public static Consumer<CraftingContext> damageTool(String oreName, Function<ItemStack, Integer> damageProvider) {
        return context -> new DamageTool(oreName, 0, damageProvider)
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        findAndDamageTools(context.event);
    }

    private void findAndDamageTools(ItemCraftedEvent event) {
        int actualDamage = damage;

        if (damageProvider != null) {
            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                if (event.craftMatrix.getStackInSlot(i) != null) {
                    actualDamage += damageProvider.apply(event.craftMatrix.getStackInSlot(i));
                }
            }
        }

        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                for (ItemStack is : tools) {
                    if (event.craftMatrix.getStackInSlot(i).getItem() == is.getItem()) {
                        Bids.LOG.debug("Found matching tool to be damaged: " + is.getDisplayName());
                        damageToolInSlot(event, i, actualDamage);
                    }
                }
            }
        }
    }

    private void damageToolInSlot(ItemCraftedEvent event, int i, int actualDamage) {
        ItemStack tool = event.craftMatrix.getStackInSlot(i);
        if (damageToolItem(event, tool, actualDamage) && (tool.getItemDamage() != 0 || event.player.capabilities.isCreativeMode)) {
            int stackSize = event.craftMatrix.getStackInSlot(i).stackSize;
            stackSize = Math.min(stackSize + 1, 2);
            event.craftMatrix.getStackInSlot(i).stackSize = stackSize;

            Bids.LOG.debug("Tool was damaged");
        }
    }

    protected boolean damageToolItem(ItemCraftedEvent event, ItemStack tool, int actualDamage) {
        tool.damageItem(actualDamage, event.player);

        return true;
    }

}
