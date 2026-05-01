package com.unforbidable.tfc.bids.Core.Crafting;

import com.unforbidable.tfc.bids.Bids;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeManagerSession implements AutoCloseable {

    private final List<ActionableRecipeBuilder> builders = new ArrayList<>();
    private final List<MatchingRecipe> removingRecipes = new ArrayList<>();
    private final List<CloningRecipe> cloningRecipes = new ArrayList<>();

    public ActionableRecipeBuilder addRecipe(IRecipe recipe) {
        GameRegistry.addRecipe(recipe);

        ActionableRecipeBuilder builder = new ActionableRecipeBuilder(recipe);
        builders.add(builder);

        return builder;
    }

    public ActionableRecipeBuilder addShapelessRecipe(ItemStack output, Object ...input) {
        return addRecipe(RecipeFactory.createShapeless(output, input));
    }

    public ActionableRecipeBuilder addShapedRecipe(ItemStack output, Object ...input) {
        return addRecipe(RecipeFactory.createShaped(output, input));
    }

    public Stream<MatchingRecipe> currentRecipeStream() {
        return getRegisteredRecipesUnchecked().stream()
            .filter(r -> removingRecipes.stream().noneMatch(r2 -> r2.recipe.instance == r))
            .filter(RecipeAccessor::isSupportedRecipe)
            .map(RecipeAccessor::of)
            .map(MatchingRecipe::of);
    }

    @SuppressWarnings({"unchecked" })
    private List<IRecipe> getRegisteredRecipesUnchecked() {
        return CraftingManager.getInstance().getRecipeList();
    }

    void markForRemoval(MatchingRecipe recipe) {
        removingRecipes.add(recipe);
    }

    void submitCloningRecipe(CloningRecipe cloningRecipe) {
        cloningRecipes.add(cloningRecipe);
    }

    public void flush() {
        if (!builders.isEmpty()) {
            // Collect any pending crafting recipe action builders
            RecipeManager.actionableRecipes.addAll(builders.stream()
                .map(ActionableRecipeBuilder::build)
                .filter(r -> r.action != null)
                .collect(Collectors.toList()));

            builders.clear();
        }

        if (!cloningRecipes.isEmpty()) {
            // Add any cloned recipes pending registration
            cloningRecipes.forEach(this::registerClonedRecipe);
            cloningRecipes.clear();
        }

        if (!removingRecipes.isEmpty()) {
            // Remove any recipes marked for removal still pending
            removingRecipes.forEach(this::removeExistingRecipe);
            removingRecipes.clear();
        }
    }

    private void removeExistingRecipe(MatchingRecipe matchingRecipe) {
        CraftingManager.getInstance().getRecipeList()
            .remove(matchingRecipe.recipe.instance);

        // Remove also the crafting recipe with action
        RecipeManager.actionableRecipes.stream()
            .filter(r -> r.recipe == matchingRecipe.recipe.instance)
            .findFirst()
            .ifPresent(RecipeManager.actionableRecipes::remove);

        Bids.LOG.info("Existing recipe removed: {}", matchingRecipe.recipe.getOutput());
        Bids.LOG.debug("{}", matchingRecipe.recipe);
    }

    private void registerClonedRecipe(CloningRecipe cloningRecipe) {
        try {
            ActionableRecipe actionableRecipe = cloningRecipe.build();

            if (actionableRecipe.action != null) {
                RecipeManager.actionableRecipes.add(actionableRecipe);
            }

            GameRegistry.addRecipe(actionableRecipe.recipe);

            Bids.LOG.info("Cloned recipe added: {}", cloningRecipe.recipe.getOutput());
            Bids.LOG.debug("{} -> {}", RecipeAccessor.of(actionableRecipe.recipe), cloningRecipe.recipe);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to clone recipe from {} due to error: {}", cloningRecipe.recipe, ex.getMessage(), ex);
        }
    }

    @Override
    public void close() {
        flush();
    }

}
