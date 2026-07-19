package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BidsBlocks {

    // BidsBlocks is not to be referred to during initialization before blocks are registered
    // use core.features.registry.FeatureRegistryLookup instead during initialization

    // Crucible
    public static final Block clayCrucible = block(BlockNames.CLAY_CRUCIBLE);
    public static final Block fireClayCrucible = block(BlockNames.FIRE_CLAY_CRUCIBLE);

    // Mudbrick
    public static final Block mudBrickChimney = block(BlockNames.MUD_BRICK_CHIMNEY);
    public static final Block mudBrickChimney2 = block(BlockNames.MUD_BRICK_CHIMNEY_2);

    // Quarry
    public static final Block quarry = block(BlockNames.QUARRY);

    // Carving
    public static final Block carvingRock = block(BlockNames.CARVING_ROCK);
    public static final Block carvingWood = block(BlockNames.CARVING_WOOD);

    // Woodpile
    public static final Block woodPile = block(BlockNames.WOODPILE);
    public static final Block crackedStoneSed = block(BlockNames.CRACKED_STONE_SED);
    public static final Block crackedStoneMM = block(BlockNames.CRACKED_STONE_MM);
    public static final Block crackedStoneIgIn = block(BlockNames.CRACKED_STONE_IG_IN);
    public static final Block crackedStoneIgEx = block(BlockNames.CRACKED_STONE_IG_EX);
    public static final Block crackedOre = block(BlockNames.CRACKED_ORE);
    public static final Block crackedOre1b = block(BlockNames.CRACKED_ORE_1B);
    public static final Block crackedOre2 = block(BlockNames.CRACKED_ORE_2);
    public static final Block crackedOre3 = block(BlockNames.CRACKED_ORE_3);
    public static final Block light = block(BlockNames.LIGHT);

    // Firepit
    public static final Block newFirepit = block(BlockNames.FIREPIT);
    public static final Block tiedStickBundle = block(BlockNames.TIED_STICK_BUNDLE);

    // Rough stone
    public static final Block roughStoneSed = block(BlockNames.ROUGH_STONE_SED);
    public static final Block roughStoneBrickSed = block(BlockNames.ROUGH_STONE_BRICK_SED);
    public static final Block roughStoneTileSed = block(BlockNames.ROUGH_STONE_TILE_SED);
    public static final Block roughStoneBrickFenceSed = block(BlockNames.ROUGH_STONE_BRICK_FENCE_SED);
    public static final Block roughStoneTileFenceSed = block(BlockNames.ROUGH_STONE_TILE_FENCE_SED);
    public static final Block roughStoneMM = block(BlockNames.ROUGH_STONE_MM);
    public static final Block roughStoneBrickMM = block(BlockNames.ROUGH_STONE_BRICK_MM);
    public static final Block roughStoneTileMM = block(BlockNames.ROUGH_STONE_TILE_MM);
    public static final Block roughStoneBrickFenceMM = block(BlockNames.ROUGH_STONE_BRICK_FENCE_MM);
    public static final Block roughStoneTileFenceMM = block(BlockNames.ROUGH_STONE_TILE_FENCE_MM);
    public static final Block roughStoneIgIn = block(BlockNames.ROUGH_STONE_IG_IN);
    public static final Block roughStoneBrickIgIn = block(BlockNames.ROUGH_STONE_BRICK_IG_IN);
    public static final Block roughStoneTileIgIn = block(BlockNames.ROUGH_STONE_TILE_IG_IN);
    public static final Block roughStoneBrickFenceIgIn = block(BlockNames.ROUGH_STONE_BRICK_IG_IN);
    public static final Block roughStoneTileFenceIgIn = block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_IN);
    public static final Block roughStoneIgEx = block(BlockNames.ROUGH_STONE_BRICK_IG_EX);
    public static final Block roughStoneBrickIgEx = block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_EX);
    public static final Block roughStoneTileIgEx = block(BlockNames.ROUGH_STONE_TILE_IG_EX);
    public static final Block roughStoneBrickFenceIgEx = block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_EX);
    public static final Block roughStoneTileFenceIgEx = block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_EX);

    // Log Wall
    public static final Block logWallEast = block(BlockNames.LOG_WALL_EAST);
    public static final Block logWallNorth = block(BlockNames.LOG_WALL_NORTH);
    public static final Block logWallCorner = block(BlockNames.LOG_WALL_CORNER);
    public static final Block logWallEastAlt = block(BlockNames.LOG_WALL_EAST_ALT);
    public static final Block logWallNorthAlt = block(BlockNames.LOG_WALL_NORTH_ALT);
    public static final Block logWallCornerAlt = block(BlockNames.LOG_WALL_CORNER_ALT);

    public static final Block logWallEast2 = block(BlockNames.LOG_WALL_EAST_2);
    public static final Block logWallNorth2 = block(BlockNames.LOG_WALL_NORTH_2);
    public static final Block logWallCorner2 = block(BlockNames.LOG_WALL_CORNER_2);
    public static final Block logWallEastAlt2 = block(BlockNames.LOG_WALL_EAST_ALT_2);
    public static final Block logWallNorthAlt2 = block(BlockNames.LOG_WALL_NORTH_ALT_2);
    public static final Block logWallCornerAlt2 = block(BlockNames.LOG_WALL_CORNER_ALT_2);

    public static final Block logWallEast3 = block(BlockNames.LOG_WALL_EAST_3);
    public static final Block logWallNorth3 = block(BlockNames.LOG_WALL_NORTH_3);
    public static final Block logWallCorner3 = block(BlockNames.LOG_WALL_CORNER_3);
    public static final Block logWallEastAlt3 = block(BlockNames.LOG_WALL_EAST_ALT_3);
    public static final Block logWallNorthAlt3 = block(BlockNames.LOG_WALL_NORTH_ALT_3);
    public static final Block logWallCornerAlt3 = block(BlockNames.LOG_WALL_CORNER_ALT_3);

    public static final Block logWallVert = block(BlockNames.LOG_WALL_VERT);
    public static final Block logWallVertAlt = block(BlockNames.LOG_WALL_VERT_ALT);
    public static final Block logWallVert2 = block(BlockNames.LOG_WALL_VERT_2);
    public static final Block logWallVertAlt2 = block(BlockNames.LOG_WALL_VERT_ALT_2);
    public static final Block logWallVert3 = block(BlockNames.LOG_WALL_VERT_3);
    public static final Block logWallVertAlt3 = block(BlockNames.LOG_WALL_VERT_ALT_3);

    // Drying rack
    public static final Block dryingRack = block(BlockNames.DRYING_RACK);

    // Firewood
    public static final Block stackedFirewood = block(BlockNames.STACKED_FIREWOOD);
    public static final Block stackedFirewood2 = block(BlockNames.STACKED_FIREWOOD_2);
    public static final Block stackedFirewood3 = block(BlockNames.STACKED_FIREWOOD_3);

    // Chopping block
    public static final Block choppingBlock = block(BlockNames.CHOPPING_BLOCK);
    public static final Block choppingBlock2 = block(BlockNames.CHOPPING_BLOCK_2);
    public static final Block choppingBlock3 = block(BlockNames.CHOPPING_BLOCK_3);

    // Wattle
    public static final Block wattleTrapdoor = block(BlockNames.WATTLE_TRAPDOOR);
    public static final Block wattleTrapdoorCover = block(BlockNames.WATTLE_TRAPDOOR_COVER);
    public static final Block wattleGate = block(BlockNames.WATTLE_GATE);

    // Saddle quern
    public static final Block saddleQuernBaseSed = block(BlockNames.SADDLE_QUERN_BASE);
    public static final Block saddleQuernHandstoneSed = block(BlockNames.SADDLE_QUERN_HANDSTONE);
    public static final Block saddleQuernPressingStoneSed = block(BlockNames.SADDLE_QUERN_PRESSING_STONE);
    public static final Block stonePressLever = block(BlockNames.STONE_PRESS_LEVER);
    public static final Block stonePressWeightSed = block(BlockNames.STONE_PRESS_WEIGHT);

    // Lamp
    public static final Block clayLamp = block(BlockNames.CLAY_LAMP);

    // Wall hook
    public static final Block wallHook = block(BlockNames.WALL_HOOK);

    // Well
    public static final Block aquifer = block(BlockNames.AQUIFER);
    public static final Block aquifer2 = block(BlockNames.AQUIFER_2);

    // Anvil
    public static final Block unfinishedAnvilStage1 = block(BlockNames.UNFINISHED_ANVIL_STAGE_1);
    public static final Block unfinishedAnvilStage2 = block(BlockNames.UNFINISHED_ANVIL_STAGE_2);
    public static final Block unfinishedAnvilStage3 = block(BlockNames.UNFINISHED_ANVIL_STAGE_3);
    public static final Block unfinishedAnvilStage4 = block(BlockNames.UNFINISHED_ANVIL_STAGE_4);
    public static final Block unfinishedAnvilStage5 = block(BlockNames.UNFINISHED_ANVIL_STAGE_5);
    public static final Block unfinishedAnvilStage6 = block(BlockNames.UNFINISHED_ANVIL_STAGE_6);

    // Cooking Pot
    public static final Block cookingPot = block(BlockNames.COOKING_POT);
    public static final Block cookingPotLid = block(BlockNames.COOKING_POT_LID);
    public static final Block steamingMesh = block(BlockNames.STEAMING_MESH);

    // Cooking Prep
    public static final Block cookingPrep = block(BlockNames.COOKING_PREP);

    // Crops
    public static final Block newCrops = block(BlockNames.CROP);
    public static final Block newTilledSoil = block(BlockNames.TILLED_SOIL);
    public static final Block newTilledSoil2 = block(BlockNames.TILLED_SOIL2);

    // Wall bearing
    public static final Block woodAxleWallBearing = block(BlockNames.WOOD_AXLE_WALL_BEARING);

    // Screw
    public static final Block woodScrew = block(BlockNames.WOOD_SCREW);

    // Screw press
    public static final Block screwPressRackBottom = block(BlockNames.SCREW_PRESS_RACK_BOTTOM);
    public static final Block screwPressRackMiddle = block(BlockNames.SCREW_PRESS_RACK_MIDDLE);
    public static final Block screwPressRackTop = block(BlockNames.SCREW_PRESS_RACK_TOP);
    public static final Block screwPressRackBridge = block(BlockNames.SCREW_PRESS_RACK_BRIDGE);
    public static final Block screwPressBarrel = block(BlockNames.SCREW_PRESS_BARREL);
    public static final Block screwPressDisc = block(BlockNames.SCREW_PRESS_DISC);
    public static final Block screwPressLever = block(BlockNames.SCREW_PRESS_LEVER);
    public static final Block screwPressLeverTop = block(BlockNames.SCREW_PRESS_LEVER_TOP);

    // Palisade
    public static final Block palisade = block(BlockNames.PALISADE);
    public static final Block palisade2 = block(BlockNames.PALISADE_2);
    public static final Block palisade3 = block(BlockNames.PALISADE_3);

    // Straw Nest
    public static final Block strawNest = block(BlockNames.STRAW_NEST);

    // Firebrick
    public static final Block fireBrickChimney = block(BlockNames.FIREBRICK_CHIMNEY);

    // Processing surface
    public static final Block processingSurface = block(BlockNames.PROCESSING_SURFACE);

    // Decorative surface
    public static final Block decorativeSurface = block(BlockNames.DECORATIVE_SURFACE);

    // Soaking surface
    public static final Block soakingSurface = block(BlockNames.SOAKING_SURFACE);

    // Drying surface
    public static final Block dryingSurface = block(BlockNames.DRYING_SURFACE);

    private static Block block(String name) {
        Block block = GameRegistry.findBlock(Tags.MOD_ID, name);
        if (block == null) {
            Bids.LOG.error("Block not found in game registry: {}", name);
        }

        return block;
    }

}
