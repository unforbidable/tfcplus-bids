package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingRoughStoneBrick;
import com.unforbidable.tfc.bids.Core.Network.NetworkHelper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockCarving;
import com.unforbidable.tfc.bids.Blocks.BlockClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockFireClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockMudChimney;
import com.unforbidable.tfc.bids.Blocks.BlockQuarry;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStone;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStoneBrick;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Carving.Carvings.CarvingRoughStone;
import com.unforbidable.tfc.bids.Core.Quarry.Quarriables.QuarriableStone;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemMudChimney;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemQuarry;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemRoughStone;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemRoughStoneBrick;
import com.unforbidable.tfc.bids.Render.Blocks.RenderCarving;
import com.unforbidable.tfc.bids.Render.Blocks.RenderClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderFireClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderQuarry;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.api.QuarryRegistry;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
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
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClientOnly() {
        registerBlockRenderers();
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

        roughStoneSed = new BlockRoughStone()
                .setNames(Global.STONE_SED).setBlockName("RoughStoneSed")
                .setBlockTextureName("Rough");

        roughStoneBrickSed = new BlockRoughStoneBrick()
                .setNames(Global.STONE_SED).setBlockName("RoughStoneBrickSed")
                .setBlockTextureName("Rough Brick");
    }

    private static void updateBlocks() {
        Bids.LOG.info("Update blocks");

        if (BidsOptions.Crucible.enableClassicHandBreakable) {
            // Lower the hardness of the classic TFC crucible
            // The original value is 4.0f
            Bids.LOG.info("Classic TFC crucible hardness reduced");
            TFCBlocks.crucible.setHardness(0.5f);
        }
    }

    private static void setupHarvest() {
        Bids.LOG.info("Set block harvestability");

        roughStoneSed.setHarvestLevel("shovel", 0);

        roughStoneBrickSed.setHarvestLevel("shovel", 0);

        carvingRock.setHarvestLevel("shovel", 0);
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
    }

    private static void registerTileEntities() {
        Bids.LOG.info("Register tile entities");

        GameRegistry.registerTileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        GameRegistry.registerTileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");

        GameRegistry.registerTileEntity(TileEntityChimney.class, "BidsChimney");

        GameRegistry.registerTileEntity(TileEntityQuarry.class, "BidsQuarry");

        GameRegistry.registerTileEntity(TileEntityCarving.class, "BidsCarving");
    }

    private static void registerMessages() {
        Bids.LOG.info("Register tile entity messages");

        Bids.network.registerMessage(CarvingMessage.ServerHandler.class, CarvingMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(CarvingMessage.ClientHandler.class, CarvingMessage.class,
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

        GameRegistry.registerBlock(roughStoneSed, ItemRoughStone.class, "RoughStoneSed");

        GameRegistry.registerBlock(roughStoneBrickSed, ItemRoughStoneBrick.class, "RoughStoneBrickSed");
    }

}
