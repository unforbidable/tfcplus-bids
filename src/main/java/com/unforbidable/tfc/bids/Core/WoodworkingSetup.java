package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.Action;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroup;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;
import com.unforbidable.tfc.bids.Core.Woodworking.Material.Material;
import com.unforbidable.tfc.bids.Core.Woodworking.WoodworkingSpecs;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsWoodworking;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WoodworkingSetup {

    public static void preInit() {
        setupActions();
        setupMaterials();
        registerMaterials();
        registerTools();
    }

    private static void registerMaterials() {
        WoodworkingRegistry.registerMaterial(BidsItems.peeledLogSeasoned, BidsWoodworking.Materials.LOG);
        WoodworkingRegistry.registerMaterial(TFCItems.singlePlank, BidsWoodworking.Materials.BOARD);
    }

    private static void registerTools() {
        for (ItemStack item : OreDictionary.getOres("itemChisel")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.CHISEL_CUT);
        }

        for (ItemStack item : OreDictionary.getOres("itemSaw")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.SAW_CUT);
        }

        for (ItemStack item : OreDictionary.getOres("itemAxe")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.AXE_CHOP);
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.AXE_CARVE);
        }

        for (ItemStack item : OreDictionary.getOres("itemKnife")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.KNIFE_CARVE);
        }

        for (ItemStack item : OreDictionary.getOres("itemDrill")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.DRILL_DRILL);
        }

        for (ItemStack item : OreDictionary.getOres("itemHandAxe")) {
            WoodworkingRegistry.registerTool(item.getItem(), BidsWoodworking.Actions.AXE_CARVE);
        }
    }

    private static void setupActions() {
        BidsWoodworking.Actions.DRILL_DRILL = ActionGroup.create("drill_drill")
            .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
            .add(new Action("drill_drill", WoodworkingSpecs.DRILL_DRILL_1X1))
            .build();

        BidsWoodworking.Actions.SAW_CUT = ActionGroup.create("saw_cut")
            .add(new Action("saw_cut_top", WoodworkingSpecs.SAW_CUT_TOP, EnumWoodworkingActionSide.TOP))
            .add(new Action("saw_cut_bottom", WoodworkingSpecs.SAW_CUT_BOTTOM, EnumWoodworkingActionSide.BOTTOM))
            .add(new Action("saw_cut_right", WoodworkingSpecs.SAW_CUT_RIGHT, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("saw_cut_left", WoodworkingSpecs.SAW_CUT_LEFT, EnumWoodworkingActionSide.LEFT))
            .build();

        BidsWoodworking.Actions.AXE_CHOP = ActionGroup.create("axe_chop")
            .add(new Action("axe_chop_top", WoodworkingSpecs.AXE_CHOP_TOP, EnumWoodworkingActionSide.TOP))
            .add(new Action("axe_chop_bottom", WoodworkingSpecs.AXE_CHOP_BOTTOM, EnumWoodworkingActionSide.BOTTOM))
            .build();

        BidsWoodworking.Actions.AXE_CARVE = ActionGroup.create("axe_carve")
            .add(new Action("axe_carve_right_a", WoodworkingSpecs.AXE_CARVE_RIGHT_A, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("axe_carve_right_b", WoodworkingSpecs.AXE_CARVE_RIGHT_B, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("axe_carve_left_a", WoodworkingSpecs.AXE_CARVE_RIGHT_A, EnumWoodworkingActionSide.LEFT))
            .add(new Action("axe_carve_left_b", WoodworkingSpecs.AXE_CARVE_RIGHT_A, EnumWoodworkingActionSide.LEFT))
            .build();

        BidsWoodworking.Actions.KNIFE_CARVE = ActionGroup.create("knife_carve")
            .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
            .add(new Action("knife_carve_right_a", WoodworkingSpecs.KNIFE_CARVE_RIGHT_A, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("knife_carve_right_b", WoodworkingSpecs.KNIFE_CARVE_RIGHT_B, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("knife_carve_right_d", WoodworkingSpecs.KNIFE_CARVE_RIGHT_C, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("knife_carve_right_d", WoodworkingSpecs.KNIFE_CARVE_RIGHT_D, EnumWoodworkingActionSide.RIGHT))
            .add(new Action("knife_carve_left_a", WoodworkingSpecs.KNIFE_CARVE_LEFT_A, EnumWoodworkingActionSide.LEFT))
            .add(new Action("knife_carve_left_b", WoodworkingSpecs.KNIFE_CARVE_LEFT_B, EnumWoodworkingActionSide.LEFT))
            .add(new Action("knife_carve_left_c", WoodworkingSpecs.KNIFE_CARVE_LEFT_C, EnumWoodworkingActionSide.LEFT))
            .add(new Action("knife_carve_left_d", WoodworkingSpecs.KNIFE_CARVE_LEFT_D, EnumWoodworkingActionSide.LEFT))
            .build();

        BidsWoodworking.Actions.CHISEL_CUT = ActionGroup.create("chisel_cut")
            .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
            .add(new Action("chisel_cut_h", WoodworkingSpecs.CHISEL_CUT_H))
            .add(new Action("chisel_cut_v", WoodworkingSpecs.CHISEL_CUT_V))
            .build();
    }

    private static void setupMaterials() {
        BidsWoodworking.Materials.LOG = new Material(17, 33, false);
        BidsWoodworking.Materials.BOARD = new Material(17, 33, true);
    }

}
