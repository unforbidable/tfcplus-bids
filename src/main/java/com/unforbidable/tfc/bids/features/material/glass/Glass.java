package com.unforbidable.tfc.bids.features.material.glass;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemDrinkingGlass;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

/**
 * <li> drinking glass - 250 mb glass container for drinking</li>
 * <li> shot glass - 50 mb glass container for drinking</li>
 * <li> glass jug - 2000 mb glass container for drinking</li>
 */
@FeatureName("glass")
public class Glass extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.DRINKING_GLASS, ItemDrinkingGlass::new)
            .drink(250, false)
            .overlays(0, 40, 80, 100)
            .apply(i -> i.setGlassReturnAmount(40));
        init.item(ItemNames.SHOT_GLASS, ItemDrinkingGlass::new)
            .drink(50, false)
            .apply(i -> i.setGlassReturnAmount(20));
        init.item(ItemNames.GLASS_JUG, ItemDrinkingGlass::new)
            .drink(2000, false)
            .overlays(0, 11, 22, 33, 44, 55, 66, 77, 88, 100)
            .apply(i -> i.setGlassReturnAmount(80));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .hide(BidsItems.flatGlass);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.drinkingGlass, 2),
                "     ", "     ", "#   #", "#   #", "#####", '#', BidsItems.flatGlass))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.shotGlass, 4),
                "     ", "     ", " # # ", " # # ", " ### ", '#', BidsItems.flatGlass))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.glassJug, 1),
                " #   ", "# ## ", "# # #", "# ## ", "###  ", '#', BidsItems.flatGlass));
    }

}
