package com.unforbidable.tfc.bids.Core.Woodworking;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionSpec;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Orientation;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Point;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;

public class WoodworkingSpecs {

    public static final ActionSpec DRILL_DRILL_1X1 = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(1, 1))
        .margin(Shape.rectFrom(-1, -1).size(3, 3))
        .build();

    public static final ActionSpec DRILL_DRILL_2X2 = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(2, 2))
        .margin(Shape.rectFrom(-1, -1).size(4, 4))
        .origin(Point.at(1, 1))
        .build();

    public static final ActionSpec SAW_CUT_TOP = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).to(1, 1))
        .clearance(Shape.rectFrom(0, 0).to(1, -4))
        .build();
    public static final ActionSpec SAW_CUT_BOTTOM = SAW_CUT_TOP.flip(Orientation.VERTICAL);
    public static final ActionSpec SAW_CUT_RIGHT = SAW_CUT_TOP.rotate(1);
    public static final ActionSpec SAW_CUT_LEFT = SAW_CUT_RIGHT.flip(Orientation.HORIZONTAL);

    public static final ActionSpec AXE_CHOP_TOP = ActionSpec.create()
        .cutout(Shape.rectFrom(-1, -1).size(3, 255))
        .clearance(Shape.rectFrom(0, 0).size(1, -16))
        .build();
    public static final ActionSpec AXE_CHOP_BOTTOM = AXE_CHOP_TOP.flip(Orientation.VERTICAL);

    public static final ActionSpec AXE_CARVE_RIGHT_A = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(4, 2).to(2, 2).build())
        .clearance(Shape.from(2, 0).to(4, 2).to(2, 2)
            .to(10, 10).to(10, 0).build())
        .build();
    public static final ActionSpec AXE_CARVE_RIGHT_B = AXE_CARVE_RIGHT_A.flip(Orientation.VERTICAL);
    public static final ActionSpec AXE_CARVE_LEFT_A = AXE_CARVE_RIGHT_A.flip(Orientation.HORIZONTAL);
    public static final ActionSpec AXE_CARVE_LEFT_B = AXE_CARVE_RIGHT_B.flip(Orientation.HORIZONTAL);

    public static final ActionSpec KNIFE_CARVE_RIGHT_A = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(2, 2).build())
        .clearance(Shape.from(2, 0).to(2, 2).to(4, 2).to(4, 0).build())
        .build();
    public static final ActionSpec KNIFE_CARVE_RIGHT_B = KNIFE_CARVE_RIGHT_A.flip(Orientation.VERTICAL);
    public static final ActionSpec KNIFE_CARVE_RIGHT_C = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(0, 2).build())
        .clearance(Shape.from(2, 0).to(0, 2).to(2, 2).to(4, 0).build())
        .build();
    public static final ActionSpec KNIFE_CARVE_RIGHT_D = KNIFE_CARVE_RIGHT_C.flip(Orientation.VERTICAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_A = KNIFE_CARVE_RIGHT_A.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_B = KNIFE_CARVE_RIGHT_B.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_C = KNIFE_CARVE_RIGHT_C.flip(Orientation.HORIZONTAL);
    public static final ActionSpec KNIFE_CARVE_LEFT_D = KNIFE_CARVE_RIGHT_D.flip(Orientation.HORIZONTAL);

    public static final ActionSpec CHISEL_CUT_H = ActionSpec.create()
        .cutout(Shape.rectFrom(-1, 0).size(3, 1))
        .clearance(Shape.rectFrom(-2, 0).to(-1, 1))
        .clearance(Shape.rectFrom(2, 0).to(3, 1))
        .build();
    public static final ActionSpec CHISEL_CUT_V = CHISEL_CUT_H.rotate(1);

}
