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
        registerNetworkMessages();
    }

    public static void init() {
        registerToolItems();
        registerMaterialItems();
    }

    private static void registerRecipes() {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            String suffix = RecipeSetup.getOreSuffixWood(wood.index);
            WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_LUMBER, "logWood" + suffix, wood.items.getLumber(2)));

            if (wood.items.hasBoard()) {
                WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_BOARD, "logWood" + suffix, wood.items.getBoard()));
                WoodworkingManager.addRecipe(new WoodworkingRecipe(BidsWoodworking.PLAN_SHAFT, wood.items.getBoard(), wood.items.getShaft()));
            }
        }

        WoodworkingManager.addRecipe(new WoodworkingOreRecipe(BidsWoodworking.PLAN_PADDLE, "woodBoard", new ItemStack(TFCItems.paddle)));
    }

    private static void registerMaterialItems() {
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, TFCItems.logs);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.logsSeasoned);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.peeledLog);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_LOG, BidsItems.peeledLogSeasoned);
        WoodworkingRegistry.addItemAsMaterial(BidsWoodworking.MATERIAL_BOARD, BidsItems.board);
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
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_LOG, new Material(13, 25, EnumWoodworkingMaterialType.THICK));
        WoodworkingRegistry.registerMaterial(BidsWoodworking.MATERIAL_BOARD, new Material(13, 25, EnumWoodworkingMaterialType.FLAT));
    }

    private static void registerPlans() {
        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_LUMBER, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(3, 25)) // left cut off
            .cutout(Shape.rectFrom(6, 0).size(1, 25)) // middle saw cut
            .cutout(Shape.rectFrom(10, 0).size(3, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_BOARD, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
            .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
            .build());

        WoodworkingRegistry.registerPlan(BidsWoodworking.PLAN_SHAFT, Plan.create()
            .cutout(Shape.rectFrom(0, 0).size(4, 25)) // left cut off
            .cutout(Shape.rectFrom(9, 0).size(4, 25)) // right cut off
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
    }

    private static void registerNetworkMessages() {
        Bids.network.registerMessage(WoodworkingMessage.ServerHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(WoodworkingMessage.ClientHandler.class, WoodworkingMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
