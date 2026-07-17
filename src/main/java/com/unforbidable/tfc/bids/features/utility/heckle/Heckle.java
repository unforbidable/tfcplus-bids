package com.unforbidable.tfc.bids.features.utility.heckle;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonToolHead;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.handwork.render.HandworkToolItemRenderer;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import com.unforbidable.tfc.bids.features.utility.heckle.item.ItemHeckle;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BONE_HECKLE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BONE_KNIFE_BLADE;

@FeatureName("heckle")
public class Heckle extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(BONE_KNIFE_BLADE, ItemCommonToolHead::new);
        init.item(BONE_HECKLE, () -> new ItemHeckle(TFCItems.boneToolMaterial));
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new HandworkToolItemRenderer())
            .item(BidsItems.boneHeckle);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(new ItemStack(BidsItems.boneHeckle),
            BidsItems.boneKnifeHead, BidsItems.boneKnifeHead, TFCItems.resin, "materialBindingDecent");

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_KNIFE_HEAD)
                .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
                .cutout(Shape.rectFrom(4, 0).size(3, 1)) // right top
                .cutout(Shape.rectFrom(5, 1).size(2, 10)) // right middle
                .cutout(Shape.rectFrom(4, 11).size(3, 6)) // right bottom
                .cutout(Shape.triFrom(4, 0).size(-1, 1)) // right top corner
                .cutout(Shape.triFrom(5, 1).size(-1, 1)) // right lower corner
                .cutout(Shape.triFrom(5, 11).size(-1, -1)) // right lower corner
                .cutout(Shape.triFrom(2, 0).size(1, 1)) // right top corner
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingRecipe(WoodworkingPlanNames.PLAN_KNIFE_HEAD,
                new ItemStack(TFCItems.bone), new ItemStack(BidsItems.boneKnifeHead)));
    }

}
