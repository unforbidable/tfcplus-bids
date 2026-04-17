package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.NetworkHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionTool;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;
import com.unforbidable.tfc.bids.Core.Woodworking.Material.Material;
import com.unforbidable.tfc.bids.Core.Woodworking.Network.WoodworkingMessage;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.Plan;
import com.unforbidable.tfc.bids.Core.Woodworking.WoodworkingSpecs;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.BidsWoodworking;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.ItemStack;

public class WoodworkingSetup {

    public static void preInit() {
        registerActions();
        registerMaterials();
        registerPlans();
        registerRecipes();
    }

    public static void init() {
        registerToolItems();
        registerMaterialItems();
        registerNetworkMessages();
    }

    private static void registerRecipes() {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_LUMBER, wood.getOreWithSuffix("logWood"), wood.items.getLumber(4)));
            BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_SUPPORT, wood.getOreWithSuffix("logWood"), wood.blocks.getWoodSupport(4)));

            if (wood.items.hasBoard()) {
                BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_BOARD, wood.getOreWithSuffix("logWood"), wood.items.getBoard()));
                BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_BOARD_2, wood.getOreWithSuffix("logWood"), wood.items.getBoard(2)));
                BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingRecipe(BidsWoodworking.PLAN_SHAFT, wood.items.getBoard(), wood.items.getShaft()));
                BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingRecipe(BidsWoodworking.PLAN_SHAFT_2, wood.items.getBoard(), wood.items.getShaft(2)));
            }
        }

        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_PADDLE, "woodBoard", new ItemStack(TFCItems.paddle)));
        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_MALLET, "logWoodAny", new ItemStack(BidsItems.woodenMallet)));
        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_SCUTCHING_KNIFE, "woodBoard", new ItemStack(BidsItems.scutchingKnife)));
        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingOreRecipe(BidsWoodworking.PLAN_COMB_PADDLE, "woodBoard", new ItemStack(BidsItems.woodenCombPaddle)));

        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingRecipe(BidsWoodworking.PLAN_NEEDLE, new ItemStack(TFCItems.bone), new ItemStack(TFCItems.boneNeedle)));
        BidsRegistry.WOODWORKING_RECIPES.register(new WoodworkingRecipe(BidsWoodworking.PLAN_KNIFE_HEAD, new ItemStack(TFCItems.bone), new ItemStack(BidsItems.boneKnifeHead)));
    }

    private static void registerMaterialItems() {
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, TFCItems.logs);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.logsSeasoned);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.peeledLog);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.peeledLogSeasoned);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_BOARD, BidsItems.board);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_BONE, TFCItems.bone);
    }

    private static void registerToolItems() {
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_CHISEL, "itemChisel");
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_SAW, "itemSaw");
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_AXE, "itemAxe");
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_KNIFE, "itemKnife");
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_HAND_AXE, "itemHandAxe");
        WoodworkingRegistry.addOreAsTool(BidsWoodworking.TOOL_DRILL, "itemDrill");
    }

    private static void registerActions() {
        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_CHISEL, ActionTool.create()
            .offset(-7, -7)
            .addActions(WoodworkingSpecs.CHISEL_CUT)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_SAW, ActionTool.create()
            .offset(-7, -6)
            .addActions(WoodworkingSpecs.SAW_CUT)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_AXE, ActionTool.create()
            .offset(-2, -4)
            .addActions(WoodworkingSpecs.AXE_CHOP)
            .addActions(WoodworkingSpecs.AXE_CARVE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_KNIFE, ActionTool.create()
            .offset(-5, -6)
            .addActions(WoodworkingSpecs.KNIFE_CARVE)
            .addActions(WoodworkingSpecs.KNIFE_FILE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_HAND_AXE, ActionTool.create()
            .offset(-6, -6)
            .addActions(WoodworkingSpecs.AXE_CARVE)
            .addActions(WoodworkingSpecs.KNIFE_FILE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_DRILL, ActionTool.create()
            .offset(-6, 6)
            .addActions(WoodworkingSpecs.DRILL_DRILL)
            .build());
    }

    private static void registerMaterials() {
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_LOG, new Material(13, 25, EnumWoodworkingMaterialType.WOOD_THICK));
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_BOARD, new Material(13, 25, EnumWoodworkingMaterialType.WOOD_FLAT));
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_BONE, new Material(7, 17, EnumWoodworkingMaterialType.BONE));
    }

    private static void registerPlans() {
        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_LUMBER, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(3, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(10, 0).size(3, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_SUPPORT, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(1, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(12, 0).size(1, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_BOARD, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
            .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_SHAFT, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
            .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_BOARD_2, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(11, 0).size(2, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_SHAFT_2, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(11, 0).size(2, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_PADDLE, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(5, 16)) // top 2/3 left cut off
            .cutout(Shape.rectFrom(8, 0).size(5, 16)) // top 2/3 right cut off
            .cutout(Shape.rectFrom(0, 16).size(2, 9)) // bottom 1/3 left cut off
            .cutout(Shape.rectFrom(11, 16).size(2, 9)) // bottom 1/3 right cut off
            .cutout(Shape.triFrom(2, 16).size(3, 3)) // top left corner
            .cutout(Shape.triFrom(11, 16).size(-3, 3)) // top right corner
            .cutout(Shape.triFrom(2, 25).size(1, -1)) // bottom left corner
            .cutout(Shape.triFrom(11, 25).size(-1, -1)) // bottom right corner
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_MALLET, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(5, 9)) // top 1/3 left cut off
            .cutout(Shape.rectFrom(8, 0).size(5, 9)) // top 1/3 right cut off
            .cutout(Shape.rectFrom(0, 9).size(2, 16)) // bottom 2/3 left cut off
            .cutout(Shape.rectFrom(11, 9).size(2, 16)) // bottom 2/3 right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_SCUTCHING_KNIFE, Plan.create()
            .cutout(Shape.rectFrom(8, 0).size(5, 25)) // right cut off
            .cutout(Shape.rectFrom(0, 0).size(5, 9)) // top 1/3 left cut off
            .cutout(Shape.rectFrom(0, 9).size(2, 16)) // bottom 2/3 left cut off
            .cutout(Shape.triFrom(2, 9).size(3, 3)) // top left corner
            .cutout(Shape.triFrom(2, 25).size(3, -3)) // bottom left corner
            .cutout(Shape.pointAt(4, 20)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_COMB_PADDLE, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(5, 12)) // top 1/2 left cut off
            .cutout(Shape.rectFrom(8, 0).size(5, 12)) // top 1/2 right cut off
            .cutout(Shape.rectFrom(0, 12).size(3, 13)) // bottom 1/2 left cut off
            .cutout(Shape.rectFrom(10, 12).size(3, 13)) // bottom 1/2 right cut off
            .cutout(Shape.triFrom(3, 12).size(2, 2)) // top left corner
            .cutout(Shape.triFrom(10, 12).size(-2, 2)) // top right corner
            .cutout(Shape.pointAt(4, 23)) // hole
            .cutout(Shape.pointAt(6, 23)) // hole
            .cutout(Shape.pointAt(8, 23)) // hole
            .cutout(Shape.pointAt(4, 21)) // hole
            .cutout(Shape.pointAt(6, 21)) // hole
            .cutout(Shape.pointAt(8, 21)) // hole
            .cutout(Shape.pointAt(4, 19)) // hole
            .cutout(Shape.pointAt(6, 19)) // hole
            .cutout(Shape.pointAt(8, 19)) // hole
            .cutout(Shape.pointAt(4, 17)) // hole
            .cutout(Shape.pointAt(6, 17)) // hole
            .cutout(Shape.pointAt(8, 17)) // hole
            .cutout(Shape.pointAt(4, 15)) // hole
            .cutout(Shape.pointAt(6, 15)) // hole
            .cutout(Shape.pointAt(8, 15)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_NEEDLE, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
            .cutout(Shape.rectFrom(4, 0).size(3, 11)) // right top
            .cutout(Shape.rectFrom(5, 11).size(2, 6)) // right bottom
            .cutout(Shape.triFrom(2, 0).size(1, 1)) // left tip corner
            .cutout(Shape.triFrom(4, 0).size(-1, 1)) // right tip corner
            .cutout(Shape.triFrom(5, 11).size(-1, 1)) // lower right tip corner
            .cutout(Shape.pointAt(3, 15)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_KNIFE_HEAD, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
            .cutout(Shape.rectFrom(4, 0).size(3, 1)) // right top
            .cutout(Shape.rectFrom(5, 1).size(2, 10)) // right middle
            .cutout(Shape.rectFrom(4, 11).size(3, 6)) // right bottom
            .cutout(Shape.triFrom(4, 0).size(-1, 1)) // right top corner
            .cutout(Shape.triFrom(5, 1).size(-1, 1)) // right lower corner
            .cutout(Shape.triFrom(5, 11).size(-1, -1)) // right lower corner
            .cutout(Shape.triFrom(2, 0).size(1, 1)) // right top corner
            .build());

    }

    private static void registerNetworkMessages() {
        Bids.network.registerMessage(WoodworkingMessage.ServerHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(WoodworkingMessage.ClientHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
