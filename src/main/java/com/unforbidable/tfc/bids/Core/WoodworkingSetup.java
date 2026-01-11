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
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
import cpw.mods.fml.relauncher.Side;

public class WoodworkingSetup {

    public static void preInit() {
        registerActions();
        registerMaterials();
        registerPlans();
        registerRecipes();
        registerNetworkMessages();
    }

    public static void init() {
        registerToolItems();
        registerMaterialItems();
    }

    private static void registerRecipes() {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getLog(), wood.items.getLumber(2)));
            }

            if (wood.items.hasSeasonedLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getSeasonedLog(), wood.items.getLumber(2)));
            }

            if (wood.items.hasChoppedLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getChoppedLog(), wood.items.getLumber(2)));
            }

            if (wood.items.hasSeasonedChoppedLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getSeasonedChoppedLog(), wood.items.getLumber(2)));
            }

            if (wood.items.hasPeeledLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getPeeledLog(), wood.items.getLumber(2)));
            }

            if (wood.items.hasSeasonedPeeledLog()) {
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_PLANKS, wood.items.getSeasonedPeeledLog(), wood.items.getLumber(2)));
            }
        }
    }

    private static void registerMaterialItems() {
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.peeledLogSeasoned);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_BOARD, TFCItems.singlePlank);
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
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_HAND_AXE, ActionTool.create()
            .offset(-8, -6)
            .addActions(WoodworkingSpecs.AXE_CARVE)
            .build());

        WoodworkingRegistry.registerTool(BidsWoodworking.TOOL_DRILL, ActionTool.create()
            .offset(-8, 6)
            .addActions(WoodworkingSpecs.DRILL_DRILL)
            .build());
    }

    private static void registerMaterials() {
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_LOG, new Material(13, 25, false));
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_BOARD, new Material(13, 25, true));
    }

    private static void registerPlans() {
        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_PLANKS, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
            .build());
    }

    private static void registerNetworkMessages() {
        Bids.network.registerMessage(WoodworkingMessage.ServerHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(WoodworkingMessage.ClientHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
