package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class KnappingRecipe {

    public final ItemStack output;
    public final Object[] input;

    public KnappingRecipe(ItemStack output, Object[] input) {
        this.output = output;
        this.input = input;
    }

    public static RegistryActor<KnappingRecipe> add(ItemStack output, Object[] input) {
        return new RegistryAddingActor<>(KnappingRecipeStage.instance, new KnappingRecipe(output, input));
    }

}
