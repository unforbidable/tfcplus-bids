package com.unforbidable.tfc.bids.core.features.client.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiHandlerEntry;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiHiderEntry;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NeiRegistryHelper {

    public NeiRegistryHelper handler(TemplateRecipeHandler handler) {
        NeiRegistry.handlers.add(new NeiHandlerEntry(handler));

        return this;
    }

    public NeiRegistryHelper hide(ItemStack itemStack) {
        NeiRegistry.hiders.add(new NeiHiderEntry(itemStack));

        return this;
    }

    public NeiRegistryHelper hide(Item item) {
        return hide(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public NeiRegistryHelper hide(Item item, int damage) {
        return hide(new ItemStack(item, 1, damage));
    }

    public NeiRegistryHelper hide(Block block) {
        return hide(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
    }

    public NeiRegistryHelper hide(Block block, int meta) {
        return hide(new ItemStack(block, 1, meta));
    }

}
