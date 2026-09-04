package com.unforbidable.tfc.bids.features.utility.flintgear;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsToolMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonKnife;
import com.unforbidable.tfc.bids.common.item.ItemCommonToolHead;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.main.FlintKnappingPlans;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import net.minecraft.item.ItemStack;

@FeatureName("flintGear")
public class FlintGear extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.FLINT_KNIFE_BLADE, () -> new ItemCommonToolHead(BidsToolMaterial.flint));

        init.item(ItemNames.FLINT_KNIFE, () -> new ItemCommonKnife(BidsToolMaterial.flint, BidsToolMaterial.flint.getDamageVsEntity() * 0.75f))
            .harvest("knife", 1)
            .apply(i -> i.setSlashDamageShape("1X4").setPierceDamageShape("1X2").setAttackSpeed(5));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemKnife")
            .add(BidsItems.flintKnife);

        setup.ores("itemScrapingTool")
            .add(BidsItems.flintKnife);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_A)
                .cutout(Shape.rectFrom(13, 0).size(-7, 17)) // left
                .cutout(Shape.rectFrom(4, 0).size(-4, 1)) // right top
                .cutout(Shape.rectFrom(3, 1).size(-3, 10)) // right middle
                .cutout(Shape.rectFrom(4, 11).size(-4, 6)) // right bottom
                .cutout(Shape.triFrom(4, 0).size(1, 1)) // right top corner
                .cutout(Shape.triFrom(3, 1).size(1, 1)) // right lower corner
                .cutout(Shape.triFrom(3, 11).size(1, -1)) // right lower corner
                .cutout(Shape.triFrom(6, 0).size(-1, 1)) // right top corner
                .build())
            .add(Plan.create(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_B)
                .cutout(Shape.rectFrom(0, 0).size(7, 17)) // left
                .cutout(Shape.rectFrom(9, 0).size(4, 1)) // right top
                .cutout(Shape.rectFrom(10, 1).size(3, 10)) // right middle
                .cutout(Shape.rectFrom(9, 11).size(4, 6)) // right bottom
                .cutout(Shape.triFrom(9, 0).size(-1, 1)) // right top corner
                .cutout(Shape.triFrom(10, 1).size(-1, 1)) // right lower corner
                .cutout(Shape.triFrom(10, 11).size(-1, -1)) // right lower corner
                .cutout(Shape.triFrom(7, 0).size(1, 1)) // right top corner
                .build())
            .add(Plan.create(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_2)
                .cutout(Shape.rectFrom(6, 0).size(1, 17)) // left
                .cutout(Shape.rectFrom(4, 0).size(-4, 1)) // right top
                .cutout(Shape.rectFrom(3, 1).size(-3, 10)) // right middle
                .cutout(Shape.rectFrom(4, 11).size(-4, 6)) // right bottom
                .cutout(Shape.triFrom(4, 0).size(1, 1)) // right top corner
                .cutout(Shape.triFrom(3, 1).size(1, 1)) // right lower corner
                .cutout(Shape.triFrom(3, 11).size(1, -1)) // right lower corner
                .cutout(Shape.triFrom(6, 0).size(-1, 1)) // right top corner
                .cutout(Shape.rectFrom(9, 0).size(4, 1)) // right top
                .cutout(Shape.rectFrom(10, 1).size(3, 10)) // right middle
                .cutout(Shape.rectFrom(9, 11).size(4, 6)) // right bottom
                .cutout(Shape.triFrom(9, 0).size(-1, 1)) // right top corner
                .cutout(Shape.triFrom(10, 1).size(-1, 1)) // right lower corner
                .cutout(Shape.triFrom(10, 11).size(-1, -1)) // right lower corner
                .cutout(Shape.triFrom(7, 0).size(1, 1)) // right top corner
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_A, "materialFlintCore", new ItemStack(BidsItems.flintKnifeBlade)))
            .add(new WoodworkingOreRecipe(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_B, "materialFlintCore", new ItemStack(BidsItems.flintKnifeBlade)))
            .add(new WoodworkingOreRecipe(FlintKnappingPlans.PLAN_FLINT_KNIFE_BLADE_2, "materialFlintCore", new ItemStack(BidsItems.flintKnifeBlade, 2)));

        setup.recipes()
            .addShaped(new ItemStack(BidsItems.flintKnife), "HB", "W ",
                'H', BidsItems.flintKnifeBlade, 'B', "materialBindingStrong", 'W', "stickWood");
    }

}
