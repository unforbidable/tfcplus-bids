package com.unforbidable.tfc.bids.features.utility.shovel;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsToolMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonShovel;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import com.unforbidable.tfc.bids.features.utility.shovel.item.ItemPrimitiveShovel;
import net.minecraft.item.ItemStack;

@FeatureName("shovel")
public class Shovel extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.WOODEN_SHOVEL, () -> new ItemPrimitiveShovel(TFCItems.woodToolMaterial));
        init.item(ItemNames.HARDENED_WOODEN_SHOVEL, () -> new ItemPrimitiveShovel(BidsToolMaterial.hardenedWood));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Primitive wooden shovel doesn't have "shovel" harvest tool class
        // instead it is effective only on select blocks
        // This makes digging stick preferable for harvesting certain blocks
        ItemPrimitiveShovel.effectiveAgainstBlocks.addAll(Sets.newHashSet(
            TFCBlocks.sand, TFCBlocks.sand2,
            TFCBlocks.gravel, TFCBlocks.gravel2,
            TFCBlocks.peat, TFCBlocks.peatGrass
        ));

        setup.ores("itemShovel")
            .add(BidsItems.woodenShovel);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_SHOVEL)
                .cutout(Shape.rectFrom(0, 0).size(5, 17)) // top 2/3 left cut off
                .cutout(Shape.rectFrom(8, 0).size(5, 17)) // top 2/3 right cut off
                .cutout(Shape.rectFrom(0, 17).size(3, 8)) // bottom 1/3 left cut off
                .cutout(Shape.rectFrom(10, 17).size(3, 8)) // bottom 1/3 right cut off
                .cutout(Shape.triFrom(3, 25).size(2, -2)) // bottom left corner
                .cutout(Shape.triFrom(10, 25).size(-2, -2)) // bottom right corner
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_SHOVEL, "boardWood", new ItemStack(BidsItems.woodenShovel)));

        setup.registry(TfcRegistry.Heat.values)
            .add(HeatValue.add(new ItemStack(BidsItems.woodenShovel), 1, 100, new ItemStack(BidsItems.hardenedWoodenShovel)));
    }

}
