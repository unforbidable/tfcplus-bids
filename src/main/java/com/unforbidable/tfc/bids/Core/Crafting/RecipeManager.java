package com.unforbidable.tfc.bids.Core.Crafting;

import com.unforbidable.tfc.bids.Bids;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeManager {

    private static final List<ActionableRecipeBuilder> builders = new ArrayList<>();
    private static final List<ActionableRecipe> actionableRecipes = new ArrayList<>();
    private static final List<MatchingRecipe> removingRecipes = new ArrayList<>();
    private static final List<CloningRecipe> cloningRecipes = new ArrayList<>();

    public static void handleItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        CraftingContext context = new CraftingContext(event);

        for (ActionableRecipe actionableRecipe : getActionableRecipes()) {
            if (recipeMatchesEvent(actionableRecipe, event)) {
                actionableRecipe.action.accept(context);

                break;
            }
        }
    }

    private static boolean recipeMatchesEvent(ActionableRecipe actionableRecipe, PlayerEvent.ItemCraftedEvent event) {
        return actionableRecipe.recipe.matches((InventoryCrafting) event.craftMatrix, event.player.worldObj);
    }

    private static List<ActionableRecipe> getActionableRecipes() {
        if (!builders.isEmpty()) {
            // Collect any pending crafting recipe action builders
            actionableRecipes.addAll(builders.stream()
                .map(ActionableRecipeBuilder::build)
                .filter(r -> r.action != null)
                .collect(Collectors.toList()));

            builders.clear();
        }

        return actionableRecipes;
    }

    public static List<MatchingRecipe> getCurrentRecipes() {
        return getRegisteredRecipesUnchecked().stream()
            .filter(r -> removingRecipes.stream().noneMatch(r2 -> r2.recipe.instance == r))
            .filter(RecipeAccessor::isSupportedRecipe)
            .map(RecipeAccessor::of)
            .map(MatchingRecipe::of)
            .collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked" })
    private static List<IRecipe> getRegisteredRecipesUnchecked() {
        return CraftingManager.getInstance().getRecipeList();
    }

    public static void markForRemoval(MatchingRecipe recipe) {
        removingRecipes.add(recipe);
    }

    public static void submitCloningRecipe(CloningRecipe cloningRecipe) {
        cloningRecipes.add(cloningRecipe);
    }

    public static void flush() {
        // Add any cloned recipes pending registration
        cloningRecipes.forEach(RecipeManager::registerClonedRecipe);
        cloningRecipes.clear();

        // Remove any recipes marked for removal still pending
        removingRecipes.forEach(RecipeManager::removeExistingRecipe);
        removingRecipes.clear();
    }

    private static void removeExistingRecipe(MatchingRecipe matchingRecipe) {
        CraftingManager.getInstance().getRecipeList()
            .remove(matchingRecipe.recipe.instance);

        // Remove also the crafting recipe with action
        actionableRecipes.stream()
            .filter(r -> r.recipe == matchingRecipe.recipe.instance)
            .findFirst()
            .ifPresent(actionableRecipes::remove);

        Bids.LOG.info("Existing recipe was removed: {}", matchingRecipe.recipe);
    }

    private static void registerClonedRecipe(CloningRecipe cloningRecipe) {
        try {
            ActionableRecipe actionableRecipe = cloningRecipe.build();

            if (actionableRecipe.action != null) {
                actionableRecipes.add(actionableRecipe);
            }

            GameRegistry.addRecipe(actionableRecipe.recipe);

            Bids.LOG.info("Cloned recipe was added: {} from existing: {}", RecipeAccessor.of(actionableRecipe.recipe), cloningRecipe.recipe);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to clone recipe from {} due to error: {}", cloningRecipe.recipe, ex.getMessage(), ex);
        }
    }

    public static ActionableRecipeBuilder addRecipe(IRecipe recipe) {
        GameRegistry.addRecipe(recipe);

        ActionableRecipeBuilder builder = new ActionableRecipeBuilder(recipe);
        builders.add(builder);

        return builder;
    }

    public static ActionableRecipeBuilder addShapelessRecipe(ItemStack output, Object ...input) {
        return addRecipe(RecipeFactory.createShapeless(output, input));
    }

    public static ActionableRecipeBuilder addShapedRecipe(ItemStack output, Object ...input) {
        return addRecipe(RecipeFactory.createShaped(output, input));
    }

}
