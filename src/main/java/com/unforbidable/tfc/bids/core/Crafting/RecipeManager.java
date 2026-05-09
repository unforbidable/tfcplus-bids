package com.unforbidable.tfc.bids.core.crafting;

import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.inventory.InventoryCrafting;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    static final List<ActionableRecipe> actionableRecipes = new ArrayList<>();
    static RecipeManagerSession session = new RecipeManagerSession();

    public static void handleItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        CraftingContext context = new CraftingContext(event);

        for (ActionableRecipe actionableRecipe : actionableRecipes) {
            if (recipeMatchesEvent(actionableRecipe, event)) {
                actionableRecipe.action.accept(context);

                break;
            }
        }
    }

    private static boolean recipeMatchesEvent(ActionableRecipe actionableRecipe, PlayerEvent.ItemCraftedEvent event) {
        return actionableRecipe.recipe.matches((InventoryCrafting) event.craftMatrix, event.player.worldObj);
    }

    public static RecipeManagerSession getSession() {
        return session;
    }

}
