package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Items.ItemBlocks.ItemSoil;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.*;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.*;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackMessage;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Network.NetworkHelper;
import com.unforbidable.tfc.bids.Core.Quarry.Quarriables.QuarriableStone;
import com.unforbidable.tfc.bids.Core.SaddleQuern.EnumWorkStoneType;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlocks.CrackableBlockOre;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlocks.CrackableBlockStone;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileMessage;
import com.unforbidable.tfc.bids.Items.ItemBlocks.*;
import com.unforbidable.tfc.bids.Render.Blocks.*;
import com.unforbidable.tfc.bids.Render.Tiles.*;
import com.unforbidable.tfc.bids.TileEntities.*;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallVertType;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlockSetup extends BidsBlocks {

    public static void preInit() {
        initBlocks();
        setupHarvest();
        registerBlocks();
        registerOre();
        registerCarvings();
        registerQuarryBlocks();
        registerCrackableBlocks();
        registerTileEntities();
        registerMessages();
    }

    public static void postInit() {
        updateBlocks();
        setupFireInfo();
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClientOnly() {
        registerBlockRenderers();
        registerTileEntitiesClientOnly();
    }

    private static void initBlocks() {
        Bids.LOG.info("Initialize blocks");

        final float clayCrucibleHardness = BidsOptions.Crucible.enableClayHandBreakable ? 0.5f : 4.0f;
        final float fireClayCrucibleHardness = BidsOptions.Crucible.enableFireClayHandBreakable ? 0.5f : 4.0f;
        clayCrucible = new BlockClayCrucible().setBlockName("ClayCrucible")
                .setBlockTextureName("Pottery Crucible")
                .setHardness(clayCrucibleHardness);
        fireClayCrucible = new BlockFireClayCrucible().setBlockName("FireClayCrucible")
                .setBlockTextureName("Fire Clay Crucible")
                .setHardness(fireClayCrucibleHardness);

        mudBrickChimney = new BlockMudChimney(0).setDirt(TFCBlocks.dirt)
                .setBlockName("MudBrickChimney");
        mudBrickChimney2 = new BlockMudChimney(16).setDirt(TFCBlocks.dirt2)
                .setBlockName("MudBrickChimney2");

        quarry = new BlockQuarry().setBlockName("Quarry");

        carvingRock = new BlockCarving(Material.rock).setBlockName("CarvingRock");
        carvingWood = new BlockCarving(Material.wood).setBlockName("CarvingWood");

        woodPile = new BlockWoodPile().setBlockName("WoodPile")
                .setBlockTextureName("Wood Pile");

        newFirepit = new BlockNewFirepit().setBlockName("NewFirepit");

        roughStoneSed = new BlockRoughStone()
                .setMetaHavingTopTexture(0, 4) // Shale and Sandstone
                .setNames(Global.STONE_SED).setBlockName("RoughStoneSed")
                .setBlockTextureName("Rough");
        roughStoneMM = new BlockRoughStone()
                .setMetaHavingTopTexture(1, 2, 3) // Slate, Shist and Phyllite
                .setNames(Global.STONE_MM).setBlockName("RoughStoneMM")
                .setBlockTextureName("Rough");
        roughStoneIgIn = new BlockRoughStone()
                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneIgIn")
                .setBlockTextureName("Rough");
        roughStoneIgEx = new BlockRoughStone()
                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneIgEx")
                .setBlockTextureName("Rough");

        roughStoneBrickSed = new BlockRoughStoneBrick()
                .setMetaHavingTopTexture(0, 4) // Shale and Sandstone
                .setNames(Global.STONE_SED).setBlockName("RoughStoneBrickSed")
                .setBlockTextureName("Rough Brick");
        roughStoneBrickMM = new BlockRoughStoneBrick()
                .setMetaHavingTopTexture(1, 2, 3) // Slate, Shist and Phyllite
                .setNames(Global.STONE_MM).setBlockName("RoughStoneBrickMM")
                .setBlockTextureName("Rough Brick");
        roughStoneBrickIgIn = new BlockRoughStoneBrick()
                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneBrickIgIn")
                .setBlockTextureName("Rough Brick");
        roughStoneBrickIgEx = new BlockRoughStoneBrick()
                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneBrickIgEx")
                .setBlockTextureName("Rough Brick");

        roughStoneTileSed = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setNames(Global.STONE_SED).setBlockName("RoughStoneTileSed")
                .setBlockTextureName("Rough Tile");
        roughStoneTileMM = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setNames(Global.STONE_MM).setBlockName("RoughStoneTileMM")
                .setBlockTextureName("Rough Tile");
        roughStoneTileIgIn = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneTileIgIn")
                .setBlockTextureName("Rough Tile");
        roughStoneTileIgEx = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneTileIgEx")
                .setBlockTextureName("Rough Tile");

        logWallEast = new BlockLogWall(EnumLogWallType.EAST, 0).setBlockName("LogWallEast");
        logWallNorth = new BlockLogWall(EnumLogWallType.NORTH, 0).setBlockName("LogWallNorth");
        logWallCorner = new BlockLogWall(EnumLogWallType.CORNER, 0).setBlockName("LogWallCorner");
        logWallEastAlt = new BlockLogWall(EnumLogWallType.EAST_ALT, 0).setBlockName("LogWallEastAlt");
        logWallNorthAlt = new BlockLogWall(EnumLogWallType.NORTH_ALT, 0).setBlockName("LogWallNorthAlt");
        logWallCornerAlt = new BlockLogWall(EnumLogWallType.CORNER_ALT, 0).setBlockName("LogWallCornerAlt");

        logWallEast2 = new BlockLogWall(EnumLogWallType.EAST, 16).setBlockName("LogWallEast2");
        logWallNorth2 = new BlockLogWall(EnumLogWallType.NORTH, 16).setBlockName("LogWallNorth2");
        logWallCorner2 = new BlockLogWall(EnumLogWallType.CORNER, 16).setBlockName("LogWallCorner2");
        logWallEastAlt2 = new BlockLogWall(EnumLogWallType.EAST_ALT, 16).setBlockName("LogWallEastAlt2");
        logWallNorthAlt2 = new BlockLogWall(EnumLogWallType.NORTH_ALT, 16).setBlockName("LogWallNorthAlt2");
        logWallCornerAlt2 = new BlockLogWall(EnumLogWallType.CORNER_ALT, 16).setBlockName("LogWallCornerAlt2");

        logWallEast3 = new BlockLogWall(EnumLogWallType.EAST, 32).setBlockName("LogWallEast3");
        logWallNorth3 = new BlockLogWall(EnumLogWallType.NORTH, 32).setBlockName("LogWallNorth3");
        logWallCorner3 = new BlockLogWall(EnumLogWallType.CORNER, 32).setBlockName("LogWallCorner3");
        logWallEastAlt3 = new BlockLogWall(EnumLogWallType.EAST_ALT, 32).setBlockName("LogWallEastAlt3");
        logWallNorthAlt3 = new BlockLogWall(EnumLogWallType.NORTH_ALT, 32).setBlockName("LogWallNorthAlt3");
        logWallCornerAlt3 = new BlockLogWall(EnumLogWallType.CORNER_ALT, 32).setBlockName("LogWallCornerAlt3");

        logWallVert = new BlockLogWallVert(EnumLogWallVertType.DEFAULT, 0).setBlockName("LogWallVert");
        logWallVertAlt = new BlockLogWallVert(EnumLogWallVertType.ALT, 0).setBlockName("LogWallVertAlt");

        logWallVert2 = new BlockLogWallVert(EnumLogWallVertType.DEFAULT, 16).setBlockName("LogWallVert2");
        logWallVertAlt2 = new BlockLogWallVert(EnumLogWallVertType.ALT, 16).setBlockName("LogWallVertAlt2");

        logWallVert3 = new BlockLogWallVert(EnumLogWallVertType.DEFAULT, 32).setBlockName("LogWallVert3");
        logWallVertAlt3 = new BlockLogWallVert(EnumLogWallVertType.ALT, 32).setBlockName("LogWallVertAlt3");

        tiedStickBundle = new BlockTiedStickBundle().setBlockName("TiedStickBundle");

        stackedFirewood = new BlockStackedFirewood(0).setBlockName("StackedFirewood");
        stackedFirewood2 = new BlockStackedFirewood(16).setBlockName("StackedFirewood2");
        stackedFirewood3 = new BlockStackedFirewood(32).setBlockName("StackedFirewood3");

        dryingRack = new BlockDryingRack().setBlockName("DryingRack");

        choppingBlock = new BlockChoppingBlock(TFCBlocks.woodVert)
                .setBlockName("ChoppingBlock");
        choppingBlock2 = new BlockChoppingBlock(TFCBlocks.woodVert2)
                .setBlockName("ChoppingBlock2");
        choppingBlock3 = new BlockChoppingBlock(TFCBlocks.woodVert3)
                .setBlockName("ChoppingBlock3");

        wattleTrapdoor = new BlockWattleTrapDoor().setIgnoreRedstone(true)
                .setBlockName("WattleTrapDoor")
                .setBlockTextureName("Wattle Trap Door");
        wattleTrapdoorCover = new BlockWattleTrapDoorCover()
                .setBlockName("WattleTrapDoorCover");

        saddleQuernBaseSed = new BlockSaddleQuern((BlockRoughStone) roughStoneSed)
                .setBlockName("SaddleQuernSed");
        saddleQuernHandstoneSed = new BlockWorkStone((BlockRoughStone) roughStoneSed, EnumWorkStoneType.SADDLE_QUERN_CRUSHING)
                .setBlockName("SaddleQuernHandstoneSed");
        saddleQuernPressingStoneSed = new BlockWorkStone((BlockRoughStone) roughStoneSed, EnumWorkStoneType.SADDLE_QUERN_PRESSING)
                .setBlockName("SaddleQuernPressingStoneSed");
        stonePressLever = new BlockStonePressLever()
                .setBlockName("StonePressLever");
        stonePressWeightSed = new BlockStonePressWeight((BlockRoughStone) roughStoneSed)
                .setBlockName("StonePressWeight");

        clayLamp = new BlockClayLamp()
                .setBlockName("ClayLamp");

        wallHook = new BlockWallHook()
                .setBlockName("WallHook");

        aquifer = new BlockAquifer(0)
                .setGravelBlock(TFCBlocks.gravel)
                .setBlockName("Aquifer");
        aquifer2 = new BlockAquifer(16)
                .setGravelBlock(TFCBlocks.gravel2)
                .setBlockName("Aquifer2");

        unfinishedAnvilStage1 = new BlockUnfinishedAnvil(0)
            .setBlockName("UnfinishedAnvilStage1");
        unfinishedAnvilStage2 = new BlockUnfinishedAnvil(1)
            .setBlockName("UnfinishedAnvilStage2");
        unfinishedAnvilStage3 = new BlockUnfinishedAnvil(2)
            .setBlockName("UnfinishedAnvilStage3");
        unfinishedAnvilStage4 = new BlockUnfinishedAnvil(3)
            .setBlockName("UnfinishedAnvilStage4");
        unfinishedAnvilStage5 = new BlockUnfinishedAnvil(4)
            .setBlockName("UnfinishedAnvilStage5");
        unfinishedAnvilStage6 = new BlockUnfinishedAnvil(5)
            .setBlockName("UnfinishedAnvilStage6");

        cookingPot = new BlockCookingPot()
            .setBlockTextureName("Cooking Pot")
            .setBlockName("CookingPot");
        cookingPotLid = new BlockCookingPotLid()
            .setBlockTextureName("Cooking Pot Lid")
            .setBlockName("CookingPotLid");

        steamingMesh = new BlockSteamingMesh()
            .setBlockTextureName("Steaming Mesh")
            .setBlockName("SteamingMesh");

        cookingPrep = new BlockCookingPrep()
            .setBlockName("CookingPrep");

        newCrops = new BlockNewCrop()
            .setHardness(0.3F)
            .setStepSound(Block.soundTypeGrass)
            .setBlockName("NewCrop");
        newTilledSoil = new BlockNewFarmland(TFCBlocks.dirt, 0)
            .setHardness(2F)
            .setStepSound(Block.soundTypeGravel)
            .setBlockName("NewTilledSoil");
        newTilledSoil2 = new BlockNewFarmland(TFCBlocks.dirt2, 16)
            .setHardness(2F)
            .setStepSound(Block.soundTypeGravel)
            .setBlockName("NewTilledSoil");

        woodAxleWallBearing = new BlockAxleWallBearing(Material.wood)
            .setHardness(0.5F)
            .setBlockName("WoodAxleWallBearing");

        woodScrew = new BlockScrew(Material.wood)
            .setHardness(0.5F)
            .setBlockTextureName("Wood Screw")
            .setBlockName("WoodScrew");

        screwPressRackBottom = new BlockScrewPressRackBottom()
            .setBlockName("ScrewPressRackBottom");
        screwPressRackMiddle = new BlockScrewPressRackMiddle()
            .setBlockName("ScrewPressRackMiddle");
        screwPressRackTop = new BlockScrewPressRackTop()
            .setBlockName("ScrewPressRackTop");
        screwPressRackBridge = new BlockScrewPressRackBridge()
            .setBlockName("ScrewPressRackBridge");
        screwPressBarrel = new BlockScrewPressBarrel()
            .setBlockName("ScrewPressBarrel");
        screwPressDisc = new BlockScrewPressDisc()
            .setBlockName("ScrewPressDisc");
        screwPressLever = new BlockScrewPressLever()
            .setBlockName("ScrewPressLever");
        screwPressLeverTop = new BlockScrewPressLeverTop()
            .setBlockName("ScrewPressLeverTop");

        palisade = new BlockPalisade(logWallVert, 0)
            .setBlockName("Palisade");
        palisade2 = new BlockPalisade(logWallVert2, 16)
            .setBlockName("Palisade2");
        palisade3 = new BlockPalisade(logWallVert3, 32)
            .setBlockName("Palisade3");

        wattleGate = new BlockWattleGate()
            .setBlockTextureName("Wattle Gate")
            .setBlockName("WattleGate");

        strawNest = new BlockStrawNest()
            .setBlockName("StrawNest");

        crackedStoneSed = new BlockCrackedSed(Material.rock)
            .setHardness(3.5F)
            .setBlockName("CrackedSedRock");
        crackedStoneMM = new BlockCrackedMM(Material.rock)
            .setHardness(4F)
            .setBlockName("CrackedMMRock");
        crackedStoneIgIn = new BlockCrackedIgIn(Material.rock)
            .setHardness(4F)
            .setBlockName("CrackedIgInRock");
        crackedStoneIgEx = new BlockCrackedIgEx(Material.rock)
            .setHardness(4F)
            .setBlockName("CrackedIgExRock");

        crackedOre = new BlockCrackedOre(Material.rock)
            .setHardness(5F)
            .setResistance(5F)
            .setBlockName("Ore");
        crackedOre1b = new BlockCrackedOre(Material.rock)
            .setDamageOffset(16)
            .setHardness(5F)
            .setResistance(5F)
            .setBlockName("Ore");
        crackedOre2 = new BlockCrackedOre2(Material.rock)
            .setHardness(5F)
            .setResistance(5F)
            .setBlockName("Ore");
        crackedOre3 = new BlockCrackedOre3(Material.rock)
            .setHardness(5F)
            .setResistance(5F)
            .setBlockName("Ore");
    }

    private static void updateBlocks() {
        Bids.LOG.info("Update blocks");

        if (BidsOptions.Crucible.enableClassicHandBreakable) {
            // Lower the hardness of the classic TFC crucible
            // The original value is 4.0f
            Bids.LOG.info("Classic TFC crucible hardness reduced");
            TFCBlocks.crucible.setHardness(0.5f);
        }

        if (BidsOptions.Firepit.replaceFirepitTFC) {
            BidsBlocks.firepitTFC = TFCBlocks.firepit;
            TFCBlocks.firepit = BidsBlocks.newFirepit;
        }
    }

    private static void setupHarvest() {
        Bids.LOG.info("Set block harvestability");

        roughStoneSed.setHarvestLevel("shovel", 0);
        roughStoneBrickSed.setHarvestLevel("shovel", 0);
        roughStoneTileSed.setHarvestLevel("shovel", 0);
        roughStoneMM.setHarvestLevel("shovel", 0);
        roughStoneBrickMM.setHarvestLevel("shovel", 0);
        roughStoneTileMM.setHarvestLevel("shovel", 0);
        roughStoneIgIn.setHarvestLevel("shovel", 0);
        roughStoneBrickIgIn.setHarvestLevel("shovel", 0);
        roughStoneTileIgIn.setHarvestLevel("shovel", 0);
        roughStoneIgEx.setHarvestLevel("shovel", 0);
        roughStoneBrickIgEx.setHarvestLevel("shovel", 0);
        roughStoneTileIgEx.setHarvestLevel("shovel", 0);

        crackedStoneSed.setHarvestLevel("shovel", 0);
        crackedStoneMM.setHarvestLevel("shovel", 0);
        crackedStoneIgIn.setHarvestLevel("shovel", 0);
        crackedStoneIgEx.setHarvestLevel("shovel", 0);
        crackedOre.setHarvestLevel("shovel", 0);
        crackedOre1b.setHarvestLevel("shovel", 0);
        crackedOre2.setHarvestLevel("shovel", 0);
        crackedOre3.setHarvestLevel("shovel", 0);

        carvingRock.setHarvestLevel("shovel", 0);
        carvingWood.setHarvestLevel("axe", 0);

        saddleQuernBaseSed.setHarvestLevel("shovel", 0);
        stonePressWeightSed.setHarvestLevel("shovel", 0);

        quarry.setHarvestLevel("hammer", 0);

        mudBrickChimney.setHarvestLevel("shovel", 0);
        mudBrickChimney2.setHarvestLevel("shovel", 0);

        palisade.setHarvestLevel("axe", 0);
        palisade2.setHarvestLevel("axe", 0);
        palisade3.setHarvestLevel("axe", 0);

        wattleGate.setHarvestLevel("axe", 0);
    }

    private static void setupFireInfo() {
        Bids.LOG.info("Set block flammability");

        Blocks.fire.setFireInfo(logWallEast, 5, 5);
        Blocks.fire.setFireInfo(logWallNorth, 5, 5);
        Blocks.fire.setFireInfo(logWallCorner, 5, 5);
        Blocks.fire.setFireInfo(logWallEastAlt, 5, 5);
        Blocks.fire.setFireInfo(logWallNorthAlt, 5, 5);
        Blocks.fire.setFireInfo(logWallCornerAlt, 5, 5);
        Blocks.fire.setFireInfo(logWallEast2, 5, 5);
        Blocks.fire.setFireInfo(logWallNorth2, 5, 5);
        Blocks.fire.setFireInfo(logWallCorner2, 5, 5);
        Blocks.fire.setFireInfo(logWallEastAlt2, 5, 5);
        Blocks.fire.setFireInfo(logWallNorthAlt2, 5, 5);
        Blocks.fire.setFireInfo(logWallCornerAlt2, 5, 5);
        Blocks.fire.setFireInfo(logWallEast3, 5, 5);
        Blocks.fire.setFireInfo(logWallNorth3, 5, 5);
        Blocks.fire.setFireInfo(logWallCorner3, 5, 5);
        Blocks.fire.setFireInfo(logWallEastAlt3, 5, 5);
        Blocks.fire.setFireInfo(logWallNorthAlt3, 5, 5);
        Blocks.fire.setFireInfo(logWallCornerAlt3, 5, 5);

        Blocks.fire.setFireInfo(logWallVert, 5, 5);
        Blocks.fire.setFireInfo(logWallVert2, 5, 5);
        Blocks.fire.setFireInfo(logWallVert3, 5, 5);

        Blocks.fire.setFireInfo(carvingWood, 5, 5);

        Blocks.fire.setFireInfo(choppingBlock, 5, 5);

        Blocks.fire.setFireInfo(wattleTrapdoor, 10, 30);
        Blocks.fire.setFireInfo(wattleTrapdoorCover, 60, 20);

        Blocks.fire.setFireInfo(stonePressLever, 5, 5);

        Blocks.fire.setFireInfo(wallHook, 5, 5);

        Blocks.fire.setFireInfo(newCrops, 5, 5);

        Blocks.fire.setFireInfo(woodAxleWallBearing, 5, 5);

        Blocks.fire.setFireInfo(woodScrew, 5, 5);

        Blocks.fire.setFireInfo(screwPressRackBottom, 5, 5);
        Blocks.fire.setFireInfo(screwPressRackMiddle, 5, 5);
        Blocks.fire.setFireInfo(screwPressRackTop, 5, 5);
        Blocks.fire.setFireInfo(screwPressRackBridge, 5, 5);
        Blocks.fire.setFireInfo(screwPressBarrel, 5, 5);
        Blocks.fire.setFireInfo(screwPressDisc, 5, 5);
        Blocks.fire.setFireInfo(screwPressLever, 5, 5);
        Blocks.fire.setFireInfo(screwPressLeverTop, 5, 5);

        Blocks.fire.setFireInfo(palisade, 5, 5);
        Blocks.fire.setFireInfo(palisade2, 5, 5);
        Blocks.fire.setFireInfo(palisade3, 5, 5);

        Blocks.fire.setFireInfo(wattleGate, 5, 5);

        Blocks.fire.setFireInfo(strawNest, 5, 5);
    }

    private static void registerOre() {
        Bids.LOG.info("Register block ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        OreDictionary.registerOre("stoneRough", new ItemStack(roughStoneSed, 1, WILD));
        OreDictionary.registerOre("stoneRoughBricks", new ItemStack(roughStoneBrickSed, 1, WILD));
    }

    private static void registerCarvings() {
        Bids.LOG.info("Register block carvings");

        CarvingRegistry.registerCarving(new CarvingRoughStone());
        CarvingRegistry.registerCarving(new CarvingRoughStoneBrick());
        CarvingRegistry.registerCarving(new CarvingRoughStoneTile());
        CarvingRegistry.registerCarving(new CarvingRawStone());
        CarvingRegistry.registerCarving(new CarvingLogWall());
        CarvingRegistry.registerCarving(new CarvingLogWallVert());
        CarvingRegistry.registerCarving(new CarvingStackedLogs());
        CarvingRegistry.registerCarving(new CarvingWoodVert());
        CarvingRegistry.registerCarving(new CarvingMudBrick());
        CarvingRegistry.registerCarving(new CarvingPlanks());
        CarvingRegistry.registerCarving(new CarvingStoneBrick());
        CarvingRegistry.registerCarving(new CarvingStoneLargeBrick());
        CarvingRegistry.registerCarving(new CarvingSmoothStone());
    }

    private static void registerQuarryBlocks() {
        Bids.LOG.info("Register quarriable blocks");

        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneSed, roughStoneSed, 1, 1));
        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneMM, roughStoneMM, 2, 1.5f));
        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneIgIn, roughStoneIgIn, 4, 3f));
        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneIgEx, roughStoneIgEx, 4, 3f));
    }

    private static void registerCrackableBlocks() {
        Bids.LOG.info("Register crackable blocks");

        WoodPileRegistry.registerCrackableBlock(new CrackableBlockStone(TFCBlocks.stoneSed, BidsBlocks.crackedStoneSed, 1f));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockStone(TFCBlocks.stoneMM, BidsBlocks.crackedStoneMM, 1.2f));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockStone(TFCBlocks.stoneIgIn, BidsBlocks.crackedStoneIgIn, 1.5f));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockStone(TFCBlocks.stoneIgEx, BidsBlocks.crackedStoneIgEx, 2f));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockOre(TFCBlocks.ore, BidsBlocks.crackedOre));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockOre(TFCBlocks.ore1b, BidsBlocks.crackedOre1b));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockOre(TFCBlocks.ore2, BidsBlocks.crackedOre2));
        WoodPileRegistry.registerCrackableBlock(new CrackableBlockOre(TFCBlocks.ore3, BidsBlocks.crackedOre3));
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockRenderers() {
        Bids.LOG.info("Register block renderers");

        clayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(clayCrucibleRenderId, new RenderClayCrucible());

        fireClayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(fireClayCrucibleRenderId, new RenderFireClayCrucible());

        quarryRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(quarryRenderId, new RenderQuarry());

        carvingRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(carvingRenderId, new RenderCarving());

        woodPileRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(woodPileRenderId, new RenderWoodPile());

        dryingRackRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(dryingRackRenderId, new RenderDryingRack());

        choppingBlockRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(choppingBlockRenderId, new RenderChoppingBlock());

        saddleQuernRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(saddleQuernRenderId, new RenderSaddleQuern());

        workStoneRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(workStoneRenderId, new RenderWorkStone());

        stonePressLeverRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(stonePressLeverRenderId, new RenderStonePressLever());

        stonePressWeightRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(stonePressWeightRenderId, new RenderStonePressWeight());

        clayLampRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(clayLampRenderId, new RenderClayLamp());

        wallHookRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(wallHookRenderId, new RenderWallHook());

        unfinishedAnvilStage1RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage1RenderId, new RenderUnfinishedAnvil(0));

        unfinishedAnvilStage2RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage2RenderId, new RenderUnfinishedAnvil(1));

        unfinishedAnvilStage3RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage3RenderId, new RenderUnfinishedAnvil(2));

        unfinishedAnvilStage4RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage4RenderId, new RenderUnfinishedAnvil(3));

        unfinishedAnvilStage5RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage5RenderId, new RenderUnfinishedAnvil(4));

        unfinishedAnvilStage6RenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage6RenderId, new RenderUnfinishedAnvil(5));

        cookingPotRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(cookingPotRenderId, new RenderCookingPot());

        cookingPotLidRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(cookingPotLidRenderId, new RenderCookingPotLid());

        newCropsRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(newCropsRenderId, new RenderNewCrop());

        axleWallBearingRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(axleWallBearingRenderId, new RenderAxleWallBearing());

        screwRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(screwRenderId, new RenderScrew());

        screwPressRackRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(screwPressRackRenderId, new RenderScrewPressRack());

        screwPressBarrelRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(screwPressBarrelRenderId, new RenderScrewPressBarrel());

        screwPressDiscRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(screwPressDiscRenderId, new RenderScrewPressDisc());

        screwPressLeverRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(screwPressLeverRenderId, new RenderScrewPressLever());

        palisadeRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(palisadeRenderId, new RenderPalisade());

        wattleGateRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(wattleGateRenderId, new RenderWattleGate());

        strawNestRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(strawNestRenderId, new RenderStrawNest());

        crackedStoneRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(crackedStoneRenderId, new RenderCrackedStone());

        crackedOreRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(crackedOreRenderId, new RenderCrackedOre());
    }

    private static void registerTileEntities() {
        Bids.LOG.info("Register tile entities");

        GameRegistry.registerTileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        GameRegistry.registerTileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");

        GameRegistry.registerTileEntity(TileEntityChimney.class, "BidsChimney");

        GameRegistry.registerTileEntity(TileEntityQuarry.class, "BidsQuarry");

        GameRegistry.registerTileEntity(TileEntityCarving.class, "BidsCarving");

        GameRegistry.registerTileEntity(TileEntityWoodPile.class, "BidsWoodPile");

        GameRegistry.registerTileEntity(TileEntityNewFirepit.class, "BidsNewFirepit");

        GameRegistry.registerTileEntity(TileEntityDryingRack.class, "BidsDryingRack");

        GameRegistry.registerTileEntity(TileEntityChoppingBlock.class, "BidsChoppingBlock");

        GameRegistry.registerTileEntity(TileEntitySaddleQuern.class, "BidsDrainingStone");

        GameRegistry.registerTileEntity(TileEntityStonePressLever.class, "BidsStonePressLever");

        GameRegistry.registerTileEntity(TileEntityStonePressWeight.class, "BidsStonePressWeight");

        GameRegistry.registerTileEntity(TileEntityClayLamp.class, "BidsClayLamp");

        GameRegistry.registerTileEntity(TileEntityWallHook.class, "BidsWallHook");

        GameRegistry.registerTileEntity(TileEntityAquifer.class, "BidsAquifer");

        GameRegistry.registerTileEntity(TileEntityCookingPot.class, "BidsCookingPot");

        GameRegistry.registerTileEntity(TileEntityCookingPrep.class, "BidsCookingPrep");

        GameRegistry.registerTileEntity(TileEntityNewCrop.class, "BidsNewCrop");
        GameRegistry.registerTileEntity(TileEntityNewFarmland.class, "BidsNewFarmland");

        GameRegistry.registerTileEntity(TileEntityAxleWallBearing.class, "BidsAxleWallBearing");

        GameRegistry.registerTileEntity(TileEntityScrew.class, "BidsScrew");

        GameRegistry.registerTileEntity(TileEntityScrewPressBarrel.class, "BidsScrewPressBarrel");
        GameRegistry.registerTileEntity(TileEntityScrewPressDisc.class, "BidsScrewPressDisc");
        GameRegistry.registerTileEntity(TileEntityScrewPressLever.class, "BidsScrewPressLever");

        GameRegistry.registerTileEntity(TileEntityStrawNest.class, "BidsStrawNest");
    }

    @SideOnly(Side.CLIENT)
    private static void registerTileEntitiesClientOnly() {
        Bids.LOG.info("Bind tile entity special renderers");

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingRack.class, new TileRenderDryingRack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChoppingBlock.class, new TileRenderChoppingBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaddleQuern.class, new RenderTileSaddleQuern());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallHook.class, new RenderTileWallHook());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingPot.class, new RenderTileCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingPrep.class, new RenderTileCookingPrep());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrew.class, new RenderTileScrew());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrewPressDisc.class, new RenderTileScrewPressDisc());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrewPressLever.class, new RenderTileScrewPressLever());
    }

    private static void registerMessages() {
        Bids.LOG.info("Register tile entity messages");

        Bids.network.registerMessage(CarvingMessage.ServerHandler.class, CarvingMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(CarvingMessage.ClientHandler.class, CarvingMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(WoodPileMessage.ServerHandler.class, WoodPileMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(WoodPileMessage.ClientHandler.class, WoodPileMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(DryingRackMessage.ServerHandler.class, DryingRackMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(DryingRackMessage.ClientHandler.class, DryingRackMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(TileEntityUpdateMessage.ServerHandler.class, TileEntityUpdateMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(TileEntityUpdateMessage.ClientHandler.class, TileEntityUpdateMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

    private static void registerBlocks() {
        Bids.LOG.info("Register blocks");

        GameRegistry.registerBlock(clayCrucible, ItemClayCrucible.class, "ClayCrucible");
        GameRegistry.registerBlock(fireClayCrucible, ItemFireClayCrucible.class, "FireClayCrucible");

        GameRegistry.registerBlock(mudBrickChimney, ItemMudChimney.class, "MudBrickChimney");
        GameRegistry.registerBlock(mudBrickChimney2, ItemMudChimney.class, "MudBrickChimney2");

        GameRegistry.registerBlock(quarry, ItemQuarry.class, "Quary");

        GameRegistry.registerBlock(carvingRock, "CarvingRock");
        GameRegistry.registerBlock(carvingWood, "CarvingWood");

        GameRegistry.registerBlock(woodPile, "WoodPile");

        GameRegistry.registerBlock(newFirepit, "NewFirepit");

        GameRegistry.registerBlock(roughStoneSed, ItemRoughStone.class, "RoughStoneSed");
        GameRegistry.registerBlock(roughStoneBrickSed, ItemRoughStoneBrick.class, "RoughStoneBrickSed");

        GameRegistry.registerBlock(logWallEast, ItemLogWall.class, "LogWallEast");
        GameRegistry.registerBlock(logWallNorth, ItemLogWall.class, "LogWallNorth");
        GameRegistry.registerBlock(logWallCorner, ItemLogWall.class, "LogWallCorner");
        GameRegistry.registerBlock(logWallEastAlt, ItemLogWall.class, "LogWallEastAlt");
        GameRegistry.registerBlock(logWallNorthAlt, ItemLogWall.class, "LogWallNorthAlt");
        GameRegistry.registerBlock(logWallCornerAlt, ItemLogWall.class, "LogWallCornerAlt");

        GameRegistry.registerBlock(logWallEast2, ItemLogWall16.class, "LogWallEast2");
        GameRegistry.registerBlock(logWallNorth2, ItemLogWall16.class, "LogWallNorth2");
        GameRegistry.registerBlock(logWallCorner2, ItemLogWall16.class, "LogWallCorner2");
        GameRegistry.registerBlock(logWallEastAlt2, ItemLogWall16.class, "LogWallEastAlt2");
        GameRegistry.registerBlock(logWallNorthAlt2, ItemLogWall16.class, "LogWallNorthAlt2");
        GameRegistry.registerBlock(logWallCornerAlt2, ItemLogWall16.class, "LogWallCornerAlt2");

        GameRegistry.registerBlock(logWallEast3, ItemLogWall32.class, "LogWallEast3");
        GameRegistry.registerBlock(logWallNorth3, ItemLogWall32.class, "LogWallNorth3");
        GameRegistry.registerBlock(logWallCorner3, ItemLogWall32.class, "LogWallCorner3");
        GameRegistry.registerBlock(logWallEastAlt3, ItemLogWall32.class, "LogWallEastAlt3");
        GameRegistry.registerBlock(logWallNorthAlt3, ItemLogWall32.class, "LogWallNorthAlt3");
        GameRegistry.registerBlock(logWallCornerAlt3, ItemLogWall32.class, "LogWallCornerAlt3");

        GameRegistry.registerBlock(tiedStickBundle, "TiedStickBundle");

        GameRegistry.registerBlock(dryingRack, ItemDryingRack.class, "DryingRack");

        GameRegistry.registerBlock(stackedFirewood, "StackedFirewood");
        GameRegistry.registerBlock(stackedFirewood2, "StackedFirewood2");
        GameRegistry.registerBlock(stackedFirewood3, "StackedFirewood3");

        GameRegistry.registerBlock(choppingBlock, ItemChoppingBlock.class, "ChoppingBlock");
        GameRegistry.registerBlock(choppingBlock2, ItemChoppingBlock.class, "ChoppingBlock2");
        GameRegistry.registerBlock(choppingBlock3, ItemChoppingBlock.class, "ChoppingBlock3");

        GameRegistry.registerBlock(wattleTrapdoor, ItemWattleTrapDoor.class, "WattleTrapDoor");
        GameRegistry.registerBlock(wattleTrapdoorCover, "WattleTrapDoorCover");

        GameRegistry.registerBlock(saddleQuernBaseSed, ItemSaddleQuern.class, "SaddleQuernBaseSed");
        GameRegistry.registerBlock(saddleQuernHandstoneSed, ItemWorkStone.class, "SaddleQuernHandstoneSed");

        GameRegistry.registerBlock(logWallVert, ItemLogWallVert.class, "LogWallVert");
        GameRegistry.registerBlock(logWallVertAlt, ItemLogWallVert.class, "LogWallVertAlt");
        GameRegistry.registerBlock(logWallVert2, ItemLogWallVert16.class, "LogWallVert2");
        GameRegistry.registerBlock(logWallVertAlt2, ItemLogWallVert16.class, "LogWallVertAlt2");
        GameRegistry.registerBlock(logWallVert3, ItemLogWallVert32.class, "LogWallVert3");
        GameRegistry.registerBlock(logWallVertAlt3, ItemLogWallVert32.class, "LogWallVertAlt3");

        GameRegistry.registerBlock(saddleQuernPressingStoneSed, ItemWorkStone.class, "SaddleQuernPressingStoneSed");
        GameRegistry.registerBlock(stonePressLever, "StonePressLever");
        GameRegistry.registerBlock(stonePressWeightSed, ItemStonePressWeight.class, "StonePressWeightSed");

        GameRegistry.registerBlock(clayLamp, ItemClayLamp.class, "ClayLamp");

        GameRegistry.registerBlock(wallHook, ItemWallHook.class, "WallHook");

        GameRegistry.registerBlock(aquifer, ItemGenericSoil.class, "Aquifer");
        GameRegistry.registerBlock(aquifer2, ItemGenericSoil.class, "Aquifer2");

        GameRegistry.registerBlock(roughStoneTileSed, ItemRoughStoneBrick.class, "RoughStoneTileSed");

        GameRegistry.registerBlock(roughStoneMM, ItemRoughStone.class, "RoughStoneMM");
        GameRegistry.registerBlock(roughStoneBrickMM, ItemRoughStoneBrick.class, "RoughStoneBrickMM");
        GameRegistry.registerBlock(roughStoneTileMM, ItemRoughStoneBrick.class, "RoughStoneTileMM");

        GameRegistry.registerBlock(unfinishedAnvilStage1, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage1");
        GameRegistry.registerBlock(unfinishedAnvilStage2, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage2");
        GameRegistry.registerBlock(unfinishedAnvilStage3, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage3");
        GameRegistry.registerBlock(unfinishedAnvilStage4, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage4");
        GameRegistry.registerBlock(unfinishedAnvilStage5, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage5");

        GameRegistry.registerBlock(roughStoneIgIn, ItemRoughStone.class, "RoughStoneIgIn");
        GameRegistry.registerBlock(roughStoneBrickIgIn, ItemRoughStone.class, "RoughStoneBrickIgIn");
        GameRegistry.registerBlock(roughStoneTileIgIn, ItemRoughStone.class, "RoughStoneTileIgIn");

        GameRegistry.registerBlock(roughStoneIgEx, ItemRoughStone.class, "RoughStoneIgEx");
        GameRegistry.registerBlock(roughStoneBrickIgEx, ItemRoughStone.class, "RoughStoneBrickIgEx");
        GameRegistry.registerBlock(roughStoneTileIgEx, ItemRoughStone.class, "RoughStoneTileIgEx");

        GameRegistry.registerBlock(cookingPot, ItemCookingPot.class, "CookingPot");
        GameRegistry.registerBlock(cookingPotLid, ItemCookingPotLid.class, "CookingPotLid");

        GameRegistry.registerBlock(steamingMesh, "SteamingMesh");

        GameRegistry.registerBlock(cookingPrep, "CookingPrep");

        GameRegistry.registerBlock(newCrops, ItemSoil.class, "NewCrop");
        GameRegistry.registerBlock(newTilledSoil, ItemSoil.class, "NewTilledSoil");
        GameRegistry.registerBlock(newTilledSoil2, ItemSoil.class, "NewTilledSoil2");

        GameRegistry.registerBlock(woodAxleWallBearing, "WoodAxleWallBearing");

        GameRegistry.registerBlock(woodScrew, "WoodScrew");

        GameRegistry.registerBlock(screwPressRackBottom, ItemScrewPress.class, "ScrewPressRackBottom");
        GameRegistry.registerBlock(screwPressRackMiddle, ItemScrewPress.class, "ScrewPressRackMiddle");
        GameRegistry.registerBlock(screwPressRackTop, ItemScrewPress.class, "ScrewPressRackTop");
        GameRegistry.registerBlock(screwPressRackBridge, ItemScrewPress.class, "ScrewPressRackBridge");
        GameRegistry.registerBlock(screwPressBarrel, ItemScrewPress.class, "ScrewPressBarrel");
        GameRegistry.registerBlock(screwPressDisc, ItemScrewPress.class, "ScrewPressDisc");
        GameRegistry.registerBlock(screwPressLever, ItemScrewPress.class, "ScrewPressLever");
        GameRegistry.registerBlock(screwPressLeverTop, ItemScrewPress.class, "ScrewPressLeverTop");

        GameRegistry.registerBlock(palisade, ItemPalisade.class, "Palisade");
        GameRegistry.registerBlock(palisade2, ItemPalisade16.class, "Palisade2");
        GameRegistry.registerBlock(palisade3, ItemPalisade32.class, "Palisade3");

        GameRegistry.registerBlock(wattleGate, ItemWattleGate.class, "WattleGate");

        GameRegistry.registerBlock(strawNest, "StrawNest");

        GameRegistry.registerBlock(unfinishedAnvilStage6, ItemUnfinishedAnvil.class, "UnfinishedAnvilStage6");

        GameRegistry.registerBlock(crackedStoneSed, ItemCrackedStone.class, "CrackedStoneSed");
        GameRegistry.registerBlock(crackedStoneMM, ItemCrackedStone.class, "CrackedStoneMM");
        GameRegistry.registerBlock(crackedStoneIgIn, ItemCrackedStone.class, "CrackedStoneIgIn");
        GameRegistry.registerBlock(crackedStoneIgEx, ItemCrackedStone.class, "CrackedStoneIgEx");

        GameRegistry.registerBlock(crackedOre, "CrackedOre");
        GameRegistry.registerBlock(crackedOre1b, "CrackedOre1b");
        GameRegistry.registerBlock(crackedOre2, "CrackedOre2");
        GameRegistry.registerBlock(crackedOre3, "CrackedOre3");
    }

}
