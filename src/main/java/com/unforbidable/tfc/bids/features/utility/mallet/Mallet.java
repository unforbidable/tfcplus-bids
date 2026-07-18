package com.unforbidable.tfc.bids.features.utility.mallet;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
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

import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_MALLET;

@FeatureName("mallet")
public class Mallet extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WOODEN_MALLET, () -> new ItemCommonTool(TFCItems.woodToolMaterial));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemFlaxBreakingTool")
            .add(BidsItems.woodenMallet);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_MALLET)
                .cutout(Shape.rectFrom(0, 0).size(5, 9)) // top 1/3 left cut off
                .cutout(Shape.rectFrom(8, 0).size(5, 9)) // top 1/3 right cut off
                .cutout(Shape.rectFrom(0, 9).size(2, 16)) // bottom 2/3 left cut off
                .cutout(Shape.rectFrom(11, 9).size(2, 16)) // bottom 2/3 right cut off
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_MALLET, "logWood", new ItemStack(BidsItems.woodenMallet)));
    }

}
