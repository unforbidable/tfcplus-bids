package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingRoughStoneBrick;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingStackedLogs;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackMessage;
import com.unforbidable.tfc.bids.Core.Network.NetworkHelper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockCarving;
import com.unforbidable.tfc.bids.Blocks.BlockClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockDryingRack;
import com.unforbidable.tfc.bids.Blocks.BlockFireClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockNewFirepit;
import com.unforbidable.tfc.bids.Blocks.BlockLogWall;
import com.unforbidable.tfc.bids.Blocks.BlockMudChimney;
import com.unforbidable.tfc.bids.Blocks.BlockQuarry;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStone;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStoneBrick;
import com.unforbidable.tfc.bids.Blocks.BlockStackedFirewood;
import com.unforbidable.tfc.bids.Blocks.BlockTiedStickBundle;
import com.unforbidable.tfc.bids.Blocks.BlockWoodPile;
import com.unforbidable.tfc.bids.Blocks.BlockChoppingBlock;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingLogWall;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingRawStone;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingRoughStone;
import com.unforbidable.tfc.bids.Core.Quarry.Quarriables.QuarriableStone;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileMessage;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemChoppingBlock;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemDryingRack;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemLogWall;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemLogWall16;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemLogWall32;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemMudChimney;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemQuarry;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemRoughStone;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemRoughStoneBrick;
import com.unforbidable.tfc.bids.Render.Blocks.RenderCarving;
import com.unforbidable.tfc.bids.Render.Blocks.RenderClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderDryingRack;
import com.unforbidable.tfc.bids.Render.Blocks.RenderFireClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderQuarry;
import com.unforbidable.tfc.bids.Render.Blocks.RenderWoodPile;
import com.unforbidable.tfc.bids.Render.Blocks.RenderChoppingBlock;
import com.unforbidable.tfc.bids.Render.Tiles.TileRenderDryingRack;
import com.unforbidable.tfc.bids.Render.Tiles.TileRenderChoppingBlock;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChoppingBlock;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.BidsConstants.ChoppingBlock;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;

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
                .setNames(Global.STONE_SED).setBlockName("RoughStoneSed")
                .setBlockTextureName("Rough");

        roughStoneBrickSed = new BlockRoughStoneBrick()
                .setNames(Global.STONE_SED).setBlockName("RoughStoneBrickSed")
                .setBlockTextureName("Rough Brick");

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

        tiedStickBundle = new BlockTiedStickBundle().setBlockName("TiedStickBundle");

        stackedFirewood = new BlockStackedFirewood(0).setBlockName("StackedFirewood");
        stackedFirewood2 = new BlockStackedFirewood(16).setBlockName("StackedFirewood2");
        stackedFirewood3 = new BlockStackedFirewood(32).setBlockName("StackedFirewood3");

        dryingRack = new BlockDryingRack().setBlockName("DryingRack");

        choppingBlock = new BlockChoppingBlock(TFCBlocks.woodVert, ChoppingBlock.DEFAULT)
                .setBlockName("ChoppingBlock");
        choppingBlock2 = new BlockChoppingBlock(TFCBlocks.woodVert2, ChoppingBlock.DEFAULT)
                .setBlockName("ChoppingBlock2");
        choppingBlock3 = new BlockChoppingBlock(TFCBlocks.woodVert3, ChoppingBlock.DEFAULT)
                .setBlockName("ChoppingBlock3");
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

        carvingRock.setHarvestLevel("shovel", 0);
        carvingWood.setHarvestLevel("axe", 0);
    }

    private static void setupFireInfo() {
        Bids.LOG.info("Set block flamability");

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

        Blocks.fire.setFireInfo(carvingWood, 5, 5);

        Blocks.fire.setFireInfo(woodPile, 10, 10);

        Blocks.fire.setFireInfo(choppingBlock, 5, 5);
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
        CarvingRegistry.registerCarving(new CarvingStackedLogs());
    }

    private static void registerQuarryBlocks() {
        Bids.LOG.info("Register quarriable blocks");

        QuarryRegistry.registerQuarryBlock(new QuarriableStone(TFCBlocks.stoneSed, roughStoneSed, 1, 1));
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
    }

    @SideOnly(Side.CLIENT)
    private static void registerTileEntitiesClientOnly() {
        Bids.LOG.info("Bind tile entity special renderers");

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingRack.class, new TileRenderDryingRack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChoppingBlock.class, new TileRenderChoppingBlock());
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
    }

}
