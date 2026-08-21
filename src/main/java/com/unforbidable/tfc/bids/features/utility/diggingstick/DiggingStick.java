package com.unforbidable.tfc.bids.features.utility.diggingstick;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsToolMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
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
import com.unforbidable.tfc.bids.features.utility.diggingstick.item.ItemDiggingStick;
import net.minecraft.item.ItemStack;

@FeatureName("diggingStick")
public class DiggingStick extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.DIGGING_STICK, () -> new ItemDiggingStick(TFCItems.woodToolMaterial));
        init.item(ItemNames.HARDENED_DIGGING_STICK, () -> new ItemDiggingStick(BidsToolMaterial.hardenedWood));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        ItemDiggingStick.effectiveAgainstBlocks.addAll(Sets.newHashSet(
            TFCBlocks.dirt, TFCBlocks.dirt2,
            TFCBlocks.grass, TFCBlocks.grass2,
            TFCBlocks.dryGrass, TFCBlocks.dryGrass2,
            TFCBlocks.clay, TFCBlocks.clay2,
            TFCBlocks.clayGrass, TFCBlocks.clayGrass2
        ));

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_DIGGING_STICK)
                .cutout(Shape.triFrom(0, 20).size(2, -2)) // top left corner
                .cutout(Shape.triFrom(4, 20).size(-2, -2)) // top right corner
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_DIGGING_STICK, "poleWood", new ItemStack(BidsItems.diggingStick)));

        setup.registry(TfcRegistry.Heat.values)
            .add(HeatValue.add(new ItemStack(BidsItems.diggingStick), 1, 100, new ItemStack(BidsItems.hardenedDiggingStick)));
    }

}
