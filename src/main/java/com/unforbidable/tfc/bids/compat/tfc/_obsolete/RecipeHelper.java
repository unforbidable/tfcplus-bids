package com.unforbidable.tfc.bids.compat.tfc._obsolete;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.LoomManager;
import com.dunk.tfc.api.Crafting.LoomRecipe;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import com.unforbidable.tfc.bids.core.crafting.RecipeManager;
import com.unforbidable.tfc.bids.core.crafting.RecipeManagerSession;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.features.material.textile.TextileConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import static com.unforbidable.tfc.bids.core.crafting.actions.ToolBinding.toolBinding;

public class RecipeHelper {

//    public static void handleCompositeToolRecipes() {
//        try (RecipeManagerSession recipes = RecipeManager.getSession()) {
//            recipes.currentRecipeStream()
//                .filter(r -> r.output.isAny(getStoneToolOreNames()))
//                .forEach(r -> r.clone(BidsOptions.Crafting.removeOriginalStoneToolRecipes)
//                    .addInput("materialBinding")
//                    .action(toolBinding()));
//        }
//    }

    public static List<Integer> getStoneToolOreIds() {
        List<Integer> oreIds = new ArrayList<Integer>();
        for (String ore : getStoneToolOreNames()) {
            oreIds.add(OreDictionary.getOreID(ore));
        }
        return oreIds;
    }

    public static String[] getStoneToolOreNames() {
        return new String[]{"itemAxeStone", "itemHammerStone", "itemKnifeStone", "itemShovelStone", "itemHoeStone", "itemJavelinStone", "itemAdzeStone", "itemDrillStone"};
    }

    public static void applyCompositeToolBindingBonus(ItemStack tool, ItemStack binding) {
        float bindingBonus = getBestBindingBonus(binding);
        if (bindingBonus > 0) {
            AnvilManager.setDurabilityBuff(tool, bindingBonus);
        }
    }

    private static float getBestBindingBonus(ItemStack binding) {
        int poorOreId = OreDictionary.getOreID("materialBinding");
        int decentOreId = OreDictionary.getOreID("materialBindingDecent");
        int goodOreId = OreDictionary.getOreID("materialBindingStrong");

        int[] ids = OreDictionary.getOreIDs(binding);
        for (int id : ids) {
            if (id == goodOreId) {
                return 1f;
            }
        }

        for (int id : ids) {
            if (id == decentOreId) {
                return 0.5f;
            }
        }

        for (int id : ids) {
            if (id == poorOreId) {
                return 0.1f;
            }
        }

        return 0;
    }

    public static void handleSpindleSpinningRecipes() {
        if (TextileConfig.removeOriginalSpindleSpinningRecipes) {
            try (RecipeManagerSession recipes = RecipeManager.getSession()) {
                recipes.currentRecipeStream()
                    .filter(r -> r.input.contains(TFCItems.spindle))
                    .forEach(MatchingRecipe::remove);
            }
        }
    }

    public static void handleRopeMakingRecipes() {
        if (TextileConfig.removeOriginalRopeMakingRecipes) {
            try (RecipeManagerSession recipes = RecipeManager.getSession()) {
                recipes.currentRecipeStream()
                    .filter(r -> r.output.is(TFCItems.rope))
                    .forEach(MatchingRecipe::remove);
            }
        }
    }

    public static void handleLoomRecipes() {
        if (TextileConfig.removeOriginalBurlapFiberLoomRecipes) {
            List<LoomRecipe> recipes = LoomManager.getInstance().getRecipes();
            for (int i = 0; i < recipes.size(); i++) {
                LoomRecipe recipe = recipes.get(i);
                if (recipe.getInItem().getItem() == TFCItems.sisalFiber ||
                    recipe.getInItem().getItem() == TFCItems.juteFiber) {
                    recipes.remove(i--);
                    Bids.LOG.info("Original burlap from fiber loom recipe removed: " + recipe.getInItem().getDisplayName());
                }
            }
        }
    }

}
