package com.unforbidable.tfc.bids.features.utility.smoother;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonTool;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import net.minecraft.item.ItemStack;

@FeatureName("Smoother")
public class Smoother extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.BONE_SMOOTHER, () -> new ItemCommonTool(TFCItems.boneToolMaterial));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemLeatherSmoothingTool")
            .add(BidsItems.boneSmoother);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_BONE_SMOOTHER)
                .cutout(Shape.from(0, 17).to(2, 17)
                    .to(2, 10).to(1, 9)
                    .to(1, 6).to(0, 5).build())
                .cutout(Shape.triFrom(0, 0).size(2, 2))
                .cutout(Shape.from(7, 17).to(5, 17)
                    .to(5, 10).to(6, 9)
                    .to(6, 6).to(7, 5).build())
                .cutout(Shape.triFrom(7, 0).size(-2, 2))
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_BONE_SMOOTHER, "materialBone", new ItemStack(BidsItems.boneSmoother)));
    }

}
