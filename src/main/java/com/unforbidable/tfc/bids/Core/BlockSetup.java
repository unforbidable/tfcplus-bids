package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Blocks.*;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.*;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackMessage;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Network.NetworkHelper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Quarry.Quarriables.QuarriableStone;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileMessage;
import com.unforbidable.tfc.bids.Items.ItemBlocks.*;
import com.unforbidable.tfc.bids.Render.Blocks.*;
import com.unforbidable.tfc.bids.Render.Tiles.RenderTileWallHook;
import com.unforbidable.tfc.bids.Render.Tiles.TileRenderDryingRack;
import com.unforbidable.tfc.bids.Render.Tiles.TileRenderChoppingBlock;
import com.unforbidable.tfc.bids.Render.Tiles.RenderTileSaddleQuern;
import com.unforbidable.tfc.bids.TileEntities.*;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.Core.SaddleQuern.EnumWorkStoneType;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallVertType;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
                .setSandstoneHasTopTexture(true)
                .setNames(Global.STONE_SED).setBlockName("RoughStoneSed")
                .setBlockTextureName("Rough");
        roughStoneMM = new BlockRoughStone()
                .setMetaOnly(1) // Only slate
                .setNames(Global.STONE_MM).setBlockName("RoughStoneMM")
                .setBlockTextureName("Rough");

        roughStoneBrickSed = new BlockRoughStoneBrick()
                .setSandstoneHasTopTexture(true)
                .setNames(Global.STONE_SED).setBlockName("RoughStoneBrickSed")
                .setBlockTextureName("Rough Brick");
        roughStoneBrickMM = new BlockRoughStoneBrick()
                .setMetaOnly() // No blocks yet!
                .setNames(Global.STONE_MM).setBlockName("RoughStoneBrickMM")
                .setBlockTextureName("Rough Brick");

        roughStoneTileSed = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setMetaOnly(0, 4) // Only Shale and Sandstone
                .setNames(Global.STONE_SED).setBlockName("RoughStoneTileSed")
                .setBlockTextureName("Rough Tile");
        roughStoneTileMM = new BlockRoughStoneBrick()
                .setAllHaveTopTexture(true)
                .setMetaOnly(1) // Only Slate
                .setNames(Global.STONE_MM).setBlockName("RoughStoneTileMM")
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

        carvingRock.setHarvestLevel("shovel", 0);
        carvingWood.setHarvestLevel("axe", 0);

        saddleQuernBaseSed.setHarvestLevel("shovel", 0);
        stonePressWeightSed.setHarvestLevel("shovel", 0);
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

        Blocks.fire.setFireInfo(woodPile, 10, 10);

        Blocks.fire.setFireInfo(choppingBlock, 5, 5);

        Blocks.fire.setFireInfo(wattleTrapdoor, 10, 30);
        Blocks.fire.setFireInfo(wattleTrapdoorCover, 60, 20);

        Blocks.fire.setFireInfo(stonePressLever, 5, 5);

        Blocks.fire.setFireInfo(wallHook, 5, 5);
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
        CarvingRegistry.registerCarving(new CarvingRawStone());
        CarvingRegistry.registerCarving(new CarvingLogWall());
        CarvingRegistry.registerCarving(new CarvingLogWallVert());
        CarvingRegistry.registerCarving(new CarvingStackedLogs());
        CarvingRegistry.registerCarving(new CarvingWoodVert());
        CarvingRegistry.registerCarving(new CarvingMudBrick());
    }

    private static void registerQuarryBlocks() {
        Bids.LOG.info("Register quarriable blocks");

        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneSed, roughStoneSed, 1, 1));
        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneMM, roughStoneMM, 2, 1.5f));
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
    }

    @SideOnly(Side.CLIENT)
    private static void registerTileEntitiesClientOnly() {
        Bids.LOG.info("Bind tile entity special renderers");

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingRack.class, new TileRenderDryingRack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChoppingBlock.class, new TileRenderChoppingBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaddleQuern.class, new RenderTileSaddleQuern());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallHook.class, new RenderTileWallHook());
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
    }

}
