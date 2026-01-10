package com.unforbidable.tfc.bids.Core.Woodworking;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.Action;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroup;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionSpec;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Orientation;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.PointF;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;

public class WoodworkingSpecs {

    public static final ActionSpec DRILL_DRILL_1X1 = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(1, 1))
        .margin(Shape.rectFrom(-1, -1).size(3, 3))
        .origin(PointF.at(0.5f, 0.5f))
        .build();

    public static final ActionSpec DRILL_DRILL_2X2 = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(2, 2))
        .margin(Shape.rectFrom(-1, -1).size(4, 4))
        .origin(PointF.at(1, 1))
        .build();

    public static final ActionSpec SAW_CUT_TOP = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).to(1, 1))
        .clearance(Shape.rectFrom(0, 0).to(1, -3))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec SAW_CUT_BOTTOM = SAW_CUT_TOP.flip(Orientation.VERTICAL);
    public static final ActionSpec SAW_CUT_RIGHT = SAW_CUT_TOP.rotate(1);
    public static final ActionSpec SAW_CUT_LEFT = SAW_CUT_RIGHT.flip(Orientation.HORIZONTAL);

    public static final ActionSpec AXE_CHOP_TOP = ActionSpec.create()
        .cutout(Shape.rectFrom(-1, 0).size(3, 255))
        .clearance(Shape.rectFrom(-1, 0).size(3, -32))
        .build();
    public static final ActionSpec AXE_CHOP_BOTTOM = AXE_CHOP_TOP.flip(Orientation.VERTICAL);

    public static final ActionSpec AXE_CARVE_RIGHT_A = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(4, 2).to(2, 2).build())
        .clearance(Shape.from(2, 0).to(4, 2).to(2, 2)
            .to(10, 10).to(10, 0).build())
        .origin(PointF.at(2, 1))
        .build();
    public static final ActionSpec AXE_CARVE_RIGHT_B = AXE_CARVE_RIGHT_A.flip(Orientation.VERTICAL);
    public static final ActionSpec AXE_CARVE_LEFT_A = AXE_CARVE_RIGHT_A.flip(Orientation.HORIZONTAL);
    public static final ActionSpec AXE_CARVE_LEFT_B = AXE_CARVE_RIGHT_B.flip(Orientation.HORIZONTAL);

    public static final ActionSpec KNIFE_CARVE_RIGHT_A = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(2, 2).build())
        .clearance(Shape.from(2, 0).to(2, 2).to(4, 2).to(4, 0).build())
        .origin(PointF.at(1, 1))
        .build();
    public static final ActionSpec KNIFE_CARVE_RIGHT_B = KNIFE_CARVE_RIGHT_A.flip(Orientation.VERTICAL);
    public static final ActionSpec KNIFE_CARVE_RIGHT_C = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(0, 2).build())
        .clearance(Shape.from(2, 0).to(0, 2).to(2, 2).to(4, 0).build())
        .origin(PointF.at(1, 1))
        .build();
    public static final ActionSpec KNIFE_CARVE_RIGHT_D = KNIFE_CARVE_RIGHT_C.flip(Orientation.VERTICAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_A = KNIFE_CARVE_RIGHT_A.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_B = KNIFE_CARVE_RIGHT_B.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_C = KNIFE_CARVE_RIGHT_C.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_D = KNIFE_CARVE_RIGHT_D.flip(Orientation.HORIZONTAL);

    public static final ActionSpec CHISEL_CUT_H = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(1, 1))
        .clearance(Shape.rectFrom(0, 0).size(-1, 1))
        .clearance(Shape.rectFrom(1, 0).size(1, 1))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec CHISEL_CUT_V = CHISEL_CUT_H.rotate(1);

    public static final ActionGroup DRILL_DRILL = ActionGroup.create("drill_drill")
        .damage(1f)
        .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
        .add(new Action("drill_drill", DRILL_DRILL_1X1))
        .build();

    public static final ActionGroup SAW_CUT = ActionGroup.create("saw_cut")
        .damage(0.05f)
        .add(new Action("saw_cut_top", SAW_CUT_TOP, EnumWoodworkingActionSide.TOP))
        .add(new Action("saw_cut_bottom", SAW_CUT_BOTTOM, EnumWoodworkingActionSide.BOTTOM))
        .add(new Action("saw_cut_right", SAW_CUT_RIGHT, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("saw_cut_left", SAW_CUT_LEFT, EnumWoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup AXE_CHOP = ActionGroup.create("axe_chop")
        .damage(1f)
        .add(new Action("axe_chop_top", AXE_CHOP_TOP, EnumWoodworkingActionSide.TOP))
        .add(new Action("axe_chop_bottom", AXE_CHOP_BOTTOM, EnumWoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionGroup AXE_CARVE = ActionGroup.create("axe_carve")
        .damage(0.5f)
        .add(new Action("axe_carve_right_a", AXE_CARVE_RIGHT_A, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("axe_carve_right_b", AXE_CARVE_RIGHT_B, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("axe_carve_left_a", AXE_CARVE_LEFT_A, EnumWoodworkingActionSide.LEFT))
        .add(new Action("axe_carve_left_b", AXE_CARVE_LEFT_B, EnumWoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup KNIFE_CARVE = ActionGroup.create("knife_carve")
        .damage(0.2f)
        .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
        .add(new Action("knife_carve_right_a", KNIFE_CARVE_RIGHT_A, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("knife_carve_right_b", KNIFE_CARVE_RIGHT_B, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("knife_carve_right_d", KNIFE_CARVE_RIGHT_C, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("knife_carve_right_d", KNIFE_CARVE_RIGHT_D, EnumWoodworkingActionSide.RIGHT))
        .add(new Action("knife_carve_left_a", KNIFE_CARVE_LEFT_A, EnumWoodworkingActionSide.LEFT))
        .add(new Action("knife_carve_left_b", KNIFE_CARVE_LEFT_B, EnumWoodworkingActionSide.LEFT))
        .add(new Action("knife_carve_left_c", KNIFE_CARVE_LEFT_C, EnumWoodworkingActionSide.LEFT))
        .add(new Action("knife_carve_left_d", KNIFE_CARVE_LEFT_D, EnumWoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup CHISEL_CUT = ActionGroup.create("chisel_cut")
        .damage(1f)
        .usage(ActionGroupUsage.FLAT_MATERIAL_ONLY)
        .add(new Action("chisel_cut_h", CHISEL_CUT_H))
        .add(new Action("chisel_cut_v", CHISEL_CUT_V))
        .build();

}
