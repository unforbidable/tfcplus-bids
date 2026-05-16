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
    public static Block carvingRock = block(BlockNames.CARVING_ROCK);
    public static Block carvingWood = block(BlockNames.CARVING_WOOD);

    // Woodpile
    public static Block woodPile = block(BlockNames.WOODPILE);
    public static Block crackedStoneSed = block(BlockNames.CRACKED_STONE_SED);
    public static Block crackedStoneMM = block(BlockNames.CRACKED_STONE_MM);
    public static Block crackedStoneIgIn = block(BlockNames.CRACKED_STONE_IG_IN);
    public static Block crackedStoneIgEx = block(BlockNames.CRACKED_STONE_IG_EX);
    public static Block crackedOre = block(BlockNames.CRACKED_ORE);
    public static Block crackedOre1b = block(BlockNames.CRACKED_ORE_1B);
    public static Block crackedOre2 = block(BlockNames.CRACKED_ORE_2);
    public static Block crackedOre3 = block(BlockNames.CRACKED_ORE_3);
    public static Block light = block(BlockNames.LIGHT);

    // Firepit
    public static Block newFirepit = block(BlockNames.FIREPIT);
    public static Block tiedStickBundle = block(BlockNames.TIED_STICK_BUNDLE);

    // Rough stone
    public static Block roughStoneSed = block(BlockNames.ROUGH_STONE_SED);
    public static Block roughStoneBrickSed = block(BlockNames.ROUGH_STONE_BRICK_SED);
    public static Block roughStoneTileSed = block(BlockNames.ROUGH_STONE_TILE_SED);
    public static Block roughStoneBrickFenceSed = block(BlockNames.ROUGH_STONE_BRICK_FENCE_SED);
    public static Block roughStoneTileFenceSed = block(BlockNames.ROUGH_STONE_TILE_FENCE_SED);
    public static Block roughStoneMM = block(BlockNames.ROUGH_STONE_MM);
    public static Block roughStoneBrickMM = block(BlockNames.ROUGH_STONE_BRICK_MM);
    public static Block roughStoneTileMM = block(BlockNames.ROUGH_STONE_TILE_MM);
    public static Block roughStoneBrickFenceMM = block(BlockNames.ROUGH_STONE_BRICK_FENCE_MM);
    public static Block roughStoneTileFenceMM = block(BlockNames.ROUGH_STONE_TILE_FENCE_MM);
    public static Block roughStoneIgIn = block(BlockNames.ROUGH_STONE_IG_IN);
    public static Block roughStoneBrickIgIn = block(BlockNames.ROUGH_STONE_BRICK_IG_IN);
    public static Block roughStoneTileIgIn = block(BlockNames.ROUGH_STONE_TILE_IG_IN);
    public static Block roughStoneBrickFenceIgIn = block(BlockNames.ROUGH_STONE_BRICK_IG_IN);
    public static Block roughStoneTileFenceIgIn = block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_IN);
    public static Block roughStoneIgEx = block(BlockNames.ROUGH_STONE_BRICK_IG_EX);
    public static Block roughStoneBrickIgEx = block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_EX);
    public static Block roughStoneTileIgEx = block(BlockNames.ROUGH_STONE_TILE_IG_EX);
    public static Block roughStoneBrickFenceIgEx = block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_EX);
    public static Block roughStoneTileFenceIgEx = block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_EX);

    // Log Wall



    public static int clayCrucibleRenderId;
    public static int fireClayCrucibleRenderId;
    public static int quarryRenderId;
    public static int carvingRenderId;
    public static int woodPileRenderId;
    public static int dryingRackRenderId;
    public static int choppingBlockRenderId;
    public static int saddleQuernRenderId;
    public static int workStoneRenderId;
    public static int stonePressLeverRenderId;
    public static int stonePressWeightRenderId;
    public static int clayLampRenderId;
    public static int wallHookRenderId;

    public static int unfinishedAnvilStage1RenderId;
    public static int unfinishedAnvilStage2RenderId;
    public static int unfinishedAnvilStage3RenderId;
    public static int unfinishedAnvilStage4RenderId;
    public static int unfinishedAnvilStage5RenderId;
    public static int unfinishedAnvilStage6RenderId;

    public static int cookingPotRenderId;
    public static int cookingPotLidRenderId;

    public static int newCropsRenderId;

    public static int axleWallBearingRenderId;

    public static int newFirepitRenderId;

    public static int screwRenderId;
    public static int axleHandleRenderId;
    public static int screwPressRackRenderId;
    public static int screwPressBarrelRenderId;
    public static int screwPressDiscRenderId;
    public static int screwPressLeverRenderId;
    public static int palisadeRenderId;
    public static int roughStoneFenceRenderId;
    public static int wattleGateRenderId;
    public static int strawNestRenderId;
    public static int crackedStoneRenderId;
    public static int crackedOreRenderId;
    public static int processingSurfaceRenderId;
    public static int decorativeSurfaceRenderId;
    public static int soakingSurfaceRenderId;
    public static int dryingSurfaceRenderId;


    public static Block logWallEast;
    public static Block logWallNorth;
    public static Block logWallCorner;
    public static Block logWallEastAlt;
    public static Block logWallNorthAlt;
    public static Block logWallCornerAlt;

    public static Block logWallEast2;
    public static Block logWallNorth2;
    public static Block logWallCorner2;
    public static Block logWallEastAlt2;
    public static Block logWallNorthAlt2;
    public static Block logWallCornerAlt2;

    public static Block logWallEast3;
    public static Block logWallNorth3;
    public static Block logWallCorner3;
    public static Block logWallEastAlt3;
    public static Block logWallNorthAlt3;
    public static Block logWallCornerAlt3;

    public static Block logWallVert;
    public static Block logWallVertAlt;
    public static Block logWallVert2;
    public static Block logWallVertAlt2;
    public static Block logWallVert3;
    public static Block logWallVertAlt3;

    public static Block stackedFirewood;
    public static Block stackedFirewood2;
    public static Block stackedFirewood3;

    public static Block dryingRack;

    public static Block choppingBlock;
    public static Block choppingBlock2;
    public static Block choppingBlock3;

    public static Block wattleTrapdoor;
    public static Block wattleTrapdoorCover;

    public static Block saddleQuernBaseSed;
    public static Block saddleQuernHandstoneSed;
    public static Block saddleQuernPressingStoneSed;
    public static Block stonePressLever;
    public static Block stonePressWeightSed;

    public static Block clayLamp;

    public static Block wallHook;

    public static Block aquifer;
    public static Block aquifer2;

    public static Block unfinishedAnvilStage1;
    public static Block unfinishedAnvilStage2;
    public static Block unfinishedAnvilStage3;
    public static Block unfinishedAnvilStage4;
    public static Block unfinishedAnvilStage5;
    public static Block unfinishedAnvilStage6;

    public static Block cookingPot;
    public static Block cookingPotLid;

    public static Block steamingMesh;

    public static Block cookingPrep;

    public static Block newCrops;
    public static Block newTilledSoil;
    public static Block newTilledSoil2;

    public static Block woodAxleWallBearing;

    public static Block woodScrew;

    public static Block screwPressRackBottom;
    public static Block screwPressRackMiddle;
    public static Block screwPressRackTop;
    public static Block screwPressRackBridge;
    public static Block screwPressBarrel;
    public static Block screwPressDisc;
    public static Block screwPressLever;
    public static Block screwPressLeverTop;

    public static Block palisade;
    public static Block palisade2;
    public static Block palisade3;

    public static Block wattleGate;

    public static Block strawNest;

    public static Block fireBrickChimney;

    public static Block processingSurface;

    public static Block decorativeSurface;

    public static Block soakingSurface;
    public static Block dryingSurface;

    private static Block block(String name) {
        Block block = GameRegistry.findBlock(Tags.MOD_ID, name);
        if (block == null) {
            Bids.LOG.error("Block not found in game registry: {}", name);
        }

        return block;
    }

}
