package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class KilnRecipe {

    public final ItemStack input;
    public final int level;
    public final ItemStack result;

    public KilnRecipe(ItemStack input, int level, ItemStack result) {
        this.input = input;
        this.level = level;
        this.result = result;
    }

    public static RegistryActor<KilnRecipe> add(ItemStack input, int level, ItemStack result) {
        return new RegistryAddingActor<>(KilnRecipeStage.instance, new KilnRecipe(input, level, result));
    }

}
