package com.unforbidable.tfc.bids.features.utility.largebowl;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemLargeBowlFluid;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.utility.largebowl.item.ItemLargeBowl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_FRESH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_HONEY;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_MILK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_SALT_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_VINEGAR;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

@FeatureName("largeBowl")
public class LargeBowl extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(LARGE_BOWL, ItemLargeBowl::new);

        init.item(LARGE_BOWL_FRESH_WATER, ItemLargeBowlFluid::new)
            .meta("Pottery")
            .container(() -> lookup.item(LARGE_BOWL), 1)
            .fluid(500, TFCFluids.FRESHWATER);
        init.item(LARGE_BOWL_SALT_WATER, ItemLargeBowlFluid::new)
            .meta("Pottery")
            .container(() -> lookup.item(LARGE_BOWL), 1)
            .fluid(500, TFCFluids.SALTWATER);
        init.item(LARGE_BOWL_VINEGAR, ItemLargeBowlFluid::new)
            .meta("Pottery")
            .container(() -> lookup.item(LARGE_BOWL), 1)
            .fluid(500, TFCFluids.VINEGAR);
        init.item(LARGE_BOWL_MILK, ItemLargeBowlFluid::new)
            .meta("Pottery")
            .container(() -> lookup.item(LARGE_BOWL), 1)
            .fluid(500, TFCFluids.MILK);
        init.item(LARGE_BOWL_HONEY, ItemLargeBowlFluid::new)
            .meta("Pottery")
            .container(() -> lookup.item(LARGE_BOWL), 1)
            .fluid(500, TFCFluids.HONEY);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemMilkingContainer")
            .add(new ItemStack(BidsItems.largeClayBowl, 1, 1));

        // Used for mixing flour and water into unshaped dough or flatbread dough
        setup.ores("itemLargeBowlWater")
            .add(BidsItems.freshWaterLargeBowl);

        setup.ores("itemHoneycomb")
            .add(TFCItems.honeycomb)
            .add(TFCItems.fertileHoneycomb);

        setup.recipes().addShapeless(new ItemStack(BidsItems.honeyLargeBowl),
                "itemHoneycomb", "itemHoneycomb", "itemKnife", new ItemStack(BidsItems.largeClayBowl, 1, 1))
            .action(damageTool("itemKnife"))
            .action(extraDrop(new ItemStack(TFCItems.emptyHoneycomb, 2)));

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.largeClayBowl),
                new Object[]{"#####", " ### ", " ### ", "#   #", "#####", '#',
                    new ItemStack(TFCItems.flatClay, 1, 1)}));

        setup.registry(TfcRegistry.Recipes.kiln)
            .add(KilnRecipe.add(new ItemStack(BidsItems.largeClayBowl, 1, 0), 0,
                new ItemStack(BidsItems.largeClayBowl, 1, 1)));
    }

}
