package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class SewingRecipe {

    public final ItemStack output;
    public final int[][][] pattern;
    public final ItemStack[] input;

    public SewingRecipe(ItemStack output, int[][][] pattern, ItemStack[] input) {
        this.output = output;
        this.pattern = pattern;
        this.input = input;
    }

    public static RegistryAddingActor<SewingRecipe> add(ItemStack output, int[][][] pattern, ItemStack ...input) {
        return new RegistryAddingActor<>(SewingStage.INSTANCE, new SewingRecipe(output, pattern, input));
    }

    public static RegistryAddingActor<SewingRecipe> addRepair(ItemStack output, ItemStack ...input) {
        return new RegistryAddingActor<>(SewingStage.INSTANCE, new SewingRecipe(output, null, input));
    }

}
