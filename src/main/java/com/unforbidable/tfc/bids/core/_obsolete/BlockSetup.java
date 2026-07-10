package com.unforbidable.tfc.bids.core._obsolete;

import com.dunk.tfc.Items.ItemBlocks.ItemSoil;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.common.block.itemblock.ItemGenericSoil;
import com.unforbidable.tfc.bids.common.tileentity.TileEntityChimney;
import com.unforbidable.tfc.bids.core.network._obsolete.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.core.network._obsolete.NetworkHelper;
import com.unforbidable.tfc.bids.features.device.wallbearing.render.RenderAxleWallBearing;
import com.unforbidable.tfc.bids.features.device.wallbearing.tileentity.TileEntityAxleWallBearing;
import com.unforbidable.tfc.bids.features.building.carving.tileentity.TileEntityCarving;
import com.unforbidable.tfc.bids.features.building.decorativesurface.block.BlockDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.main.DecorativeSurfacePlacer;
import com.unforbidable.tfc.bids.features.building.decorativesurface.render.RenderDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.render.RenderTileDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.tileentity.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.firebrick.block.BlockFirebrickChimney;
import com.unforbidable.tfc.bids.features.building.firebrick.block.blockitem.ItemFireBrickChimney;
import com.unforbidable.tfc.bids.features.building.firebrick.tileentity.TileEntityFireBrickChimney;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall16;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall32;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert16;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert32;
import com.unforbidable.tfc.bids.features.building.mudbrick.block.itemblock.ItemMudbrickChimney;
import com.unforbidable.tfc.bids.features.building.palisade.block.BlockPalisade;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade16;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade32;
import com.unforbidable.tfc.bids.features.building.palisade.render.RenderPalisade;
import com.unforbidable.tfc.bids.features.building.roughstone.block.blockitem.ItemRoughStone;
import com.unforbidable.tfc.bids.features.building.roughstone.block.blockitem.ItemRoughStoneFence;
import com.unforbidable.tfc.bids.features.building.wattle.block.blockitem.ItemWattleGate;
import com.unforbidable.tfc.bids.features.building.wattle.block.blockitem.ItemWattleTrapDoor;
import com.unforbidable.tfc.bids.features.building.wattle.render.RenderWattleGate;
import com.unforbidable.tfc.bids.features.device.choppingblock.block.itemblock.ItemChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.render.RenderChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.render.RenderTileChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.tileentity.TileEntityChoppingBlock;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.blockitem.ItemCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.blockitem.ItemCookingPotLid;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderCookingPotLid;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderTileCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.tileentity.TileEntityCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingprep.render.RenderTileCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.tileentity.TileEntityCookingPrep;
import com.unforbidable.tfc.bids.features.device.crucible.block.itemblock.ItemClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.block.itemblock.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderTileDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.tileentity.TileEntityDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingsurface.block.BlockDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.main.DryingSurfacePlacer;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderTileDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.tileentity.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.features.device.firepit.render.RenderNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.beehive.BeehiveKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.climbing.ClimbingKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.square.SquareKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.tunnel.TunnelKilnChamber;
import com.unforbidable.tfc.bids.features.device.lamp.block.itemblock.ItemClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.render.RenderClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.tileentity.TileEntityClayLamp;
import com.unforbidable.tfc.bids.features.device.processingsurface.block.BlockProcessingSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.main.ProcessingSurfacePlacer;
import com.unforbidable.tfc.bids.features.device.processingsurface.render.RenderProcessingSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.features.device.screw.block.BlockScrew;
import com.unforbidable.tfc.bids.features.device.screw.render.RenderScrew;
import com.unforbidable.tfc.bids.features.device.screw.render.RenderTileScrew;
import com.unforbidable.tfc.bids.features.device.screw.tileentity.TileEntityScrew;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressLeverTop;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackBottom;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackBridge;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackMiddle;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackTop;
import com.unforbidable.tfc.bids.features.device.screwpress.block.blockitem.ItemScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressRack;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderTileScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderTileScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressLever;
import com.unforbidable.tfc.bids.features.device.soakingsurface.block.BlockSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfacePlacer;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderTileSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.tileentity.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderTileSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntityStonePressWeight;
import com.unforbidable.tfc.bids.features.device.strawnest.block.BlockStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.render.RenderStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.tileentity.TileEntityStrawNest;
import com.unforbidable.tfc.bids.features.device.wallhook.block.blockitem.ItemWallHook;
import com.unforbidable.tfc.bids.features.device.wallhook.render.RenderTileWallHook;
import com.unforbidable.tfc.bids.features.device.wallhook.render.RenderWallHook;
import com.unforbidable.tfc.bids.features.device.wallhook.tileentity.TileEntityWallHook;
import com.unforbidable.tfc.bids.features.device.woodpile.block.blockitem.ItemCrackedStone;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.features.utility.unfinishedanvil.block.blockitem.ItemUnfinishedAnvil;
import com.unforbidable.tfc.bids.features.utility.unfinishedanvil.render.RenderUnfinishedAnvil;
import com.unforbidable.tfc.bids.features.resource.crop.render.RenderNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewFarmland;
import com.unforbidable.tfc.bids.features.resource.quarry.block.itemblock.ItemQuarry;
import com.unforbidable.tfc.bids.features.resource.quarry.tileentity.TileEntityQuarry;
import com.unforbidable.tfc.bids.features.resource.well.tileentity.TileEntityAquifer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockSetup extends BidsBlocks {

    public static void preInit() {
        initBlocks();
        setupHarvest();
        registerBlocks();
        registerCarvings();
        registerQuarryBlocks();
        registerCrackableBlocks();
        registerKilnChambers();
        registerSurfacePlacers();
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

//        final float clayCrucibleHardness = CrucibleConfig.enableClayHandBreakable ? 0.5f : 4.0f;
//        final float fireClayCrucibleHardness = CrucibleConfig.enableFireClayHandBreakable ? 0.5f : 4.0f;
//        clayCrucible = new BlockClayCrucible().setBlockName("ClayCrucible")
//                .setBlockTextureName("Pottery Crucible")
//                .setHardness(clayCrucibleHardness);
//        fireClayCrucible = new BlockFireClayCrucible().setBlockName("FireClayCrucible")
//                .setBlockTextureName("Fire Clay Crucible")
//                .setHardness(fireClayCrucibleHardness);

//        mudBrickChimney = new BlockMudbrickChimney(0).setDirt(TFCBlocks.dirt)
//                .setBlockName("MudBrickChimney");
//        mudBrickChimney2 = new BlockMudbrickChimney(16).setDirt(TFCBlocks.dirt2)
//                .setBlockName("MudBrickChimney2");

//        quarry = new BlockQuarry().setBlockName("Quarry");

//        carvingRock = new BlockCarving(Material.rock).setBlockName("CarvingRock");
//        carvingWood = new BlockCarving(Material.wood).setBlockName("CarvingWood");

//        woodPile = new BlockWoodpile().setBlockName("WoodPile")
//                .setBlockTextureName("Wood Pile");
//
//        newFirepit = new BlockNewFirepit().setBlockName("NewFirepit");
//
//        roughStoneSed = new BlockRoughStone()
//                .setMetaHavingTopTexture(0, 4) // Shale and Sandstone
//                .setNames(Global.STONE_SED).setBlockName("RoughStoneSed")
//                .setBlockTextureName("Rough");
//        roughStoneMM = new BlockRoughStone()
//                .setMetaHavingTopTexture(1, 2, 3) // Slate, Shist and Phyllite
//                .setNames(Global.STONE_MM).setBlockName("RoughStoneMM")
//                .setBlockTextureName("Rough");
//        roughStoneIgIn = new BlockRoughStone()
//                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneIgIn")
//                .setBlockTextureName("Rough");
//        roughStoneIgEx = new BlockRoughStone()
//                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneIgEx")
//                .setBlockTextureName("Rough");
//
//        roughStoneBrickSed = new BlockRoughStoneBrick()
//                .setMetaHavingTopTexture(0, 4) // Shale and Sandstone
//                .setNames(Global.STONE_SED).setBlockName("RoughStoneBrickSed")
//                .setBlockTextureName("Rough Brick");
//        roughStoneBrickMM = new BlockRoughStoneBrick()
//                .setMetaHavingTopTexture(1, 2, 3) // Slate, Shist and Phyllite
//                .setNames(Global.STONE_MM).setBlockName("RoughStoneBrickMM")
//                .setBlockTextureName("Rough Brick");
//        roughStoneBrickIgIn = new BlockRoughStoneBrick()
//                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneBrickIgIn")
//                .setBlockTextureName("Rough Brick");
//        roughStoneBrickIgEx = new BlockRoughStoneBrick()
//                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneBrickIgEx")
//                .setBlockTextureName("Rough Brick");
//
//        roughStoneTileSed = new BlockRoughStoneBrick()
//                .setAllHaveTopTexture(true)
//                .setNames(Global.STONE_SED).setBlockName("RoughStoneTileSed")
//                .setBlockTextureName("Rough Tile");
//        roughStoneTileMM = new BlockRoughStoneBrick()
//                .setAllHaveTopTexture(true)
//                .setNames(Global.STONE_MM).setBlockName("RoughStoneTileMM")
//                .setBlockTextureName("Rough Tile");
//        roughStoneTileIgIn = new BlockRoughStoneBrick()
//                .setAllHaveTopTexture(true)
//                .setNames(Global.STONE_IGIN).setBlockName("RoughStoneTileIgIn")
//                .setBlockTextureName("Rough Tile");
//        roughStoneTileIgEx = new BlockRoughStoneBrick()
//                .setAllHaveTopTexture(true)
//                .setNames(Global.STONE_IGEX).setBlockName("RoughStoneTileIgEx")
//                .setBlockTextureName("Rough Tile");
//
//        roughStoneBrickFenceSed = new BlockRoughStoneFence((BlockRoughStone) roughStoneBrickSed)
//            .setMaterialBlockTopBottom(roughStoneTileSed)
//            .setBlockName("RoughStoneBrickFenceSed");
//        roughStoneBrickFenceMM = new BlockRoughStoneFence((BlockRoughStone) roughStoneBrickMM)
//            .setMaterialBlockTopBottom(roughStoneTileMM)
//            .setBlockName("RoughStoneBrickFenceMM");
//        roughStoneBrickFenceIgIn = new BlockRoughStoneFence((BlockRoughStone) roughStoneBrickIgIn)
//            .setMaterialBlockTopBottom(roughStoneTileIgIn)
//            .setBlockName("RoughStoneBrickFenceIgIn");
//        roughStoneBrickFenceIgEx = new BlockRoughStoneFence((BlockRoughStone) roughStoneBrickIgEx)
//            .setMaterialBlockTopBottom(roughStoneTileIgEx)
//            .setBlockName("RoughStoneBrickFenceIgEx");
//
//        roughStoneTileFenceSed = new BlockRoughStoneFence((BlockRoughStone) roughStoneTileSed)
//            .setBlockName("RoughStoneTileFenceSed");
//        roughStoneTileFenceMM = new BlockRoughStoneFence((BlockRoughStone) roughStoneTileMM)
//            .setBlockName("RoughStoneTileFenceMM");
//        roughStoneTileFenceIgIn = new BlockRoughStoneFence((BlockRoughStone) roughStoneTileIgIn)
//            .setBlockName("RoughStoneTileFenceIgIn");
//        roughStoneTileFenceIgEx = new BlockRoughStoneFence((BlockRoughStone) roughStoneTileIgEx)
//            .setBlockName("RoughStoneTileFenceIgEx");

//        logWallEast = new BlockLogWall(LogWallType.EAST, 0).setBlockName("LogWallEast");
//        logWallNorth = new BlockLogWall(LogWallType.NORTH, 0).setBlockName("LogWallNorth");
//        logWallCorner = new BlockLogWall(LogWallType.CORNER, 0).setBlockName("LogWallCorner");
//        logWallEastAlt = new BlockLogWall(LogWallType.EAST_ALT, 0).setBlockName("LogWallEastAlt");
//        logWallNorthAlt = new BlockLogWall(LogWallType.NORTH_ALT, 0).setBlockName("LogWallNorthAlt");
//        logWallCornerAlt = new BlockLogWall(LogWallType.CORNER_ALT, 0).setBlockName("LogWallCornerAlt");
//
//        logWallEast2 = new BlockLogWall(LogWallType.EAST, 16).setBlockName("LogWallEast2");
//        logWallNorth2 = new BlockLogWall(LogWallType.NORTH, 16).setBlockName("LogWallNorth2");
//        logWallCorner2 = new BlockLogWall(LogWallType.CORNER, 16).setBlockName("LogWallCorner2");
//        logWallEastAlt2 = new BlockLogWall(LogWallType.EAST_ALT, 16).setBlockName("LogWallEastAlt2");
//        logWallNorthAlt2 = new BlockLogWall(LogWallType.NORTH_ALT, 16).setBlockName("LogWallNorthAlt2");
//        logWallCornerAlt2 = new BlockLogWall(LogWallType.CORNER_ALT, 16).setBlockName("LogWallCornerAlt2");
//
//        logWallEast3 = new BlockLogWall(LogWallType.EAST, 32).setBlockName("LogWallEast3");
//        logWallNorth3 = new BlockLogWall(LogWallType.NORTH, 32).setBlockName("LogWallNorth3");
//        logWallCorner3 = new BlockLogWall(LogWallType.CORNER, 32).setBlockName("LogWallCorner3");
//        logWallEastAlt3 = new BlockLogWall(LogWallType.EAST_ALT, 32).setBlockName("LogWallEastAlt3");
//        logWallNorthAlt3 = new BlockLogWall(LogWallType.NORTH_ALT, 32).setBlockName("LogWallNorthAlt3");
//        logWallCornerAlt3 = new BlockLogWall(LogWallType.CORNER_ALT, 32).setBlockName("LogWallCornerAlt3");
//
//        logWallVert = new BlockLogWallVert(LogWallVertType.DEFAULT, 0).setBlockName("LogWallVert");
//        logWallVertAlt = new BlockLogWallVert(LogWallVertType.ALT, 0).setBlockName("LogWallVertAlt");
//
//        logWallVert2 = new BlockLogWallVert(LogWallVertType.DEFAULT, 16).setBlockName("LogWallVert2");
//        logWallVertAlt2 = new BlockLogWallVert(LogWallVertType.ALT, 16).setBlockName("LogWallVertAlt2");
//
//        logWallVert3 = new BlockLogWallVert(LogWallVertType.DEFAULT, 32).setBlockName("LogWallVert3");
//        logWallVertAlt3 = new BlockLogWallVert(LogWallVertType.ALT, 32).setBlockName("LogWallVertAlt3");
//
//        tiedStickBundle = new BlockTiedStickBundle().setBlockName("TiedStickBundle");
//
//        stackedFirewood = new BlockStackedFirewood(0).setBlockName("StackedFirewood");
//        stackedFirewood2 = new BlockStackedFirewood(16).setBlockName("StackedFirewood2");
//        stackedFirewood3 = new BlockStackedFirewood(32).setBlockName("StackedFirewood3");
//
//        dryingRack = new BlockDryingRack().setBlockName("DryingRack");

//        choppingBlock = new BlockChoppingBlock(TFCBlocks.woodVert)
//                .setBlockName("ChoppingBlock");
//        choppingBlock2 = new BlockChoppingBlock(TFCBlocks.woodVert2)
//                .setBlockName("ChoppingBlock2");
//        choppingBlock3 = new BlockChoppingBlock(TFCBlocks.woodVert3)
//                .setBlockName("ChoppingBlock3");

//        wattleTrapdoor = new BlockWattleTrapDoor().setIgnoreRedstone(true)
//                .setBlockName("WattleTrapDoor")
//                .setBlockTextureName("Wattle Trap Door");
//        wattleTrapdoorCover = new BlockWattleTrapDoorCover()
//                .setBlockName("WattleTrapDoorCover");

//        saddleQuernBaseSed = new BlockSaddleQuern(roughStoneSed)
//                .setBlockName("SaddleQuernSed");
//        saddleQuernHandstoneSed = new BlockWorkStone(roughStoneSed)
//                .setWorkStoneType(WorkStoneType.SADDLE_QUERN_CRUSHING)
//                .setBlockName("SaddleQuernHandstoneSed");
//        saddleQuernPressingStoneSed = new BlockWorkStone(roughStoneSed)
//                .setWorkStoneType(WorkStoneType.SADDLE_QUERN_PRESSING)
//                .setBlockName("SaddleQuernPressingStoneSed");
//        stonePressLever = new BlockStonePressLever()
//                .setBlockName("StonePressLever");
//        stonePressWeightSed = new BlockStonePressWeight(roughStoneSed)
//                .setBlockName("StonePressWeight");

//        clayLamp = new BlockClayLamp()
//                .setBlockName("ClayLamp");

//        wallHook = new BlockWallHook()
//                .setBlockName("WallHook");

//        aquifer = new BlockAquifer(0, TFCBlocks.gravel)
//                .setBlockName("Aquifer");
//        aquifer2 = new BlockAquifer(16, TFCBlocks.gravel2)
//                .setBlockName("Aquifer2");

//        unfinishedAnvilStage1 = new BlockUnfinishedAnvil(0)
//            .setBlockName("UnfinishedAnvilStage1");
//        unfinishedAnvilStage2 = new BlockUnfinishedAnvil(1)
//            .setBlockName("UnfinishedAnvilStage2");
//        unfinishedAnvilStage3 = new BlockUnfinishedAnvil(2)
//            .setBlockName("UnfinishedAnvilStage3");
//        unfinishedAnvilStage4 = new BlockUnfinishedAnvil(3)
//            .setBlockName("UnfinishedAnvilStage4");
//        unfinishedAnvilStage5 = new BlockUnfinishedAnvil(4)
//            .setBlockName("UnfinishedAnvilStage5");
//        unfinishedAnvilStage6 = new BlockUnfinishedAnvil(5)
//            .setBlockName("UnfinishedAnvilStage6");

//        cookingPot = new BlockCookingPot()
//            .setBlockTextureName("Cooking Pot")
//            .setBlockName("CookingPot");
//        cookingPotLid = new BlockCookingPotLid()
//            .setBlockTextureName("Cooking Pot Lid")
//            .setBlockName("CookingPotLid");
//
//        steamingMesh = new BlockSteamingMesh()
//            .setBlockTextureName("Steaming Mesh")
//            .setBlockName("SteamingMesh");

//        cookingPrep = new BlockCookingPrep()
//            .setBlockName("CookingPrep");

//        newCrops = new BlockNewCrop()
//            .setHardness(0.3F)
//            .setStepSound(Block.soundTypeGrass)
//            .setBlockName("NewCrop");
//        newTilledSoil = new BlockNewFarmland(TFCBlocks.dirt, 0)
//            .setHardness(2F)
//            .setStepSound(Block.soundTypeGravel)
//            .setBlockName("NewTilledSoil");
//        newTilledSoil2 = new BlockNewFarmland(TFCBlocks.dirt2, 16)
//            .setHardness(2F)
//            .setStepSound(Block.soundTypeGravel)
//            .setBlockName("NewTilledSoil");

        palisade = new BlockPalisade(logWallVert, 0)
            .setBlockName("Palisade");
        palisade2 = new BlockPalisade(logWallVert2, 16)
            .setBlockName("Palisade2");
        palisade3 = new BlockPalisade(logWallVert3, 32)
            .setBlockName("Palisade3");
//        woodAxleWallBearing = new BlockAxleWallBearing(Material.wood)
//            .setHardness(0.5F)
//            .setBlockName("WoodAxleWallBearing");

//        woodScrew = new BlockScrew(Material.wood)
//            .setHardness(0.5F)
//            .setBlockTextureName("Wood Screw")
//            .setBlockName("WoodScrew");

//        screwPressRackBottom = new BlockScrewPressRackBottom()
//            .setBlockName("ScrewPressRackBottom");
//        screwPressRackMiddle = new BlockScrewPressRackMiddle()
//            .setBlockName("ScrewPressRackMiddle");
//        screwPressRackTop = new BlockScrewPressRackTop()
//            .setBlockName("ScrewPressRackTop");
//        screwPressRackBridge = new BlockScrewPressRackBridge()
//            .setBlockName("ScrewPressRackBridge");
//        screwPressBarrel = new BlockScrewPressBarrel()
//            .setBlockName("ScrewPressBarrel");
//        screwPressDisc = new BlockScrewPressDisc()
//            .setBlockName("ScrewPressDisc");
//        screwPressLever = new BlockScrewPressLever()
//            .setBlockName("ScrewPressLever");
//        screwPressLeverTop = new BlockScrewPressLeverTop()
//            .setBlockName("ScrewPressLeverTop");


//        wattleGate = new BlockWattleGate()
//            .setBlockTextureName("Wattle Gate")
//            .setBlockName("WattleGate");
//
        strawNest = new BlockStrawNest()
            .setBlockName("StrawNest");

//        crackedStoneSed = new BlockCrackedSed(Material.rock)
//            .setHardness(3.5F)
//            .setBlockName("CrackedSedRock");
//        crackedStoneMM = new BlockCrackedMM(Material.rock)
//            .setHardness(4F)
//            .setBlockName("CrackedMMRock");
//        crackedStoneIgIn = new BlockCrackedIgIn(Material.rock)
//            .setHardness(4F)
//            .setBlockName("CrackedIgInRock");
//        crackedStoneIgEx = new BlockCrackedIgEx(Material.rock)
//            .setHardness(4F)
//            .setBlockName("CrackedIgExRock");
//
//        crackedOre = new BlockCrackedOre(Material.rock)
//            .setHardness(5F)
//            .setResistance(5F)
//            .setBlockName("Ore");
//        crackedOre1b = new BlockCrackedOre(Material.rock)
//            .setDamageOffset(16)
//            .setHardness(5F)
//            .setResistance(5F)
//            .setBlockName("Ore");
//        crackedOre2 = new BlockCrackedOre2(Material.rock)
//            .setHardness(5F)
//            .setResistance(5F)
//            .setBlockName("Ore");
//        crackedOre3 = new BlockCrackedOre3(Material.rock)
//            .setHardness(5F)
//            .setResistance(5F)
//            .setBlockName("Ore");

        fireBrickChimney = new BlockFirebrickChimney()
            .setBlockName("FireBrickChimney");

//        light = new BlockLight()
//            .setBlockName("Light");

        processingSurface = new BlockProcessingSurface()
            .setBlockName("ProcessingSurface");

        decorativeSurface = new BlockDecorativeSurface()
            .setBlockName("DecorativeSurface");

        soakingSurface = new BlockSoakingSurface()
            .setBlockName("SoakingSurface");

        dryingSurface = new BlockDryingSurface()
            .setBlockName("DryingSurface");
    }

    private static void updateBlocks() {
        Bids.LOG.info("Update blocks");

/*
        if (CrucibleConfig.enableClassicHandBreakable) {
            // Lower the hardness of the classic TFC crucible
            // The original value is 4.0f
            Bids.LOG.info("Classic TFC crucible hardness reduced");
            TFCBlocks.crucible.setHardness(0.5f);
        }

*/
//        if (FirepitConfig.replaceFirepitTFC) {
//            BidsBlocks.firepitTFC = TFCBlocks.firepit;
//            TFCBlocks.firepit = BidsBlocks.newFirepit;
//        }
    }

    private static void setupHarvest() {
        Bids.LOG.info("Set block harvestability");

        roughStoneSed.setHarvestLevel("shovel", 0);
        roughStoneBrickSed.setHarvestLevel("shovel", 0);
        roughStoneTileSed.setHarvestLevel("shovel", 0);
        roughStoneBrickFenceSed.setHarvestLevel("shovel", 0);
        roughStoneTileFenceSed.setHarvestLevel("shovel", 0);
        roughStoneMM.setHarvestLevel("shovel", 0);
        roughStoneBrickMM.setHarvestLevel("shovel", 0);
        roughStoneTileMM.setHarvestLevel("shovel", 0);
        roughStoneBrickFenceMM.setHarvestLevel("shovel", 0);
        roughStoneTileFenceMM.setHarvestLevel("shovel", 0);
        roughStoneIgIn.setHarvestLevel("shovel", 0);
        roughStoneBrickIgIn.setHarvestLevel("shovel", 0);
        roughStoneTileIgIn.setHarvestLevel("shovel", 0);
        roughStoneBrickFenceIgIn.setHarvestLevel("shovel", 0);
        roughStoneTileFenceIgIn.setHarvestLevel("shovel", 0);
        roughStoneIgEx.setHarvestLevel("shovel", 0);
        roughStoneBrickIgEx.setHarvestLevel("shovel", 0);
        roughStoneTileIgEx.setHarvestLevel("shovel", 0);
        roughStoneBrickFenceIgEx.setHarvestLevel("shovel", 0);
        roughStoneTileFenceIgEx.setHarvestLevel("shovel", 0);

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

    private static void registerCarvings() {
        Bids.LOG.info("Register block carvings");

//        BidsRegistry.CARVING_BLOCKS.register(new CarvableRoughStone());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableRoughStoneBrick());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableRoughStoneTile());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableRawStone());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableLogWall());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableLogWallVert());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableStackedLogs());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableWoodVert());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableMudBrick());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvablePlanks());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableStoneBrick());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableStoneLargeBrick());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableSmoothStone());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableBrick());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableFireBrick());
    }

    private static void registerQuarryBlocks() {
        Bids.LOG.info("Register quarriable blocks");

//        BidsRegistry.QUARRY_BLOCKS.register(TFCBlocks.stoneSed, new QuarriableStone(TFCBlocks.stoneSed, roughStoneSed, 1, 1));
//        BidsRegistry.QUARRY_BLOCKS.register(TFCBlocks.stoneMM, new QuarriableStone(TFCBlocks.stoneMM, roughStoneMM, 2, 1.5f));
//        BidsRegistry.QUARRY_BLOCKS.register(TFCBlocks.stoneIgIn, new QuarriableStone(TFCBlocks.stoneIgIn, roughStoneIgIn, 4, 3f));
//        BidsRegistry.QUARRY_BLOCKS.register(TFCBlocks.stoneIgEx, new QuarriableStone(TFCBlocks.stoneIgEx, roughStoneIgEx, 4, 3f));
    }

    private static void registerCrackableBlocks() {
        Bids.LOG.info("Register crackable blocks");

//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockStone(TFCBlocks.stoneSed, BidsBlocks.crackedStoneSed, 1f));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockStone(TFCBlocks.stoneMM, BidsBlocks.crackedStoneMM, 1.2f));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockStone(TFCBlocks.stoneIgIn, BidsBlocks.crackedStoneIgIn, 1.5f));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockStone(TFCBlocks.stoneIgEx, BidsBlocks.crackedStoneIgEx, 2f));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockOre(TFCBlocks.ore, BidsBlocks.crackedOre));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockOre(TFCBlocks.ore1b, BidsBlocks.crackedOre1b));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockOre(TFCBlocks.ore2, BidsBlocks.crackedOre2));
//        BidsRegistry.WOODPILE_CRACKABLE_BLOCKS.register(new CrackableBlockOre(TFCBlocks.ore3, BidsBlocks.crackedOre3));
    }

    private static void registerKilnChambers() {
        Bids.LOG.info("Register kiln chambers");

        if (BidsOptions.Kiln.enableTunnelKiln) {
            BidsRegistry.KILN_CHAMBERS.register(TunnelKilnChamber.class);
        }
        if (BidsOptions.Kiln.enableSquareKiln) {
            BidsRegistry.KILN_CHAMBERS.register(SquareKilnChamber.class);
        }
        if (BidsOptions.Kiln.enableBeehiveKiln) {
            BidsRegistry.KILN_CHAMBERS.register(BeehiveKilnChamber.class);
        }
        if (BidsOptions.Kiln.enableClimbingKiln) {
            BidsRegistry.KILN_CHAMBERS.register(ClimbingKilnChamber.class);
        }
    }

    private static void registerSurfacePlacers() {
        Bids.LOG.info("Register surface item placers");

        BidsRegistry.SURFACE_ITEM_PLACERS.register(new ProcessingSurfacePlacer());
        BidsRegistry.SURFACE_ITEM_PLACERS.register(new DecorativeSurfacePlacer());
        BidsRegistry.SURFACE_ITEM_PLACERS.register(new SoakingSurfacePlacer());
        BidsRegistry.SURFACE_ITEM_PLACERS.register(new DryingSurfacePlacer());
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockRenderers() {
        Bids.LOG.info("Register block renderers");

//        clayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(clayCrucibleRenderId, new RenderClayCrucible());
//
//        fireClayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(fireClayCrucibleRenderId, new RenderFireClayCrucible());

//        quarryRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(quarryRenderId, new RenderQuarry());

//        carvingRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(carvingRenderId, new RenderCarving());
//
//        woodPileRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(woodPileRenderId, new RenderWoodpile());

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
        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage1RenderId, new RenderUnfinishedAnvil());

//        unfinishedAnvilStage2RenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage2RenderId, new RenderUnfinishedAnvil(1));
//
//        unfinishedAnvilStage3RenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage3RenderId, new RenderUnfinishedAnvil(2));
//
//        unfinishedAnvilStage4RenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage4RenderId, new RenderUnfinishedAnvil(3));
//
//        unfinishedAnvilStage5RenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage5RenderId, new RenderUnfinishedAnvil(4));
//
//        unfinishedAnvilStage6RenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(unfinishedAnvilStage6RenderId, new RenderUnfinishedAnvil(5));

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

//        crackedStoneRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(crackedStoneRenderId, new RenderCrackedStone());
//
//        crackedOreRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(crackedOreRenderId, new RenderCrackedOre());

//        roughStoneFenceRenderId = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(roughStoneFenceRenderId, new RenderRoughStoneFence());

        processingSurfaceRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(processingSurfaceRenderId, new RenderProcessingSurface());

        decorativeSurfaceRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(decorativeSurfaceRenderId, new RenderDecorativeSurface());

        soakingSurfaceRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(soakingSurfaceRenderId, new RenderSoakingSurface());

        newFirepitRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(newFirepitRenderId, new RenderNewFirepit());

        dryingSurfaceRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(dryingSurfaceRenderId, new RenderDryingSurface());
    }

    private static void registerTileEntities() {
        Bids.LOG.info("Register tile entities");

        GameRegistry.registerTileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        GameRegistry.registerTileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");

        GameRegistry.registerTileEntity(TileEntityChimney.class, "BidsChimney");

        GameRegistry.registerTileEntity(TileEntityQuarry.class, "BidsQuarry");

        GameRegistry.registerTileEntity(TileEntityCarving.class, "BidsCarving");

        GameRegistry.registerTileEntity(TileEntityWoodpile.class, "BidsWoodPile");

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

        GameRegistry.registerTileEntity(TileEntityFireBrickChimney.class, "BidsFireBrickChimney");

        GameRegistry.registerTileEntity(TileEntityProcessingSurface.class, "BidsProcessingSurface");

        GameRegistry.registerTileEntity(TileEntityDecorativeSurface.class, "BidsDecorativeSurface");

        GameRegistry.registerTileEntity(TileEntitySoakingSurface.class, "BidsSoakingSurface");

        GameRegistry.registerTileEntity(TileEntityDryingSurface.class, "BidsDryingSurface");
    }

    @SideOnly(Side.CLIENT)
    private static void registerTileEntitiesClientOnly() {
        Bids.LOG.info("Bind tile entity special renderers");

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingRack.class, new RenderTileDryingRack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChoppingBlock.class, new RenderTileChoppingBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaddleQuern.class, new RenderTileSaddleQuern());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallHook.class, new RenderTileWallHook());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingPot.class, new RenderTileCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingPrep.class, new RenderTileCookingPrep());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrew.class, new RenderTileScrew());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrewPressDisc.class, new RenderTileScrewPressDisc());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScrewPressLever.class, new RenderTileScrewPressLever());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecorativeSurface.class, new RenderTileDecorativeSurface());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoakingSurface.class, new RenderTileSoakingSurface());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingSurface.class, new RenderTileDryingSurface());
    }

    private static void registerMessages() {
        Bids.LOG.info("Register tile entity messages");

//        Bids.network.registerMessage(CarvingPacket.ServerHandler.class, CarvingPacket.class,
//                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
//        Bids.network.registerMessage(CarvingPacket.ClientHandler.class, CarvingPacket.class,
//                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
//
//        Bids.network.registerMessage(WoodpilePacket.ServerHandler.class, WoodpilePacket.class,
//                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
//        Bids.network.registerMessage(WoodpilePacket.ClientHandler.class, WoodpilePacket.class,
//                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(TileEntityUpdateMessage.ServerHandler.class, TileEntityUpdateMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(TileEntityUpdateMessage.ClientHandler.class, TileEntityUpdateMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

    private static void registerBlocks() {
        Bids.LOG.info("Register blocks");

        GameRegistry.registerBlock(clayCrucible, ItemClayCrucible.class, "ClayCrucible");
        GameRegistry.registerBlock(fireClayCrucible, ItemFireClayCrucible.class, "FireClayCrucible");

        GameRegistry.registerBlock(mudBrickChimney, ItemMudbrickChimney.class, "MudBrickChimney");
        GameRegistry.registerBlock(mudBrickChimney2, ItemMudbrickChimney.class, "MudBrickChimney2");

        GameRegistry.registerBlock(quarry, ItemQuarry.class, "Quary");

        GameRegistry.registerBlock(carvingRock, "CarvingRock");
        GameRegistry.registerBlock(carvingWood, "CarvingWood");

        GameRegistry.registerBlock(woodPile, "WoodPile");

        GameRegistry.registerBlock(newFirepit, "NewFirepit");

        GameRegistry.registerBlock(roughStoneSed, ItemRoughStone.class, "RoughStoneSed");
        GameRegistry.registerBlock(roughStoneBrickSed, ItemRoughStone.class, "RoughStoneBrickSed");

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

        GameRegistry.registerBlock(dryingRack, "DryingRack");

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

        GameRegistry.registerBlock(roughStoneTileSed, ItemRoughStone.class, "RoughStoneTileSed");

        GameRegistry.registerBlock(roughStoneMM, ItemRoughStone.class, "RoughStoneMM");
        GameRegistry.registerBlock(roughStoneBrickMM, ItemRoughStone.class, "RoughStoneBrickMM");
        GameRegistry.registerBlock(roughStoneTileMM, ItemRoughStone.class, "RoughStoneTileMM");

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

        GameRegistry.registerBlock(fireBrickChimney, ItemFireBrickChimney.class, "FireBrickChimney");

        GameRegistry.registerBlock(light, "Light");

        GameRegistry.registerBlock(roughStoneBrickFenceSed, ItemRoughStoneFence.class, "RoughStoneBrickFenceSed");
        GameRegistry.registerBlock(roughStoneBrickFenceMM, ItemRoughStoneFence.class, "RoughStoneBrickFenceMM");
        GameRegistry.registerBlock(roughStoneBrickFenceIgIn, ItemRoughStoneFence.class, "RoughStoneBrickFenceIgIn");
        GameRegistry.registerBlock(roughStoneBrickFenceIgEx, ItemRoughStoneFence.class, "RoughStoneBrickFenceIgEx");

        GameRegistry.registerBlock(roughStoneTileFenceSed, ItemRoughStoneFence.class, "RoughStoneTileFenceSed");
        GameRegistry.registerBlock(roughStoneTileFenceMM, ItemRoughStoneFence.class, "RoughStoneTileFenceMM");
        GameRegistry.registerBlock(roughStoneTileFenceIgIn, ItemRoughStoneFence.class, "RoughStoneTileFenceIgIn");
        GameRegistry.registerBlock(roughStoneTileFenceIgEx, ItemRoughStoneFence.class, "RoughStoneTileFenceIgEx");

        GameRegistry.registerBlock(processingSurface, "ProcessingSurface");

        GameRegistry.registerBlock(decorativeSurface, "DecorativeSurface");

        GameRegistry.registerBlock(soakingSurface, "SoakingSurface");

        GameRegistry.registerBlock(dryingSurface, "DryingSurface");
    }

}
