package com.unforbidable.tfc.bids.features.utility.scutchingknife;

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

@FeatureName("scutchingKnife")
public class ScutchingKnife extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.SCUTCHING_KNIFE, () -> new ItemCommonTool(TFCItems.woodToolMaterial));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemFlaxScutchingTool")
           .add(BidsItems.scutchingKnife);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_SCUTCHING_KNIFE)
                .cutout(Shape.rectFrom(8, 0).size(5, 25)) // right cut off
                .cutout(Shape.rectFrom(0, 0).size(5, 9)) // top 1/3 left cut off
                .cutout(Shape.rectFrom(0, 9).size(2, 16)) // bottom 2/3 left cut off
                .cutout(Shape.triFrom(2, 9).size(3, 3)) // top left corner
                .cutout(Shape.triFrom(2, 25).size(3, -3)) // bottom left corner
                .cutout(Shape.pointAt(4, 20)) // hole
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_SCUTCHING_KNIFE, "boardWood", new ItemStack(BidsItems.scutchingKnife)));
    }

}
