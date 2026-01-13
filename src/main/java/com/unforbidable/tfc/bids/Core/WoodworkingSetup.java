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
import com.unforbidable.tfc.bids.api.BidsWoodworking;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingManager;
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
            String suffix = RecipeSetup.getOreSuffixWood(wood.index);
            WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_LUMBER, "logWood" + suffix, wood.items.getLumber(4)));
            WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_SUPPORT, "logWood" + suffix, wood.blocks.getWoodSupport(4)));

            if (wood.items.hasBoard()) {
                WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_BOARD, "logWood" + suffix, wood.items.getBoard()));
                WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_BOARD_2, "logWood" + suffix, wood.items.getBoard(2)));
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_SHAFT, wood.items.getBoard(), wood.items.getShaft()));
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_SHAFT_2, wood.items.getBoard(), wood.items.getShaft(2)));
            }
        }

        WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_PADDLE, "woodBoard", new ItemStack(TFCItems.paddle)));
        WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_MALLET, "logWoodAny", new ItemStack(BidsItems.woodenMallet)));
        WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_SCUTCHING_KNIFE, "woodBoard", new ItemStack(BidsItems.scutchingKnife)));
        WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_COMB_PADDLE, "woodBoard", new ItemStack(BidsItems.woodenCombPaddle)));

        WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_NEEDLE, new ItemStack(TFCItems.bone), new ItemStack(TFCItems.boneNeedle)));
        WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_KNIFE_HEAD, new ItemStack(TFCItems.bone), new ItemStack(BidsItems.boneKnifeHead)));
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
            .offset(-10, -10)
            .addActions(WoodworkingSpecs.CHISEL_CUT)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_SAW, ActionTool.create()
            .offset(-10, -10)
            .addActions(WoodworkingSpecs.SAW_CUT)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_AXE, ActionTool.create()
            .offset(-6, -4)
            .addActions(WoodworkingSpecs.AXE_CHOP)
            .addActions(WoodworkingSpecs.AXE_CARVE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_KNIFE, ActionTool.create()
            .offset(-12, -12)
            .addActions(WoodworkingSpecs.KNIFE_CARVE)
            .addActions(WoodworkingSpecs.KNIFE_FILE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_HAND_AXE, ActionTool.create()
            .offset(-8, -6)
            .addActions(WoodworkingSpecs.AXE_CARVE)
            .addActions(WoodworkingSpecs.KNIFE_FILE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_DRILL, ActionTool.create()
            .offset(-8, 6)
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
            .cutout(Shape.from(2, 19).to(2, 16).to(5, 16).build()) // top left corner
            .cutout(Shape.from(11, 19).to(8, 16).to(11, 16).build()) // top right corner
            .cutout(Shape.from(2, 24).to(3, 25).to(2, 25).build()) // bottom left corner
            .cutout(Shape.from(11, 24).to(11, 25).to(10, 25).build()) // bottom right corner
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
            .cutout(Shape.from(2, 12).to(2, 9).to(5, 9).build()) // top left corner
            .cutout(Shape.from(2, 22).to(5, 25).to(2, 25).build()) // bottom left corner
            .cutout(Shape.rectFrom(4, 20).size(1, 1)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_COMB_PADDLE, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(5, 12)) // top 1/2 left cut off
            .cutout(Shape.rectFrom(8, 0).size(5, 12)) // top 1/2 right cut off
            .cutout(Shape.rectFrom(0, 12).size(3, 13)) // bottom 1/2 left cut off
            .cutout(Shape.rectFrom(10, 12).size(3, 13)) // bottom 1/2 right cut off
            .cutout(Shape.from(3, 14).to(3, 12).to(5, 12).build()) // top left corner
            .cutout(Shape.from(10, 14).to(8, 12).to(10, 12).build()) // top right corner
            .cutout(Shape.rectFrom(4, 23).size(1, 1)) // hole
            .cutout(Shape.rectFrom(6, 23).size(1, 1)) // hole
            .cutout(Shape.rectFrom(8, 23).size(1, 1)) // hole
            .cutout(Shape.rectFrom(4, 21).size(1, 1)) // hole
            .cutout(Shape.rectFrom(6, 21).size(1, 1)) // hole
            .cutout(Shape.rectFrom(8, 21).size(1, 1)) // hole
            .cutout(Shape.rectFrom(4, 19).size(1, 1)) // hole
            .cutout(Shape.rectFrom(6, 19).size(1, 1)) // hole
            .cutout(Shape.rectFrom(8, 19).size(1, 1)) // hole
            .cutout(Shape.rectFrom(4, 17).size(1, 1)) // hole
            .cutout(Shape.rectFrom(6, 17).size(1, 1)) // hole
            .cutout(Shape.rectFrom(8, 17).size(1, 1)) // hole
            .cutout(Shape.rectFrom(4, 15).size(1, 1)) // hole
            .cutout(Shape.rectFrom(6, 15).size(1, 1)) // hole
            .cutout(Shape.rectFrom(8, 15).size(1, 1)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_NEEDLE, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
            .cutout(Shape.rectFrom(4, 0).size(3, 11)) // right top
            .cutout(Shape.rectFrom(5, 11).size(2, 6)) // right bottom
            .cutout(Shape.from(2, 0).to(3, 0).to(2, 1).build()) // left tip corner
            .cutout(Shape.from(3, 0).to(4, 0).to(4, 1).build()) // right tip corner
            .cutout(Shape.from(4, 11).to(5, 11).to(5, 12).build()) // lower right tip corner
            .cutout(Shape.rectFrom(3, 15).size(1, 1)) // hole
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_KNIFE_HEAD, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(2, 17)) // left
            .cutout(Shape.rectFrom(4, 0).size(3, 1)) // right top
            .cutout(Shape.rectFrom(5, 1).size(2, 10)) // right middle
            .cutout(Shape.rectFrom(4, 11).size(3, 6)) // right bottom
            .cutout(Shape.from(3, 0).to(4, 0).to(4, 1).build()) // right top corner
            .cutout(Shape.from(4, 1).to(5, 1).to(5, 2).build()) // right lower corner
            .cutout(Shape.from(4, 11).to(5, 11).to(5, 10).build()) // right lower corner
            .cutout(Shape.from(2, 0).to(3, 0).to(2, 1).build()) // right middle top corner
            .build());

    }

    private static void registerNetworkMessages() {
        Bids.network.registerMessage(WoodworkingMessage.ServerHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(WoodworkingMessage.ClientHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
