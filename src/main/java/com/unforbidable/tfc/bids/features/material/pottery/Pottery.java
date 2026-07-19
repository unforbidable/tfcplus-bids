package com.unforbidable.tfc.bids.features.material.pottery;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonPottery;
import com.unforbidable.tfc.bids.common.item.ItemDrinkingPottery;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import net.minecraft.item.ItemStack;

/**
 * <li>clay pipe - currently only used for crafting a mud brick chimney</li>
 * <li>clay mug - 200 mB container for drinking</li>
 */
@FeatureName("pottery")
public class Pottery extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.CLAY_PIPE, ItemCommonPottery::new);

        init.item(ItemNames.CLAY_MUG, ItemDrinkingPottery::new)
            .drink(200, true)
            .overlays(0, 100);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(new ItemStack(BidsItems.clayPipe),
            TFCItems.clayTile, TFCItems.clayTile);

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.clayMug, 2),
                "#####", "#####", "    #", "   # ", "    #",
                '#', new ItemStack(TFCItems.flatClay, 1, 1)));

        setup.registry(TfcRegistry.Recipes.kiln)
            .add(KilnRecipe.add(new ItemStack(BidsItems.clayPipe, 1, 0), 0,
                new ItemStack(BidsItems.clayPipe, 1, 1)))
            .add(KilnRecipe.add(new ItemStack(BidsItems.clayMug, 1, 0), 0,
                new ItemStack(BidsItems.clayMug, 1, 1)));
    }

}
