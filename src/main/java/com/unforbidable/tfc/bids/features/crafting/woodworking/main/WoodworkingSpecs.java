package com.unforbidable.tfc.bids.features.crafting.woodworking.main;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.Action;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionGroup;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionSpec;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Orientation;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.PointF;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSide;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterialType;

public class WoodworkingSpecs {

    public static final ActionSpec drillSingle = ActionSpec.create()
        .cutout(Shape.pointAt(0, 0))
        .margin(Shape.rectFrom(-1, -1).size(3, 3))
        .origin(PointF.at(0.5f, 0.5f))
        .build();

    public static final ActionSpec DRILL_DRILL_2X2 = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(2, 2))
        .margin(Shape.rectFrom(-1, -1).size(4, 4))
        .origin(PointF.at(1, 1))
        .build();

    public static final ActionSpec sawCutTop = ActionSpec.create()
        .cutout(Shape.pointAt(0, 0))
        .clearance(Shape.rectFrom(0, 0).to(1, -3))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec sawCutBottom = sawCutTop.flip(Orientation.VERTICAL);
    public static final ActionSpec sawCutRight = sawCutTop.rotate(1);
    public static final ActionSpec sawCutLeft = sawCutRight.flip(Orientation.HORIZONTAL);

    public static final ActionSpec axeChopTop = ActionSpec.create()
        .cutout(Shape.rectFrom(-1, 0).size(3, 255))
        .clearance(Shape.rectFrom(-1, 0).size(3, -32))
        .build();
    public static final ActionSpec axeChopBottom = axeChopTop.flip(Orientation.VERTICAL);

    public static final ActionSpec axeCarveRightA = ActionSpec.create()
        .cutout(Shape.from(0, 0).to(2, 0).to(4, 2).to(2, 2).build())
        .clearance(Shape.from(2, 0).to(4, 2).to(2, 2)
            .to(10, 10).to(10, 0).build())
        .origin(PointF.at(2, 1))
        .build();
    public static final ActionSpec axeCarveRightB = axeCarveRightA.flip(Orientation.VERTICAL);
    public static final ActionSpec axeCarveLeftA = axeCarveRightA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec axeCarveLeftB = axeCarveRightB.flip(Orientation.HORIZONTAL);

    public static final ActionSpec knifeCarveRightA = ActionSpec.create()
        .cutout(Shape.triFrom(2, 0).size(-2, 2))
        .clearance(Shape.from(2, 0).to(2, 2).to(4, 2).to(4, 0).build())
        .origin(PointF.at(1, 1))
        .build();
    public static final ActionSpec knifeCarveRightB = knifeCarveRightA.flip(Orientation.VERTICAL);
    public static final ActionSpec knifeCarveRightC = ActionSpec.create()
        .cutout(Shape.triFrom(0, 0).size(2, 2))
        .clearance(Shape.from(2, 0).to(0, 2).to(2, 2).to(4, 0).build())
        .origin(PointF.at(1, 1))
        .build();
    public static final ActionSpec knifeCarveRightD = knifeCarveRightC.flip(Orientation.VERTICAL);
    public static final ActionSpec knifeCarveLeftA = knifeCarveRightA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec knifeCarveLeftB = knifeCarveRightB.flip(Orientation.HORIZONTAL);
    public static final ActionSpec knifeCarveLeftC = knifeCarveRightC.flip(Orientation.HORIZONTAL);
    public static final ActionSpec knifeCarveLeftD = knifeCarveRightD.flip(Orientation.HORIZONTAL);

    public static final ActionSpec chiselCutH = ActionSpec.create()
        .cutout(Shape.pointAt(0, 0))
        .clearance(Shape.pointAt(-1, 0))
        .clearance(Shape.pointAt(1, 0))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec chiselCutV = chiselCutH.rotate(1);

    public static final ActionGroup drill = ActionGroup.create("drill")
        .damage(1f)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .usage(WoodworkingMaterialType.WOOD_DELICATE)
        .add(new Action("drillSingle", drillSingle))
        .build();

    public static final ActionGroup sawCut = ActionGroup.create("sawCut")
        .damage(0.05f)
        .usage(WoodworkingMaterialType.WOOD_THICK)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .usage(WoodworkingMaterialType.WOOD_DELICATE)
        .usage(WoodworkingMaterialType.BONE)
        .add(new Action("sawCutTop", sawCutTop, WoodworkingActionSide.TOP))
        .add(new Action("sawCutBottom", sawCutBottom, WoodworkingActionSide.BOTTOM))
        .add(new Action("sawCutRight", sawCutRight, WoodworkingActionSide.RIGHT))
        .add(new Action("sawCutLeft", sawCutLeft, WoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup axeChop = ActionGroup.create("axeChop")
        .damage(1f)
        .usage(WoodworkingMaterialType.WOOD_THICK)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .add(new Action("axeChopTop", axeChopTop, WoodworkingActionSide.TOP))
        .add(new Action("axeChopBottom", axeChopBottom, WoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionGroup axeCarve = ActionGroup.create("axeCarve")
        .damage(0.5f)
        .usage(WoodworkingMaterialType.WOOD_THICK)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .add(new Action("axeCarveRightA", axeCarveRightA, WoodworkingActionSide.RIGHT))
        .add(new Action("axeCarveRightB", axeCarveRightB, WoodworkingActionSide.RIGHT))
        .add(new Action("axeCarveLeftA", axeCarveLeftA, WoodworkingActionSide.LEFT))
        .add(new Action("axeCarveLeftB", axeCarveLeftB, WoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup knifeCarve = ActionGroup.create("knifeCarve")
        .damage(0.2f)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .usage(WoodworkingMaterialType.WOOD_DELICATE)
        .add(new Action("knifeCarveRightA", knifeCarveRightA, WoodworkingActionSide.RIGHT))
        .add(new Action("knifeCarveRightB", knifeCarveRightB, WoodworkingActionSide.RIGHT))
        .add(new Action("knifeCarveRightC", knifeCarveRightC, WoodworkingActionSide.RIGHT))
        .add(new Action("knifeCarveRightD", knifeCarveRightD, WoodworkingActionSide.RIGHT))
        .add(new Action("knifeCarveLeftA", knifeCarveLeftA, WoodworkingActionSide.LEFT))
        .add(new Action("knifeCarveLeftB", knifeCarveLeftB, WoodworkingActionSide.LEFT))
        .add(new Action("knifeCarveLeftC", knifeCarveLeftC, WoodworkingActionSide.LEFT))
        .add(new Action("knifeCarveLeftD", knifeCarveLeftD, WoodworkingActionSide.LEFT))
        .build();

    public static final ActionGroup chiselCut = ActionGroup.create("chiselCut")
        .damage(1f)
        .usage(WoodworkingMaterialType.WOOD_FLAT)
        .usage(WoodworkingMaterialType.WOOD_DELICATE)
        .add(new Action("chiselCutH", chiselCutH))
        .add(new Action("chiselCutV", chiselCutV))
        .build();

    public static final ActionSpec knifeFileSingle = ActionSpec.create()
        .cutout(Shape.pointAt(0, 0))
        .origin(PointF.at(0.5f, 0.5f))
        .build();

    public static final ActionSpec knifeFileCornerLeftA = ActionSpec.create()
        .cutout(Shape.triFrom(0, 0).size(1, 1))
        .clearance(Shape.rectFrom(0, 0).size(-1, 1))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec knifeFileCornerLeftB = knifeFileCornerLeftA.flip(Orientation.VERTICAL);
    public static final ActionSpec knifeFileCornerRightA = knifeFileCornerLeftA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec knifeFileCornerRightB = knifeFileCornerRightA.flip(Orientation.VERTICAL);

    public static final ActionGroup knifeFile = ActionGroup.create("knifeFile")
        .damage(0.1f)
        .usage(WoodworkingMaterialType.BONE)
        .add(new Action("knifeFileSingle", knifeFileSingle))
        .add(new Action("knifeFileCornerLeftA", knifeFileCornerLeftA))
        .add(new Action("knifeFileCornerLeftB", knifeFileCornerLeftB))
        .add(new Action("knifeFileCornerRightA", knifeFileCornerRightA))
        .add(new Action("knifeFileCornerRightB", knifeFileCornerRightB))
        .build();

}
