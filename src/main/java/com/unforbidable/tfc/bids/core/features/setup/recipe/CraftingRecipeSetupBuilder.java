package com.unforbidable.tfc.bids.core.features.setup.recipe;

import com.unforbidable.tfc.bids.core.crafting.ActionableRecipeBuilder;
import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import com.unforbidable.tfc.bids.core.crafting.RecipeFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftingRecipeSetupBuilder {

    private final List<ActionableRecipeBuilder> builders = new ArrayList<>();
    private final List<MatchSpecBuilder> matchers = new ArrayList<>();

    public ActionableRecipeBuilder add(IRecipe recipe) {
        ActionableRecipeBuilder builder = new ActionableRecipeBuilder(recipe);
        builders.add(builder);

        return builder;
    }

    public ActionableRecipeBuilder addShapeless(ItemStack output, Object ...input) {
        return add(RecipeFactory.createShapeless(output, input));
    }

    public ActionableRecipeBuilder addShaped(ItemStack output, Object ...input) {
        return add(RecipeFactory.createShaped(output, input));
    }

    public MatchSpecBuilder match(Predicate<MatchingRecipe> match) {
        MatchSpecBuilder builder = new MatchSpecBuilder(match);
        matchers.add(builder);

        return builder;
    }

    public CraftingRecipeSetup build() {
        return new CraftingRecipeSetup(
            builders.stream()
                .map(ActionableRecipeBuilder::build)
                .collect(Collectors.toList()),
            matchers.stream()
                .map(MatchSpecBuilder::build)
                .collect(Collectors.toList())
        );
    }

}
