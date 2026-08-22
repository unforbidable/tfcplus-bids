package com.unforbidable.tfc.bids.api.names;

import com.unforbidable.tfc.bids.api.annotations.BlockId;

public interface BlockNames {

    // Block ids help ensure the sequence of block registration remains unchanged
    // during the entire lifetime of the mod

    // Crucible
    @BlockId(0)
    String CLAY_CRUCIBLE = "ClayCrucible";
    @BlockId(1)
    String FIRE_CLAY_CRUCIBLE = "FireClayCrucible";

    // Mud Brick
    @BlockId(2)
    String MUD_BRICK_CHIMNEY = "MudBrickChimney";
    @BlockId(3)
    String MUD_BRICK_CHIMNEY_2 = "MudBrickChimney2";

    // Quarry
    @BlockId(4)
    String QUARRY = "Quary";

    // Carving
    @BlockId(5)
    String CARVING_ROCK = "CarvingRock";
    @BlockId(6)
    String CARVING_WOOD = "CarvingWood";

    // Woodpile
    @BlockId(7)
    String WOODPILE = "WoodPile";

    // Firepit
    @BlockId(8)
    String FIREPIT = "NewFirepit";
    @BlockId(29)
    String TIED_STICK_BUNDLE = "TiedStickBundle";

    // Rough Stone
    @BlockId(9)
    String ROUGH_STONE_SED = "RoughStoneSed";
    @BlockId(10)
    String ROUGH_STONE_BRICK_SED = "RoughStoneBrickSed";
    @BlockId(54)
    String ROUGH_STONE_TILE_SED = "RoughStoneTileSed";
    @BlockId(55)
    String ROUGH_STONE_MM = "RoughStoneMM";
    @BlockId(56)
    String ROUGH_STONE_BRICK_MM = "RoughStoneBrickMM";
    @BlockId(57)
    String ROUGH_STONE_TILE_MM = "RoughStoneTileMM";
    @BlockId(63)
    String ROUGH_STONE_IG_IN = "RoughStoneIgIn";
    @BlockId(64)
    String ROUGH_STONE_BRICK_IG_IN = "RoughStoneBrickIgIn";
    @BlockId(65)
    String ROUGH_STONE_TILE_IG_IN = "RoughStoneTileIgIn";
    @BlockId(66)
    String ROUGH_STONE_IG_EX = "RoughStoneIgEx";
    @BlockId(67)
    String ROUGH_STONE_BRICK_IG_EX = "RoughStoneBrickIgEx";
    @BlockId(68)
    String ROUGH_STONE_TILE_IG_EX = "RoughStoneTileIgEx";
    @BlockId(102)
    String ROUGH_STONE_BRICK_FENCE_SED = "RoughStoneBrickFenceSed";
    @BlockId(103)
    String ROUGH_STONE_BRICK_FENCE_MM = "RoughStoneBrickFenceMM";
    @BlockId(104)
    String ROUGH_STONE_BRICK_FENCE_IG_IN = "RoughStoneBrickFenceIgIn";
    @BlockId(105)
    String ROUGH_STONE_BRICK_FENCE_IG_EX = "RoughStoneBrickFenceIgEx";
    @BlockId(106)
    String ROUGH_STONE_TILE_FENCE_SED = "RoughStoneTileFenceSed";
    @BlockId(107)
    String ROUGH_STONE_TILE_FENCE_MM = "RoughStoneTileFenceMM";
    @BlockId(108)
    String ROUGH_STONE_TILE_FENCE_IG_IN = "RoughStoneTileFenceIgIn";
    @BlockId(109)
    String ROUGH_STONE_TILE_FENCE_IG_EX = "RoughStoneTileFenceIgEx";

