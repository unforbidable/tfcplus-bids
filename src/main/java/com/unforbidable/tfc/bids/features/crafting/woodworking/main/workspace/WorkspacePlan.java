package com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingPlan;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import java.awt.geom.Area;
import net.minecraft.item.ItemStack;

public class WorkspacePlan {

    private final WoodworkingPlan plan;
    private final WoodworkingRecipe recipe;
    private final ItemStack itemStack;

    public WorkspacePlan(WoodworkingPlan plan, WoodworkingRecipe recipe, ItemStack itemStack) {
        this.plan = plan;
        this.recipe = recipe;
        this.itemStack = itemStack;
    }

    public String getName() {
        return recipe.getPlanName();
    }

    public ItemStack getResult() {
        return recipe.getResult(itemStack);
    }

    public Area getCutout() {
        return plan.getCutout();
    }

    public boolean matches(Area cutout) {
        return plan.matches(cutout);
    }

}
