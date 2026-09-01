package com.unforbidable.tfc.bids.features.crafting.flintknapping.main;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSide;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.Action;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionGroup;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionSpec;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Orientation;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.PointF;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;

public class FlintKnappingSpec {

    public static final ActionSpec hammerSplitTop = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(1, 55))
        .clearance(Shape.rectFrom(-1, 0).size(3, -8))
        .margin(Shape.rectFrom(-2, 0).size(5, 2))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec hammerSplitBottom = hammerSplitTop.flip(Orientation.VERTICAL);
    public static final ActionGroup hammerSplit = ActionGroup.create("hammerSplit")
        .damage(1f)
        .usage(FlintKnappingMaterials.FLINT_CORE)
        .add(new Action("hammerSplitTop", hammerSplitTop, WoodworkingActionSide.TOP))
        .add(new Action("hammerSplitBottom", hammerSplitBottom, WoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionSpec hardHammerReduceLeft = ActionSpec.create()
        .cutout(Shape.rectFrom(0, -1).size(2, 2))
        .clearance(Shape.rectFrom(0, -3).size(-4, 6))
        .origin(PointF.at(1f, 0f))
        .build();
    public static final ActionSpec hardHammerReduceRight = hardHammerReduceLeft.flip(Orientation.HORIZONTAL);
    public static final ActionSpec hardHammerReduceTop = hardHammerReduceRight.rotate(3);
    public static final ActionSpec hardHammerReduceBottom = hardHammerReduceTop.flip(Orientation.VERTICAL);
    public static final ActionGroup hardHammerReduce = ActionGroup.create("hardHammerReduce")
        .damage(0.5f)
        .usage(FlintKnappingMaterials.FLINT_RAW)
        .add(new Action("hardHammerReduceLeft", hardHammerReduceLeft, WoodworkingActionSide.LEFT))
        .add(new Action("hardHammerReduceRight", hardHammerReduceRight, WoodworkingActionSide.RIGHT))
        .add(new Action("hardHammerReduceTop", hardHammerReduceTop, WoodworkingActionSide.TOP))
        .add(new Action("hardHammerReduceBottom", hardHammerReduceBottom, WoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionSpec hardHammerCleanLeftA = ActionSpec.create()
        .cutout(Shape.triFrom(0, 0).size(2, 2))
        .clearance(Shape.from(0, 0)
            .to(2, 0).to(2, -2)
            .to(4,-2).to(4, -4)
            .to(-4, -4)
            .to(-4, 4).to(-2, 4)
            .to(-2, 2).to(0, 2).build())
        .origin(PointF.at(1f, 1f))
        .build();
    public static final ActionSpec hardHammerCleanLeftB = hardHammerCleanLeftA.flip(Orientation.VERTICAL);
    public static final ActionSpec hardHammerCleanRightA = hardHammerCleanLeftA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec hardHammerCleanRightB = hardHammerCleanRightA.flip(Orientation.VERTICAL);
    public static final ActionGroup hardHammerClean = ActionGroup.create("hardHammerClean")
        .damage(0.25f)
        .usage(FlintKnappingMaterials.FLINT_RAW)
        .add(new Action("hardHammerCleanLeftA", hardHammerCleanLeftA, WoodworkingActionSide.LEFT))
        .add(new Action("hardHammerCleanLeftB", hardHammerCleanLeftB, WoodworkingActionSide.LEFT))
        .add(new Action("hardHammerCleanRightA", hardHammerCleanRightA, WoodworkingActionSide.RIGHT))
        .add(new Action("hardHammerCleanRightB", hardHammerCleanRightB, WoodworkingActionSide.RIGHT))
        .build();

    public static final ActionSpec softHammerReduceLeft = ActionSpec.create()
        .cutout(Shape.rectFrom(0, -1).size(2, 2))
        .clearance(Shape.rectFrom(0, -3).size(-4, 6))
        .origin(PointF.at(1f, 0f))
        .build();
    public static final ActionSpec softHammerReduceRight = softHammerReduceLeft.flip(Orientation.HORIZONTAL);
    public static final ActionSpec softHammerReduceTop = softHammerReduceRight.rotate(3);
    public static final ActionSpec softHammerReduceBottom = softHammerReduceTop.flip(Orientation.VERTICAL);
    public static final ActionGroup softHammerReduce = ActionGroup.create("softHammerReduce")
        .damage(0.25f)
        .usage(FlintKnappingMaterials.FLINT_CORE)
        .add(new Action("softHammerReduceLeft", softHammerReduceLeft, WoodworkingActionSide.LEFT))
        .add(new Action("softHammerReduceRight", softHammerReduceRight, WoodworkingActionSide.RIGHT))
        .add(new Action("softHammerReduceTop", softHammerReduceTop, WoodworkingActionSide.TOP))
        .add(new Action("softHammerReduceBottom", softHammerReduceBottom, WoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionSpec softHammerCleanLeftA = ActionSpec.create()
        .cutout(Shape.triFrom(0, 0).size(2, 2))
        .clearance(Shape.from(0, 0)
            .to(2, 0).to(2, -2)
            .to(4,-2).to(4, -4)
            .to(-4, -4)
            .to(-4, 4).to(-2, 4)
            .to(-2, 2).to(0, 2).build())
        .origin(PointF.at(1f, 1f))
        .build();
    public static final ActionSpec softHammerCleanLeftB = softHammerCleanLeftA.flip(Orientation.VERTICAL);
    public static final ActionSpec softHammerCleanRightA = softHammerCleanLeftA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec softHammerCleanRightB = softHammerCleanRightA.flip(Orientation.VERTICAL);
    public static final ActionGroup softHammerClean = ActionGroup.create("softHammerClean")
        .damage(0.125f)
        .usage(FlintKnappingMaterials.FLINT_CORE)
        .add(new Action("softHammerCleanLeftA", softHammerCleanLeftA, WoodworkingActionSide.LEFT))
        .add(new Action("softHammerCleanLeftB", softHammerCleanLeftB, WoodworkingActionSide.LEFT))
        .add(new Action("softHammerCleanRightA", softHammerCleanRightA, WoodworkingActionSide.RIGHT))
        .add(new Action("softHammerCleanRightB", softHammerCleanRightB, WoodworkingActionSide.RIGHT))
        .build();

    public static final ActionSpec pressureReduceLeft = ActionSpec.create()
        .cutout(Shape.rectFrom(0, 0).size(1, 1))
        .clearance(Shape.rectFrom(0, 0).size(-1, 1))
        .clearance(Shape.rectFrom(-1, -1).size(-1, 3))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec pressureReduceRight = pressureReduceLeft.flip(Orientation.HORIZONTAL);
    public static final ActionSpec pressureReduceTop = pressureReduceRight.rotate(3);
    public static final ActionSpec pressureReduceBottom = pressureReduceTop.flip(Orientation.VERTICAL);
    public static final ActionGroup pressureReduce = ActionGroup.create("pressureReduce")
        .damage(0.1f)
        .usage(FlintKnappingMaterials.FLINT_CORE)
        .usage(FlintKnappingMaterials.FLINT_FLAKE)
        .add(new Action("pressureReduceLeft", pressureReduceLeft, WoodworkingActionSide.LEFT))
        .add(new Action("pressureReduceRight", pressureReduceRight, WoodworkingActionSide.RIGHT))
        .add(new Action("pressureReduceTop", pressureReduceTop, WoodworkingActionSide.TOP))
        .add(new Action("pressureReduceBottom", pressureReduceBottom, WoodworkingActionSide.BOTTOM))
        .build();

    public static final ActionSpec pressureCleanLeftA = ActionSpec.create()
        .cutout(Shape.triFrom(0, 0).size(1, 1))
        .clearance(Shape.rectFrom(0, 0).size(-1, 1))
        .clearance(Shape.rectFrom(1, 0).size(-2, -1))
        .origin(PointF.at(0.5f, 0.5f))
        .build();
    public static final ActionSpec pressureCleanLeftB = pressureCleanLeftA.flip(Orientation.VERTICAL);
    public static final ActionSpec pressureCleanRightA = pressureCleanLeftA.flip(Orientation.HORIZONTAL);
    public static final ActionSpec pressureCleanRightB = pressureCleanRightA.flip(Orientation.VERTICAL);
    public static final ActionGroup pressureClean = ActionGroup.create("pressureClean")
        .damage(0.05f)
        .usage(FlintKnappingMaterials.FLINT_CORE)
        .usage(FlintKnappingMaterials.FLINT_FLAKE)
        .add(new Action("pressureCleanLeftA", pressureCleanLeftA, WoodworkingActionSide.LEFT))
        .add(new Action("pressureCleanLeftB", pressureCleanLeftB, WoodworkingActionSide.LEFT))
        .add(new Action("pressureCleanRightA", pressureCleanRightA, WoodworkingActionSide.RIGHT))
        .add(new Action("pressureCleanRightB", pressureCleanRightB, WoodworkingActionSide.RIGHT))
        .build();

}