    // Log Wall
    @BlockId(11)
    String LOG_WALL_EAST = "LogWallEast";
    @BlockId(12)
    String LOG_WALL_NORTH = "LogWallNorth";
    @BlockId(13)
    String LOG_WALL_CORNER = "LogWallCorner";
    @BlockId(14)
    String LOG_WALL_EAST_ALT = "LogWallEastAlt";
    @BlockId(15)
    String LOG_WALL_NORTH_ALT = "LogWallNorthAlt";
    @BlockId(16)
    String LOG_WALL_CORNER_ALT = "LogWallCornerAlt";
    @BlockId(17)
    String LOG_WALL_EAST_2 = "LogWallEast2";
    @BlockId(18)
    String LOG_WALL_NORTH_2 = "LogWallNorth2";
    @BlockId(19)
    String LOG_WALL_CORNER_2 = "LogWallCorner2";
    @BlockId(20)
    String LOG_WALL_EAST_ALT_2 = "LogWallEastAlt2";
    @BlockId(21)
    String LOG_WALL_NORTH_ALT_2 = "LogWallNorthAlt2";
    @BlockId(22)
    String LOG_WALL_CORNER_ALT_2 = "LogWallCornerAlt2";
    @BlockId(23)
    String LOG_WALL_EAST_3 = "LogWallEast3";
    @BlockId(24)
    String LOG_WALL_NORTH_3 = "LogWallNorth3";
    @BlockId(25)
    String LOG_WALL_CORNER_3 = "LogWallCorner3";
    @BlockId(26)
    String LOG_WALL_EAST_ALT_3 = "LogWallEastAlt3";
    @BlockId(27)
    String LOG_WALL_NORTH_ALT_3 = "LogWallNorthAlt3";
    @BlockId(28)
    String LOG_WALL_CORNER_ALT_3 = "LogWallCornerAlt3";
    @BlockId(41)
    String LOG_WALL_VERT = "LogWallVert";
    @BlockId(42)
    String LOG_WALL_VERT_ALT = "LogWallVertAlt";
    @BlockId(43)
    String LOG_WALL_VERT_2 = "LogWallVert2";
    @BlockId(44)
    String LOG_WALL_VERT_ALT_2 = "LogWallVertAlt2";
    @BlockId(45)
    String LOG_WALL_VERT_3 = "LogWallVert3";
    @BlockId(46)
    String LOG_WALL_VERT_ALT_3 = "LogWallVertAlt3";

    // Drying Rack
    @BlockId(30)
    String DRYING_RACK = "DryingRack";

    // Firewood
    @BlockId(31)
    String STACKED_FIREWOOD = "StackedFirewood";
    @BlockId(32)
    String STACKED_FIREWOOD_2 = "StackedFirewood2";
    @BlockId(33)
    String STACKED_FIREWOOD_3 = "StackedFirewood3";

    // Chopping Block
    @BlockId(34)
    String CHOPPING_BLOCK = "ChoppingBlock";
    @BlockId(35)
    String CHOPPING_BLOCK_2 = "ChoppingBlock2";
    @BlockId(36)
    String CHOPPING_BLOCK_3 = "ChoppingBlock3";

    // Wattle
    @BlockId(37)
    String WATTLE_TRAPDOOR = "WattleTrapDoor";
    @BlockId(38)
    String WATTLE_TRAPDOOR_COVER = "WattleTrapDoorCover";
    @BlockId(89)
    String WATTLE_GATE = "WattleGate";

    // Saddle Quern / Stone Press
    @BlockId(39)
    String SADDLE_QUERN_BASE = "SaddleQuernBaseSed";
    @BlockId(40)
    String SADDLE_QUERN_HANDSTONE = "SaddleQuernHandstoneSed";
    @BlockId(47)
    String SADDLE_QUERN_PRESSING_STONE = "SaddleQuernPressingStoneSed";
    @BlockId(48)
    String STONE_PRESS_LEVER = "StonePressLever";
    @BlockId(49)
    String STONE_PRESS_WEIGHT = "StonePressWeightSed";

    // Lamp
    @BlockId(50)
    String CLAY_LAMP = "ClayLamp";

    // Wall Hook
    @BlockId(51)
    String WALL_HOOK = "WallHook";

    // Aquifer
    @BlockId(52)
    String AQUIFER = "Aquifer";
    @BlockId(53)
    String AQUIFER_2 = "Aquifer2";

