package com.unforbidable.tfc.bids.features.crafting.flintknapping;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsToolMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.meta.StoneMeta;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonTool;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.eventhandler.FlintEventHandler;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.item.ItemFlintRock;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.main.FlintKnappingMaterials;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.main.FlintKnappingPlans;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.main.FlintKnappingSpec;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionTool;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.material.Material;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("flintKnapping")
public class FlintKnapping extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(FlintKnappingConfig::load, "crafting");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.FLINT_CORE, ItemFlintRock::new);
        init.item(ItemNames.FLINT_FLAKE, ItemFlintRock::new);

        init.item(ItemNames.BONE_PRESSURE_FLAKER, () -> new ItemCommonTool(TFCItems.boneToolMaterial))
            .apply(i -> i.setMaxDamage(60));
        init.item(ItemNames.ANTLER_BILLET, () -> new ItemCommonTool(BidsToolMaterial.antler));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new FlintEventHandler());

        setup.ores("materialFlint")
            .add(Items.flint);

        setup.ores("materialFlintCore")
            .add(BidsItems.flintCore);

        setup.ores("materialFlintFlake")
            .add(BidsItems.flintFlake);

        setup.ores("itemPressureFlaker")
            .add(BidsItems.bonePressureFlaker);

        setup.ores("itemBillet")
            .add(BidsItems.antlerBillet);

        setup.ores("itemRockHardHammer")
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.QUARTZITE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.BASALT))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.ANDESITE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.DACITE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.GABBRO))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.DIORITE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.GRANITE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.GNEISS));

        setup.ores("itemRockSoftHammer")
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.SANDSTONE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.LIMESTONE))
            .add(new ItemStack(TFCItems.looseRock, 1, StoneMeta.DOLOMITE));

        setup.registry(WoodworkingRegistry.materials)
            .add(new Material("materialFlint", 13, 17, FlintKnappingMaterials.FLINT_RAW))
            .add(new Material("materialFlintCore", 13, 17, FlintKnappingMaterials.FLINT_CORE))
            .add(new Material("materialFlintFlake", 8, 12, FlintKnappingMaterials.FLINT_FLAKE));

        if (FlintKnappingConfig.enableFlintKnappingAnyStone) {
            setup.registry(WoodworkingRegistry.tools)
                .add(ActionTool.create()
                    .ore("itemRock")
                    .offset(0, 0)
                    .addActions(FlintKnappingSpec.hammerSplit)
                    .addActions(FlintKnappingSpec.hardHammerReduce)
                    .addActions(FlintKnappingSpec.hardHammerClean)
                    .addActions(FlintKnappingSpec.softHammerReduce)
                    .addActions(FlintKnappingSpec.softHammerClean)
                    .build());
        } else {
            setup.registry(WoodworkingRegistry.tools)
                .add(ActionTool.create()
                    .ore("itemRockHardHammer")
                    .offset(0, 0)
                    .addActions(FlintKnappingSpec.hammerSplit)
                    .addActions(FlintKnappingSpec.hardHammerReduce)
                    .addActions(FlintKnappingSpec.hardHammerClean)
                    .build())
                .add(ActionTool.create()
                    .ore("itemRockSoftHammer")
                    .offset(0, 0)
                    .addActions(FlintKnappingSpec.softHammerReduce)
                    .addActions(FlintKnappingSpec.softHammerClean)
                    .build());
        }

        setup.registry(WoodworkingRegistry.tools)
            .add(ActionTool.create()
                .ore("itemBillet")
                .offset(-6, -4)
                .addActions(FlintKnappingSpec.softHammerReduce)
                .addActions(FlintKnappingSpec.softHammerClean)
                .build());

        setup.registry(WoodworkingRegistry.tools)
            .add(ActionTool.create()
                .ore("itemPressureFlaker")
                .offset(-6, -6)
                .addActions(FlintKnappingSpec.pressureReduce)
                .addActions(FlintKnappingSpec.pressureClean)
                .build());

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(FlintKnappingPlans.PLAN_FLINT_CORE)
                .cutout(Shape.rectFrom(0, 0).size(2, 17))
                .cutout(Shape.rectFrom(13, 0).size(-2, 17))
                .cutout(Shape.triFrom(2, 0).size(2, 2))
                .cutout(Shape.triFrom(11, 0).size(-2, 2))
                .cutout(Shape.triFrom(2, 17).size(2, -2))
                .cutout(Shape.triFrom(11, 17).size(-2, -2))
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(FlintKnappingPlans.PLAN_FLINT_CORE, "materialFlint", new ItemStack(BidsItems.flintCore)));

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_PRESSURE_FLAKER)
                .cutout(Shape.from(0, 0).to(2, 0)
                    .to(2, 7).to(1, 8)
                    .to(1, 11).to(0, 12).build())
                .cutout(Shape.from(7, 0).to(5, 0)
                    .to(5, 7).to(6, 8)
                    .to(6, 11).to(7, 12).build())
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_PRESSURE_FLAKER, "materialBone", new ItemStack(BidsItems.bonePressureFlaker)));

        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.antlerBillet),
                "materialAntler", "itemSaw")
            .action(damageTool("itemSaw"));

        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.antlerBillet),
                "materialAntler", "itemHandAxe")
            .action(damageTool("itemHandAxe"));
    }

}
