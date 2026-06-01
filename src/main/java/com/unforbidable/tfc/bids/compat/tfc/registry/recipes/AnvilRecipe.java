package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class AnvilRecipe {

    public final ItemStack input;
    public final ItemStack input2;
    public final String plan;
    public final int req;
    public final ItemStack output;
    public final String skill;
    public final boolean welding;

    public AnvilRecipe(ItemStack input, ItemStack input2, String plan, int req, ItemStack output, String skill, boolean welding) {
        this.input = input;
        this.input2 = input2;
        this.plan = plan;
        this.req = req;
        this.output = output;
        this.skill = skill;
        this.welding = welding;
    }

    public static RegistryActor<AnvilRecipe> add(ItemStack input, ItemStack input2, String plan, int req, ItemStack output, String skill) {
        return new RegistryAddingActor<>(AnvilRecipeStage.instance, new AnvilRecipe(input, input2, plan, req, output, skill, false));
    }

    public static RegistryActor<AnvilRecipe> addWeld(ItemStack input, ItemStack input2, int req, ItemStack output) {
        return new RegistryAddingActor<>(AnvilRecipeStage.instance, new AnvilRecipe(input, input2, null, req, output, null, true));
    }

}
