package com.unforbidable.tfc.bids.features.device.dryingrack;

import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.SimpleRecipeRegistry;
import net.minecraft.item.ItemStack;

public class DryingRackRegistry {

    public static final ListRegistry<DryingRackTyingEquipment> tyingEquipment = new ListRegistry<>();
    public static final SimpleRecipeRegistry<DryingRackRecipe, ItemStack> recipes = new SimpleRecipeRegistry<>();

}
