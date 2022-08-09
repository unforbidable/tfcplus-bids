package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelCoalTFC;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelLogsTFC;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelPeatTFC;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelStickBundleTFC;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelStickTFC;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderLogsTFC;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderThickLogsTFC;
import com.unforbidable.tfc.bids.Items.ItemAdze;
import com.unforbidable.tfc.bids.Items.ItemDrill;
import com.unforbidable.tfc.bids.Items.ItemDrinkingGlass;
import com.unforbidable.tfc.bids.Items.ItemDrinkingPottery;
import com.unforbidable.tfc.bids.Items.ItemFlatGlass;
import com.unforbidable.tfc.bids.Items.ItemGenericPottery;
import com.unforbidable.tfc.bids.Items.ItemGenericToolHead;
import com.unforbidable.tfc.bids.Items.ItemGlassLump;
import com.unforbidable.tfc.bids.Items.ItemKindling;
import com.unforbidable.tfc.bids.Items.ItemLogsSeasoned;
import com.unforbidable.tfc.bids.Items.ItemMetalBlowpipe;
import com.unforbidable.tfc.bids.Items.ItemOreBit;
import com.unforbidable.tfc.bids.Items.ItemPeeledLog;
import com.unforbidable.tfc.bids.Items.ItemPeeledLogSeasoned;
import com.unforbidable.tfc.bids.Items.ItemRoughBrick;
import com.unforbidable.tfc.bids.Items.ItemSmallStickBundle;
import com.unforbidable.tfc.bids.Items.ItemTiedStickBundle;
import com.unforbidable.tfc.bids.Render.Item.WoodPileItemRenderer;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.FirepitRegistry;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSetup extends BidsItems {

    public static void preInit() {
        initItems();
        setupToolHarvest();
        registerItems();
        registerOre();
        registerWoodPileItems();
        registerFirepitFuel();
    }

    public static void postInit() {
        registerPartialMolds();
        registerHeat();
    }

    @SideOnly(Side.CLIENT)
    public static void postInitClientOnly() {
        registerItemRenderers();
    }

    private static void initItems() {
        Bids.LOG.info("Initialize items");

        oreBit = new ItemOreBit().setUnlocalizedName("Ore Bit");

        metalBlowpipe = new ItemMetalBlowpipe().setUnlocalizedName("Metal Blowpipe");
        brassBlowpipe = new ItemMetalBlowpipe().setUnlocalizedName("Brass Blowpipe");

        flatGlass = new ItemFlatGlass().setUnlocalizedName("Flat Glass");

        drinkingGlass = new ItemDrinkingGlass().setGlassReturnAmount(40).setMaxStackSize(4)
                .setUnlocalizedName("Drinking Glass");
        glassJug = new ItemDrinkingGlass().setGlassReturnAmount(80).setMaxStackSize(4)
                .setUnlocalizedName("Glass Jug");
        shotGlass = new ItemDrinkingGlass().setGlassReturnAmount(20).setMaxStackSize(4)
                .setUnlocalizedName("Shot Glass");

        clayPipe = new ItemGenericPottery(true).setUnlocalizedName("Pottery Pipe");

        clayMug = new ItemDrinkingPottery().setUnlocalizedName("Pottery Mug");

        glassLump = new ItemGlassLump().setUnlocalizedName("Glass Lump");

        igInStoneDrillHead = new ItemGenericToolHead(TFCItems.igInToolMaterial)
                .setUnlocalizedName("IgIn Stone Drill Head");
        sedStoneDrillHead = new ItemGenericToolHead(TFCItems.sedToolMaterial)
                .setUnlocalizedName("Sed Stone Drill Head");
        igExStoneDrillHead = new ItemGenericToolHead(TFCItems.igExToolMaterial)
                .setUnlocalizedName("IgEx Stone Drill Head");
        mMStoneDrillHead = new ItemGenericToolHead(TFCItems.mMToolMaterial)
                .setUnlocalizedName("MM Stone Drill Head");

        igInStoneDrill = new ItemDrill(TFCItems.igInToolMaterial)
                .setUnlocalizedName("IgIn Stone Drill");
        sedStoneDrill = new ItemDrill(TFCItems.sedToolMaterial)
                .setUnlocalizedName("Sed Stone Drill");
        igExStoneDrill = new ItemDrill(TFCItems.igExToolMaterial)
                .setUnlocalizedName("IgEx Stone Drill");
        mMStoneDrill = new ItemDrill(TFCItems.mMToolMaterial)
                .setUnlocalizedName("MM Stone Drill");

        igInStoneAdzeHead = new ItemGenericToolHead(TFCItems.igInToolMaterial)
                .setUnlocalizedName("IgIn Stone Adze Head");
        sedStoneAdzeHead = new ItemGenericToolHead(TFCItems.sedToolMaterial)
                .setUnlocalizedName("Sed Stone Adze Head");
        igExStoneAdzeHead = new ItemGenericToolHead(TFCItems.igExToolMaterial)
                .setUnlocalizedName("IgEx Stone Adze Head");
        mMStoneAdzeHead = new ItemGenericToolHead(TFCItems.mMToolMaterial)
                .setUnlocalizedName("MM Stone Adze Head");

        igInStoneAdze = new ItemAdze(TFCItems.igInToolMaterial)
                .setUnlocalizedName("IgIn Stone Adze");
        sedStoneAdze = new ItemAdze(TFCItems.sedToolMaterial)
                .setUnlocalizedName("Sed Stone Adze");
        igExStoneAdze = new ItemAdze(TFCItems.igExToolMaterial)
                .setUnlocalizedName("IgEx Stone Adze");
        mMStoneAdze = new ItemAdze(TFCItems.mMToolMaterial)
                .setUnlocalizedName("MM Stone Adze");

        sedRoughStoneLooseBrick = new ItemRoughBrick().setNames(Global.STONE_SED)
                .setUnlocalizedName("Sed Rough Stone Loose Brick");

        peeledLog = new ItemPeeledLog().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Peeled Log");
        peeledLogSeasoned = new ItemPeeledLogSeasoned().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Peeled Log Seasoned");
        logsSeasoned = new ItemLogsSeasoned()
                .setUnlocalizedName("Log Seasoned");

        smallStickBundle = new ItemSmallStickBundle()
                .setUnlocalizedName("Small Stick Bundle");
        tiedStickBundle = new ItemTiedStickBundle()
                .setUnlocalizedName("Tied Stick Bundle");
        kindling = new ItemKindling()
                .setUnlocalizedName("Kindling");
    }

    private static void setupToolHarvest() {
        Bids.LOG.info("Set tool harvesting capabilities");

        sedStoneAdze.setHarvestLevel("shovel", 1);
        mMStoneAdze.setHarvestLevel("shovel", 1);
        igInStoneAdze.setHarvestLevel("shovel", 1);
        igExStoneAdze.setHarvestLevel("shovel", 1);

        sedStoneAdze.setHarvestLevel("axe", 1);
        mMStoneAdze.setHarvestLevel("axe", 1);
        igInStoneAdze.setHarvestLevel("axe", 1);
        igExStoneAdze.setHarvestLevel("axe", 1);
    }

    private static void registerOre() {
        Bids.LOG.info("Register item ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        final Item[] adzes = new Item[] { sedStoneAdze, mMStoneAdze, igInStoneAdze, igExStoneAdze };
        for (Item adze : adzes) {
            OreDictionary.registerOre("itemAdze", new ItemStack(adze, 1, WILD));
        }

        OreDictionary.registerOre("itemAdzeStone", new ItemStack(sedStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(mMStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(igInStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(igExStoneAdze, 1, WILD));

        OreDictionary.registerOre("itemRoughStoneBrickLoose", new ItemStack(sedRoughStoneLooseBrick, 1, WILD));
    }

    private static void registerWoodPileItems() {
        Bids.LOG.info("Register wood pile items");

        WoodPileRegistry.registerItem(peeledLog);
        WoodPileRegistry.registerItem(peeledLogSeasoned);
        WoodPileRegistry.registerItem(TFCItems.logs, RenderLogsTFC.class);
        WoodPileRegistry.registerItem(logsSeasoned, RenderLogsTFC.class);
        WoodPileRegistry.registerItem(TFCItems.thickLogs, RenderThickLogsTFC.class);
        WoodPileRegistry.registerItem(tiedStickBundle);
    }

    private static void registerFirepitFuel() {
        Bids.LOG.info("Register firepit fuel");

        FirepitRegistry.registerFuel(kindling);
        FirepitRegistry.registerFuel(smallStickBundle);
        FirepitRegistry.registerFuel(tiedStickBundle);
        FirepitRegistry.registerFuel(TFCItems.stick, FuelStickTFC.class);
        FirepitRegistry.registerFuel(TFCItems.fireStarter, FuelStickTFC.class);
        FirepitRegistry.registerFuel(TFCItems.stickBundle, FuelStickBundleTFC.class);
        FirepitRegistry.registerFuel(Item.getItemFromBlock(TFCBlocks.peat), FuelPeatTFC.class);
        FirepitRegistry.registerFuel(TFCItems.logs, FuelLogsTFC.class);

        if (BidsOptions.Firepit.allowFuelCharcoal) {
            FirepitRegistry.registerFuel(TFCItems.coal, FuelCoalTFC.class);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemRenderers() {
        Bids.LOG.info("Register item renderers");

        for (Item item : WoodPileRegistry.getItems()) {
            MinecraftForgeClient.registerItemRenderer(item, new WoodPileItemRenderer());
        }
    }

    private static void registerPartialMolds() {
        Bids.LOG.info("Register items as TFC molds");

        Global.GLASS.addValidPartialMold(metalBlowpipe, 2, metalBlowpipe, 1, 2);
        Global.GLASS.addValidPartialMold(brassBlowpipe, 2, brassBlowpipe, 1, 2);
    }

    private static void registerHeat() {
        Bids.LOG.info("Register TFC heat data for items");

        final HeatRegistry reg = HeatRegistry.getInstance();

        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            ItemStack smallOre = new ItemStack(TFCItems.smallOreChunk, 1, i);
            HeatIndex smallOreIndex = reg.findMatchingIndex(smallOre);
            if (smallOreIndex != null) {
                HeatRaw raw = new HeatRaw(smallOreIndex.specificHeat, smallOreIndex.meltTemp);

                ItemStack is = new ItemStack(oreBit, 1, i);
                reg.addIndex(new HeatIndex(is, raw, new ItemStack(smallOreIndex.getOutputItem(), 1)));
            }
        }
    }

    private static void registerItems() {
        Bids.LOG.info("Register items");

        GameRegistry.registerItem(oreBit, oreBit.getUnlocalizedName());

        GameRegistry.registerItem(metalBlowpipe, metalBlowpipe.getUnlocalizedName());
        GameRegistry.registerItem(brassBlowpipe, brassBlowpipe.getUnlocalizedName());

        GameRegistry.registerItem(flatGlass, flatGlass.getUnlocalizedName());

        GameRegistry.registerItem(drinkingGlass, drinkingGlass.getUnlocalizedName());
        GameRegistry.registerItem(glassJug, glassJug.getUnlocalizedName());
        GameRegistry.registerItem(shotGlass, shotGlass.getUnlocalizedName());

        GameRegistry.registerItem(clayPipe, clayPipe.getUnlocalizedName());

        GameRegistry.registerItem(clayMug, clayMug.getUnlocalizedName());

        GameRegistry.registerItem(glassLump, glassLump.getUnlocalizedName());

        GameRegistry.registerItem(igInStoneDrillHead, igInStoneDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(sedStoneDrillHead, sedStoneDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(igExStoneDrillHead, igExStoneDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(mMStoneDrillHead, mMStoneDrillHead.getUnlocalizedName());

        GameRegistry.registerItem(igInStoneDrill, igInStoneDrill.getUnlocalizedName());
        GameRegistry.registerItem(sedStoneDrill, sedStoneDrill.getUnlocalizedName());
        GameRegistry.registerItem(igExStoneDrill, igExStoneDrill.getUnlocalizedName());
        GameRegistry.registerItem(mMStoneDrill, mMStoneDrill.getUnlocalizedName());

        GameRegistry.registerItem(igInStoneAdzeHead, igInStoneAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(sedStoneAdzeHead, sedStoneAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(igExStoneAdzeHead, igExStoneAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(mMStoneAdzeHead, mMStoneAdzeHead.getUnlocalizedName());

        GameRegistry.registerItem(igInStoneAdze, igInStoneAdze.getUnlocalizedName());
        GameRegistry.registerItem(sedStoneAdze, sedStoneAdze.getUnlocalizedName());
        GameRegistry.registerItem(igExStoneAdze, igExStoneAdze.getUnlocalizedName());
        GameRegistry.registerItem(mMStoneAdze, mMStoneAdze.getUnlocalizedName());

        GameRegistry.registerItem(sedRoughStoneLooseBrick, sedRoughStoneLooseBrick.getUnlocalizedName());

        GameRegistry.registerItem(peeledLog, peeledLog.getUnlocalizedName());
        GameRegistry.registerItem(peeledLogSeasoned, peeledLogSeasoned.getUnlocalizedName());
        GameRegistry.registerItem(logsSeasoned, logsSeasoned.getUnlocalizedName());

        GameRegistry.registerItem(smallStickBundle, smallStickBundle.getUnlocalizedName());
        GameRegistry.registerItem(tiedStickBundle, tiedStickBundle.getUnlocalizedName());
        GameRegistry.registerItem(kindling, kindling.getUnlocalizedName());
    }

}
