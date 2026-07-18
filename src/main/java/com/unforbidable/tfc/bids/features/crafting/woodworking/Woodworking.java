package com.unforbidable.tfc.bids.features.crafting.woodworking;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.crafting.woodworking.container.ContainerWoodworking;
import com.unforbidable.tfc.bids.features.crafting.woodworking.eventhandler.WoodworkingEventHandler;
import com.unforbidable.tfc.bids.features.crafting.woodworking.gui.GuiWoodworking;
import com.unforbidable.tfc.bids.features.crafting.woodworking.item.ItemBoard;
import com.unforbidable.tfc.bids.features.crafting.woodworking.item.ItemShaft;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingSpecs;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionTool;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.material.Material;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import com.unforbidable.tfc.bids.features.crafting.woodworking.nei.WoodworkingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.woodworking.network.WoodworkingPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.GuiNames.WOODWORKING;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOARD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SHAFT;

@FeatureName("woodworking")
public class Woodworking extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(BOARD, ItemBoard::new)
            .meta(Global.WOOD_ALL);
        init.item(SHAFT, ItemShaft::new)
            .meta(Global.WOOD_ALL);

        init.gui(WOODWORKING, ContainerWoodworking::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.gui(WOODWORKING, GuiWoodworking::new);

        client.nei()
            .handler(new WoodworkingNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.network()
            .register(WoodworkingPacket.class);

        setup.event()
            .handler(new WoodworkingEventHandler());

        setup.ores("boardWood")
            .add(BidsItems.board);

        setup.ores("materialBone")
            .add(TFCItems.bone);

        setup.registry(WoodworkingRegistry.materials)
            .add(new Material("logWood", 13, 25, WoodworkingMaterialType.WOOD_THICK))
            .add(new Material("boardWood", 13, 25, WoodworkingMaterialType.WOOD_FLAT))
            .add(new Material("materialBone", 7, 17, WoodworkingMaterialType.BONE));

        setup.registry(WoodworkingRegistry.tools)
            .add(ActionTool.create()
                .ore("itemChisel")
                .offset(-7, -7)
                .addActions(WoodworkingSpecs.chiselCut)
                .build())
            .add(ActionTool.create()
                .ore("itemSaw")
                .offset(-7, -6)
                .addActions(WoodworkingSpecs.sawCut)
                .build())
            .add(ActionTool.create()
                .ore("itemAxe")
                .offset(-2, -4)
                .addActions(WoodworkingSpecs.axeChop)
                .addActions(WoodworkingSpecs.axeCarve)
                .build())
            .add(ActionTool.create()
                .ore("itemKnife")
                .offset(-5, -6)
                .addActions(WoodworkingSpecs.knifeCarve)
                .addActions(WoodworkingSpecs.knifeFile)
                .build())
            .add(ActionTool.create()
                .ore("itemHandAxe")
                .offset(-6, -6)
                .addActions(WoodworkingSpecs.axeCarve)
                .addActions(WoodworkingSpecs.knifeFile)
                .build())
            .add(ActionTool.create()
                .ore("itemDrill")
                .offset(-6, 6)
                .addActions(WoodworkingSpecs.drill)
                .build());

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_LUMBER)
                .cutout(Shape.rectFrom(0, 0).size(3, 25)) // left cut off
                .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
                .cutout(Shape.rectFrom(10, 0).size(3, 25)) // right cut off
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_SUPPORT)
                .cutout(Shape.rectFrom(0, 0).size(1, 25)) // left cut off
                .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
                .cutout(Shape.rectFrom(12, 0).size(1, 25)) // right cut off
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_BOARD)
                .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
                .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_SHAFT)
                .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
                .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_BOARD_2)
                .cutout(Shape.rectFrom(0, 0).size(2, 25)) // left cut off
                .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
                .cutout(Shape.rectFrom(11, 0).size(2, 25)) // right cut off
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_SHAFT_2)
                .cutout(Shape.rectFrom(0, 0).size(2, 25)) // left cut off
                .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
                .cutout(Shape.rectFrom(11, 0).size(2, 25)) // right cut off
                .build());

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            setup.registry(WoodworkingRegistry.recipes)
                .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_LUMBER, wood.getOreWithSuffix("logWood"), wood.items.getLumber(4)))
                .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_SUPPORT, wood.getOreWithSuffix("logWood"), wood.blocks.getWoodSupport(4)));

            if (wood.items.hasBoard()) {
                setup.registry(WoodworkingRegistry.recipes)
                    .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_BOARD, wood.getOreWithSuffix("logWood"), wood.items.getBoard()))
                    .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_BOARD_2, wood.getOreWithSuffix("logWood"), wood.items.getBoard(2)))
                    .add(new WoodworkingRecipe(WoodworkingPlanNames.PLAN_SHAFT, wood.items.getBoard(), wood.items.getShaft()))
                    .add(new WoodworkingRecipe(WoodworkingPlanNames.PLAN_SHAFT_2, wood.items.getBoard(), wood.items.getShaft(2)));
            }
        }

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_PADDLE)
                .cutout(Shape.rectFrom(0, 0).size(5, 16)) // top 2/3 left cut off
                .cutout(Shape.rectFrom(8, 0).size(5, 16)) // top 2/3 right cut off
                .cutout(Shape.rectFrom(0, 16).size(2, 9)) // bottom 1/3 left cut off
                .cutout(Shape.rectFrom(11, 16).size(2, 9)) // bottom 1/3 right cut off
                .cutout(Shape.triFrom(2, 16).size(3, 3)) // top left corner
                .cutout(Shape.triFrom(11, 16).size(-3, 3)) // top right corner
                .cutout(Shape.triFrom(2, 25).size(1, -1)) // bottom left corner
                .cutout(Shape.triFrom(11, 25).size(-1, -1)) // bottom right corner
                .build())
            .add(Plan.create(WoodworkingPlanNames.PLAN_NEEDLE)
                .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
                .cutout(Shape.rectFrom(4, 0).size(3, 11)) // right top
                .cutout(Shape.rectFrom(5, 11).size(2, 6)) // right bottom
                .cutout(Shape.triFrom(2, 0).size(1, 1)) // left tip corner
                .cutout(Shape.triFrom(4, 0).size(-1, 1)) // right tip corner
                .cutout(Shape.triFrom(5, 11).size(-1, 1)) // lower right tip corner
                .cutout(Shape.pointAt(3, 15)) // hole
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_PADDLE, "boardWood", new ItemStack(TFCItems.paddle)))
            .add(new WoodworkingRecipe(WoodworkingPlanNames.PLAN_NEEDLE, new ItemStack(TFCItems.bone), new ItemStack(TFCItems.boneNeedle)));
    }

}
