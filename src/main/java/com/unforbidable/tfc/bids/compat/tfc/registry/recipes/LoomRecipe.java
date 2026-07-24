package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryRemovingActor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LoomRecipe {

    public final ItemStack input;
    public final ItemStack output;
    public final ResourceLocation resourceLocation;

    public LoomRecipe(ItemStack input, ItemStack output, ResourceLocation resourceLocation) {
        this.input = input;
        this.output = output;
        this.resourceLocation = resourceLocation;
    }

    public static RegistryAddingActor<LoomRecipe> add(ItemStack input, ItemStack output, ResourceLocation resourceLocation) {
        return new RegistryAddingActor<>(LoomRecipeStage.INSTANCE, new LoomRecipe(input, output, resourceLocation));
    }

    public static RegistryRemovingActor<LoomRecipe> remove(Item input) {
        return new RegistryRemovingActor<>(LoomRecipeStage.INSTANCE, r -> r.input.getItem() == input);
    }

}