    // Unfinished Anvil
    @BlockId(58)
    String UNFINISHED_ANVIL_STAGE_1 = "UnfinishedAnvilStage1";
    @BlockId(59)
    String UNFINISHED_ANVIL_STAGE_2 = "UnfinishedAnvilStage2";
    @BlockId(60)
    String UNFINISHED_ANVIL_STAGE_3 = "UnfinishedAnvilStage3";
    @BlockId(61)
    String UNFINISHED_ANVIL_STAGE_4 = "UnfinishedAnvilStage4";
    @BlockId(62)
    String UNFINISHED_ANVIL_STAGE_5 = "UnfinishedAnvilStage5";
    @BlockId(91)
    String UNFINISHED_ANVIL_STAGE_6 = "UnfinishedAnvilStage6";

    // Cooking
    @BlockId(69)
    String COOKING_POT = "CookingPot";
    @BlockId(70)
    String COOKING_POT_LID = "CookingPotLid";
    @BlockId(71)
    String STEAMING_MESH = "SteamingMesh";
    @BlockId(72)
    String COOKING_PREP = "CookingPrep";

    // Crops
    @BlockId(73)
    String CROP = "NewCrop";
    @BlockId(74)
    String TILLED_SOIL = "NewTilledSoil";
    @BlockId(75)
    String TILLED_SOIL2 = "NewTilledSoil2";

    // Wall bearing
    @BlockId(76)
    String WOOD_AXLE_WALL_BEARING = "WoodAxleWallBearing";

    // Screw
    @BlockId(77)
    String WOOD_SCREW = "WoodScrew";

    // Screw Press
    @BlockId(78)
    String SCREW_PRESS_RACK_BOTTOM = "ScrewPressRackBottom";
    @BlockId(79)
    String SCREW_PRESS_RACK_MIDDLE = "ScrewPressRackMiddle";
    @BlockId(80)
    String SCREW_PRESS_RACK_TOP = "ScrewPressRackTop";
    @BlockId(81)
    String SCREW_PRESS_RACK_BRIDGE = "ScrewPressRackBridge";
    @BlockId(82)
    String SCREW_PRESS_BARREL = "ScrewPressBarrel";
    @BlockId(83)
    String SCREW_PRESS_DISC = "ScrewPressDisc";
    @BlockId(84)
    String SCREW_PRESS_LEVER = "ScrewPressLever";
    @BlockId(85)
    String SCREW_PRESS_LEVER_TOP = "ScrewPressLeverTop";

    // Palisade
    @BlockId(86)
    String PALISADE = "Palisade";
    @BlockId(87)
    String PALISADE_2 = "Palisade2";
    @BlockId(88)
    String PALISADE_3 = "Palisade3";

    // Straw Nest
    @BlockId(90)
    String STRAW_NEST = "StrawNest";

    // Fire-Setting
    @BlockId(92)
    String CRACKED_STONE_SED = "CrackedStoneSed";
    @BlockId(93)
    String CRACKED_STONE_MM = "CrackedStoneMM";
    @BlockId(94)
    String CRACKED_STONE_IG_IN = "CrackedStoneIgIn";
    @BlockId(95)
    String CRACKED_STONE_IG_EX = "CrackedStoneIgEx";
    @BlockId(96)
    String CRACKED_ORE = "CrackedOre";
    @BlockId(97)
    String CRACKED_ORE_1B = "CrackedOre1b";
    @BlockId(98)
    String CRACKED_ORE_2 = "CrackedOre2";
    @BlockId(99)
    String CRACKED_ORE_3 = "CrackedOre3";

    // Firebrick
    @BlockId(100)
    String FIREBRICK_CHIMNEY = "FireBrickChimney";

    // Light
    @BlockId(101)
    String LIGHT = "Light";

    // Processing Surface
    @BlockId(110)
    String PROCESSING_SURFACE = "ProcessingSurface";

    // Decorative Surface
    @BlockId(111)
    String DECORATIVE_SURFACE = "DecorativeSurface";

    // Soaking Surface
    @BlockId(112)
    String SOAKING_SURFACE = "SoakingSurface";

    // Drying Surface
    @BlockId(113)
    String DRYING_SURFACE = "DryingSurface";

    // Flora
    @BlockId(114)
    String MORE_GRASS = "MoreGrass";

    // Peg
    @BlockId(115)
    String WOODEN_PEG = "WoodenPeg";

    @BlockId(115)
    String DRYING_PEGS = "DryingPegs";

}
