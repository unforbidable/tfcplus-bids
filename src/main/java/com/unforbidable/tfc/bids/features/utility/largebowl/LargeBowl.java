package com.unforbidable.tfc.bids.features.utility.largebowl;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.utility.largebowl.item.ItemLargeBowlFluid;
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

import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_FRESH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.LARGE_BOWL_GOAT_MILK;
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

        init.item(LARGE_BOWL_FRESH_WATER, ItemLargeBowlFluid::new);
        init.item(LARGE_BOWL_SALT_WATER, ItemLargeBowlFluid::new);
        init.item(LARGE_BOWL_VINEGAR, ItemLargeBowlFluid::new);
        init.item(LARGE_BOWL_MILK, ItemLargeBowlFluid::new);
        init.item(LARGE_BOWL_HONEY, ItemLargeBowlFluid::new);
        init.item(LARGE_BOWL_GOAT_MILK, ItemLargeBowlFluid::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(TFCFluids.FRESHWATER)
            .container(BidsItems.freshWaterLargeBowl, 500, false, BidsItems.largeClayBowl, 1);
        setup.fluid(TFCFluids.SALTWATER)
            .container(BidsItems.saltWaterLargeBowl, 500, false, BidsItems.largeClayBowl, 1);
        setup.fluid(TFCFluids.VINEGAR)
            .container(BidsItems.vinegarLargeBowl, 500, false, BidsItems.largeClayBowl, 1);
        setup.fluid(TFCFluids.MILK)
            .container(BidsItems.milkLargeBowl, 500, false, BidsItems.largeClayBowl, 1);
        setup.fluid(TFCFluids.HONEY)
            .container(BidsItems.honeyLargeBowl, 500, false, BidsItems.largeClayBowl, 1);
        setup.fluid(BidsFluids.goatMilk)
            .container(BidsItems.goatMilkLargeBowl, 500, false, BidsItems.largeClayBowl, 1);

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
                "#####", " ### ", " ### ", "#   #", "#####", '#',
                new ItemStack(TFCItems.flatClay, 1, 1)));

        setup.registry(TfcRegistry.Recipes.kiln)
            .add(KilnRecipe.add(new ItemStack(BidsItems.largeClayBowl, 1, 0), 0,
                new ItemStack(BidsItems.largeClayBowl, 1, 1)));
    }

}
