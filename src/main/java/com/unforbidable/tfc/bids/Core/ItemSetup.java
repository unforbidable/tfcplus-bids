package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.Render.Item.FoodItemRenderer;
import com.dunk.tfc.api.*;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IBoots;
import com.dunk.tfc.api.Interfaces.IEquipable;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.*;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderLogsTFC;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderThickLogsTFC;
import com.unforbidable.tfc.bids.Items.*;
import com.unforbidable.tfc.bids.Render.Item.SeasonableItemRenderer;
import com.unforbidable.tfc.bids.Render.Item.SeasonedItemRenderer;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsConstants.ExtraClothing;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.FirepitRegistry;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSetup extends BidsItems {

    public static void preInit() {
        initItems();
        setupToolHarvest();
        registerItems();
        registerFluidContainers();
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

        flatGlass = new ItemGenericFlat().setUnlocalizedName("Flat Glass");

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

        copperAdze = new ItemAdze(TFCItems.copperToolMaterial)
            .setUnlocalizedName("Copper Adze");
        bronzeAdze = new ItemAdze(TFCItems.bronzeToolMaterial)
            .setUnlocalizedName("Bronze Adze");
        bismuthBronzeAdze = new ItemAdze(TFCItems.bismuthBronzeToolMaterial)
            .setUnlocalizedName("Bismuth Bronze Adze");
        blackBronzeAdze = new ItemAdze(TFCItems.blackBronzeToolMaterial)
            .setUnlocalizedName("Black Bronze Adze");

        // Obsolete - replaced with roughStoneBrick
        sedRoughStoneLooseBrick = new ItemRoughBrick().setNames(Global.STONE_SED)
                .setMetaOnly() // No actual items
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Sed Rough Stone Loose Brick");

        roughStoneBrick = new ItemRoughBrick().setNames(Global.STONE_ALL)
                .setMetaOnly(Global.STONE_SED_START, Global.STONE_SED_START + 1, Global.STONE_SED_START + 2, Global.STONE_SED_START + 3, Global.STONE_SED_START + 4, Global.STONE_SED_START + 5, Global.STONE_SED_START + 6, Global.STONE_SED_START + 7) // Only SED
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Rough Stone Brick");

        roughStoneTile = new ItemRoughBrick().setNames(Global.STONE_ALL)
                .setMetaOnly(Global.STONE_SED_START, Global.STONE_SED_START + 4) // Shale and Sandstone only
                .setTextureName("Rough Tile")
                .setUnlocalizedName("Rough Stone Tile");

        peeledLog = new ItemPeeledLog().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Peeled Log");
        peeledLogSeasoned = new ItemPeeledLogSeasoned().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Peeled Log Seasoned");
        logsSeasoned = new ItemLogsSeasoned()
                .setUnlocalizedName("Log Seasoned");
        firewood = new ItemFirewood().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Firewood");
        firewoodSeasoned = new ItemFirewoodSeasoned().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Firewood Seasoned");

        smallStickBundle = new ItemSmallStickBundle()
                .setUnlocalizedName("Small Stick Bundle");
        tiedStickBundle = new ItemTiedStickBundle()
                .setUnlocalizedName("Tied Stick Bundle");
        kindling = new ItemKindling().setFuelKindlingQuality(0.50f)
                .setUnlocalizedName("Kindling");

        bark = new ItemBark().setNames(Global.WOOD_ALL)
                .setUnlocalizedName("Bark");
        barkFibre = new ItemBastFibre()
                .setUnlocalizedName("Bark Fibre");
        flatBarkFibre = new ItemGenericFlat().setTextureFolder("plants")
                .setUnlocalizedName("Flat Bark Fibre");
        barkFibreStrip = new ItemBastFibreStrip().setSpecialCraftingType(flatBarkFibre)
                .setUnlocalizedName("Bark Fibre Strip");
        barkCordage = new ItemBastCordage()
                .setUnlocalizedName("Bark Cordage");
        barkFibreKindling = new ItemKindling().setFuelKindlingQuality(1f)
                .setUnlocalizedName("Bark Fibre Kindling");

        flatBirchBark = new ItemGenericFlat().setTextureFolder("armor/clothing")
                .setUnlocalizedName("Flat Birch Bark");
        birchBarkSheet = new ItemGenericClothSheet().setSpecialCraftingType(flatBirchBark)
                .setUnlocalizedName("Birch Bark Sheet");
        birchBarkRepairPatch = new ItemExtraClothingPiece().setExtraPieceTypes(ExtraClothing.BIRCH_BARK)
                .setUnlocalizedName("Repair Patch");
        birchBarkBagPiece = new ItemExtraClothingPiece().setExtraPieceTypes(ExtraClothing.BIRCH_BARK)
                .setUnlocalizedName("Bag Piece");
        birchBarkStrap = new ItemExtraClothingPiece().setExtraPieceTypes(ExtraClothing.BIRCH_BARK)
                .setUnlocalizedName("Strap");
        birchBarkCupPiece = new ItemExtraClothingPiece().setExtraPieceTypes(ExtraClothing.BIRCH_BARK)
                .setUnlocalizedName("Cup Piece");
        birchBarkBag = new ItemExtraBag().setMaxDamage(12)
                .setUnlocalizedName("Birch Bark Bag");
        birchBarkCupUnfinished = new ItemMiscSewable()
                .setUnlocalizedName("Birch Bark Cup Unfinished");
        birchBarkCup = new ItemDrinkingCloth()
                .setUnlocalizedName("Birch Bark Cup");
        birchBarkKindling = new ItemKindling().setFuelKindlingQuality(1f)
                .setUnlocalizedName("Birch Bark Kindling");

        birchBarkShoes = new ItemExtraBoots(IEquipable.ClothingType.BOOTS)
                .setResourceLocation(Tags.MOD_ID, "textures/models/armor/clothing/birch_bark_shoes_color.png")
                .setArmorCoverage("SOCKS", 4)
                .setArmorType(Armor.linenCloth)
                .setMaxDamage(TFCItems.strawUses)
                .setUnlocalizedName("Birch Bark Shoes");
        ((IBoots) birchBarkShoes).setTrueBoots(false)
                .setDefaultWalkable(0.07f)
                .addWalkableSurface(Material.sand, 0.02f);
        ((ItemClothing) birchBarkShoes).setRepairCost(2);

        wheatCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Wheat Crushed");
        barleyCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Barley Crushed");
        oatCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Oat Crushed");
        ryeCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Rye Crushed");
        riceCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Rice Crushed");
        cornmealCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20, false, false)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Cornmeal Crushed");

        wheatPorridge = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Wheat Porridge");
        barleyPorridge = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Barley Porridge");
        oatPorridge = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Oat Porridge");
        ryePorridge = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Rye Porridge");
        ricePorridge = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Rice Porridge");
        cornmealPorridge = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
                .setWaterPercentage(0.25f).setNutritionAsIfCooked(true)
                .setDecayRate(2f)
                .setUnlocalizedName("Cornmeal Porridge");

        plugAndFeather = new ItemPlugAndFeather()
                .setUnlocalizedName("Plug And Feather");

        appleCrushed = new ItemExtraFood(EnumFoodGroup.Fruit, 40, 20, 0, 10, 0)
                .setDecayRate(4f)
                .setUnlocalizedName("Apple Crushed");
        oliveCrushed = new ItemExtraFood(EnumFoodGroup.Fruit, 10, 0, 3, 50, 0)
                .setDecayRate(4f)
                .setUnlocalizedName("Olive Crushed");

        oliveOilBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.OliveOil");
        vinegarBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.Vinegar");
        brineBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.Brine");
        honeyBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.Honey");

        saltWaterBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setUnlocalizedName("Glass Bottle.SaltWater");
        honeyWaterBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setUnlocalizedName("Glass Bottle.HoneyWater");
        milkVinegarBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setUnlocalizedName("Glass Bottle.MilkVinegar");

        vinegarBowl = new ItemBowlFluid(new String[] { "PotteryBowl", "Bowl" })
            .setContainerItem(TFCItems.potteryBowl)
            .setUnlocalizedName("Bowl Vinegar");
        oliveOilBowl = new ItemBowlFluid(new String[] { "PotteryBowl", "Bowl" })
            .setContainerItem(TFCItems.potteryBowl)
            .setUnlocalizedName("Bowl Olive Oil");

        largeClayBowl = new ItemLargeBowl()
            .setUnlocalizedName("Large Bowl");

        freshWaterLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.FreshWater");
        saltWaterLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.SaltWater");
        vinegarLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.Vinegar");
        milkLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.Milk");
        honeyLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.Honey");

        ceramicBucketBrine = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.Brine");
        ceramicBucketHoneyWater = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.HoneyWater");
        ceramicBucketMilkVinegar = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.MilkVinegar");

        woodenBucketBrine = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.Brine");
        woodenBucketHoneyWater = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.HoneyWater");
        woodenBucketMilkVinegar = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.MilkVinegar");

        ceramicBucketRope = new ItemBucketRopeEmpty(true)
            .setUnlocalizedName("Ceramic Bucket Rope");
        ceramicBucketRopeWater = new ItemBucketRopeFluid(true)
            .setContainerItem(ceramicBucketRope)
            .setUnlocalizedName("Ceramic Bucket Rope.FreshWater");
        woodenBucketRope = new ItemBucketRopeEmpty(false)
            .setUnlocalizedName("Wooden Bucket Rope");
        woodenBucketRopeWater = new ItemBucketRopeFluid(false)
            .setContainerItem(woodenBucketRope)
            .setUnlocalizedName("Wooden Bucket Rope.FreshWater");
    }

    private static void registerFluidContainers() {
        FluidHelper.registerPartialFluidContainer(TFCFluids.OLIVEOIL, TFCItems.glassBottle, 0, oliveOilBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.VINEGAR, TFCItems.glassBottle, 0, vinegarBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.BRINE, TFCItems.glassBottle, 0, brineBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.HONEY, TFCItems.glassBottle, 0, honeyBottle, 50, 1000);

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.SALTWATER, 1000),
            new ItemStack(saltWaterBottle), new ItemStack(TFCItems.glassBottle));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEYWATER, 1000),
            new ItemStack(honeyWaterBottle), new ItemStack(TFCItems.glassBottle));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILKVINEGAR, 1000),
            new ItemStack(milkVinegarBottle), new ItemStack(TFCItems.glassBottle));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.VINEGAR, 250),
            new ItemStack(vinegarBowl, 1, 0), new ItemStack(TFCItems.potteryBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.VINEGAR, 250),
            new ItemStack(vinegarBowl, 1, 1), new ItemStack(TFCItems.potteryBowl, 1, 2));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.OLIVEOIL, 250),
            new ItemStack(oliveOilBowl, 1, 0), new ItemStack(TFCItems.potteryBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.OLIVEOIL, 250),
            new ItemStack(oliveOilBowl, 1, 1), new ItemStack(TFCItems.potteryBowl, 1, 2));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, 1000),
            new ItemStack(ceramicBucketBrine), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEYWATER, 1000),
            new ItemStack(ceramicBucketHoneyWater), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILKVINEGAR, 1000),
            new ItemStack(ceramicBucketMilkVinegar), new ItemStack(TFCItems.clayBucketEmpty));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, 1000),
            new ItemStack(woodenBucketBrine), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEYWATER, 1000),
            new ItemStack(woodenBucketHoneyWater), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILKVINEGAR, 1000),
            new ItemStack(woodenBucketMilkVinegar), new ItemStack(TFCItems.woodenBucketEmpty));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, 1000),
            new ItemStack(ceramicBucketRopeWater), new ItemStack(ceramicBucketRope));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, 1000),
            new ItemStack(woodenBucketRopeWater), new ItemStack(woodenBucketRope));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, 500),
            new ItemStack(freshWaterLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.SALTWATER, 500),
            new ItemStack(saltWaterLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.VINEGAR, 500),
            new ItemStack(vinegarLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILK, 500),
            new ItemStack(milkLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEY, 500),
            new ItemStack(honeyLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
    }

    private static void setupToolHarvest() {
        Bids.LOG.info("Set tool harvesting capabilities");

        sedStoneAdze.setHarvestLevel("shovel", 1);
        mMStoneAdze.setHarvestLevel("shovel", 1);
        igInStoneAdze.setHarvestLevel("shovel", 1);
        igExStoneAdze.setHarvestLevel("shovel", 1);
        copperAdze.setHarvestLevel("shovel", 1);
        bronzeAdze.setHarvestLevel("shovel", 1);
        bismuthBronzeAdze.setHarvestLevel("shovel", 1);
        blackBronzeAdze.setHarvestLevel("shovel", 1);

        sedStoneAdze.setHarvestLevel("axe", 1);
        mMStoneAdze.setHarvestLevel("axe", 1);
        igInStoneAdze.setHarvestLevel("axe", 1);
        igExStoneAdze.setHarvestLevel("axe", 1);
        copperAdze.setHarvestLevel("axe", 1);
        bronzeAdze.setHarvestLevel("axe", 1);
        bismuthBronzeAdze.setHarvestLevel("axe", 1);
        blackBronzeAdze.setHarvestLevel("axe", 1);
    }

    private static void registerOre() {
        Bids.LOG.info("Register item ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        final Item[] drills = new Item[] { sedStoneDrill, mMStoneDrill, igInStoneDrill, igExStoneDrill };
        for (Item drill : drills) {
            OreDictionary.registerOre("itemDrill", new ItemStack(drill, 1, WILD));
        }

        final Item[] adzes = new Item[] { sedStoneAdze, mMStoneAdze, igInStoneAdze, igExStoneAdze };
        for (Item adze : adzes) {
            OreDictionary.registerOre("itemAdze", new ItemStack(adze, 1, WILD));
        }
        OreDictionary.registerOre("itemAdze", new ItemStack(copperAdze, 1, WILD));
        OreDictionary.registerOre("itemAdze", new ItemStack(bronzeAdze, 1, WILD));
        OreDictionary.registerOre("itemAdze", new ItemStack(bismuthBronzeAdze, 1, WILD));
        OreDictionary.registerOre("itemAdze", new ItemStack(blackBronzeAdze, 1, WILD));

        OreDictionary.registerOre("itemAdzeStone", new ItemStack(sedStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(mMStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(igInStoneAdze, 1, WILD));
        OreDictionary.registerOre("itemAdzeStone", new ItemStack(igExStoneAdze, 1, WILD));

        final Item[] logs = new Item[] { logsSeasoned, peeledLog, peeledLogSeasoned };
        for (Item log : logs) {
            OreDictionary.registerOre("itemLogExtra", new ItemStack(log, 1, WILD));
        }
    }

    private static void registerWoodPileItems() {
        Bids.LOG.info("Register wood pile items");

        WoodPileRegistry.registerSeasonableItem(peeledLog);
        WoodPileRegistry.registerSeasonedItem(peeledLogSeasoned);
        WoodPileRegistry.registerSeasonableItem(TFCItems.logs, RenderLogsTFC.class);
        WoodPileRegistry.registerSeasonedItem(logsSeasoned, RenderLogsTFC.class);
        WoodPileRegistry.registerItem(TFCItems.thickLogs, RenderThickLogsTFC.class);
        WoodPileRegistry.registerItem(tiedStickBundle);
        WoodPileRegistry.registerSeasonableItem(firewood);
        WoodPileRegistry.registerSeasonedItem(firewoodSeasoned);
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
        FirepitRegistry.registerFuel(bark);
        FirepitRegistry.registerFuel(barkFibreKindling);
        FirepitRegistry.registerFuel(birchBarkKindling);
        FirepitRegistry.registerFuel(firewoodSeasoned);

        if (BidsOptions.Firepit.allowFuelLogsTFC) {
            FirepitRegistry.registerFuel(TFCItems.logs, FuelLogsTFC.class);
        }

        if (BidsOptions.Firepit.allowFuelCharcoal) {
            FirepitRegistry.registerFuel(TFCItems.coal, FuelCoalTFC.class);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemRenderers() {
        Bids.LOG.info("Register item renderers");

        for (Item item : WoodPileRegistry.getSeasonableItems()) {
            MinecraftForgeClient.registerItemRenderer(item, new SeasonableItemRenderer());
        }

        for (Item item : WoodPileRegistry.getSeasonedItems()) {
            MinecraftForgeClient.registerItemRenderer(item, new SeasonedItemRenderer());
        }

        Item[] foodItems = { wheatCrushed, barleyCrushed, oatCrushed, riceCrushed, ryeCrushed,
                cornmealCrushed, wheatPorridge, barleyPorridge, oatPorridge, ricePorridge, ryePorridge,
                cornmealPorridge, appleCrushed, oliveCrushed };

        for (Item item : foodItems) {
            MinecraftForgeClient.registerItemRenderer(item, new FoodItemRenderer());
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

        GameRegistry.registerItem(bark, bark.getUnlocalizedName());
        GameRegistry.registerItem(barkFibre, barkFibre.getUnlocalizedName());
        GameRegistry.registerItem(barkFibreStrip, barkFibreStrip.getUnlocalizedName());
        GameRegistry.registerItem(flatBarkFibre, flatBarkFibre.getUnlocalizedName());
        GameRegistry.registerItem(barkCordage, barkCordage.getUnlocalizedName());
        GameRegistry.registerItem(barkFibreKindling, barkFibreKindling.getUnlocalizedName());

        GameRegistry.registerItem(firewood, firewood.getUnlocalizedName());
        GameRegistry.registerItem(firewoodSeasoned, firewoodSeasoned.getUnlocalizedName());

        GameRegistry.registerItem(flatBirchBark, flatBirchBark.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkSheet, birchBarkSheet.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkStrap, birchBarkStrap.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkRepairPatch, birchBarkRepairPatch.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkBagPiece, birchBarkBagPiece.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkBag, birchBarkBag.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkCupPiece, birchBarkCupPiece.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkCupUnfinished, birchBarkCupUnfinished.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkCup, birchBarkCup.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkShoes, birchBarkShoes.getUnlocalizedName());
        GameRegistry.registerItem(birchBarkKindling, birchBarkKindling.getUnlocalizedName());

        GameRegistry.registerItem(wheatCrushed, wheatCrushed.getUnlocalizedName());
        GameRegistry.registerItem(barleyCrushed, barleyCrushed.getUnlocalizedName());
        GameRegistry.registerItem(oatCrushed, oatCrushed.getUnlocalizedName());
        GameRegistry.registerItem(ryeCrushed, ryeCrushed.getUnlocalizedName());
        GameRegistry.registerItem(riceCrushed, riceCrushed.getUnlocalizedName());
        GameRegistry.registerItem(cornmealCrushed, cornmealCrushed.getUnlocalizedName());
        GameRegistry.registerItem(wheatPorridge, wheatPorridge.getUnlocalizedName());
        GameRegistry.registerItem(barleyPorridge, barleyPorridge.getUnlocalizedName());
        GameRegistry.registerItem(oatPorridge, oatPorridge.getUnlocalizedName());
        GameRegistry.registerItem(ryePorridge, ryePorridge.getUnlocalizedName());
        GameRegistry.registerItem(ricePorridge, ricePorridge.getUnlocalizedName());
        GameRegistry.registerItem(cornmealPorridge, cornmealPorridge.getUnlocalizedName());

        GameRegistry.registerItem(plugAndFeather, plugAndFeather.getUnlocalizedName());

        GameRegistry.registerItem(appleCrushed, appleCrushed.getUnlocalizedName());
        GameRegistry.registerItem(oliveCrushed, oliveCrushed.getUnlocalizedName());

        GameRegistry.registerItem(oliveOilBottle, oliveOilBottle.getUnlocalizedName());
        GameRegistry.registerItem(vinegarBottle, vinegarBottle.getUnlocalizedName());
        GameRegistry.registerItem(saltWaterBottle, saltWaterBottle.getUnlocalizedName());
        GameRegistry.registerItem(brineBottle, brineBottle.getUnlocalizedName());
        GameRegistry.registerItem(honeyBottle, honeyBottle.getUnlocalizedName());
        GameRegistry.registerItem(honeyWaterBottle, honeyWaterBottle.getUnlocalizedName());
        GameRegistry.registerItem(milkVinegarBottle, milkVinegarBottle.getUnlocalizedName());

        GameRegistry.registerItem(vinegarBowl, vinegarBowl.getUnlocalizedName());
        GameRegistry.registerItem(oliveOilBowl, oliveOilBowl.getUnlocalizedName());

        GameRegistry.registerItem(ceramicBucketBrine, ceramicBucketBrine.getUnlocalizedName());
        GameRegistry.registerItem(ceramicBucketHoneyWater, ceramicBucketHoneyWater.getUnlocalizedName());
        GameRegistry.registerItem(ceramicBucketMilkVinegar, ceramicBucketMilkVinegar.getUnlocalizedName());

        GameRegistry.registerItem(woodenBucketBrine, woodenBucketBrine.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketHoneyWater, woodenBucketHoneyWater.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketMilkVinegar, woodenBucketMilkVinegar.getUnlocalizedName());

        GameRegistry.registerItem(ceramicBucketRope, ceramicBucketRope.getUnlocalizedName());
        GameRegistry.registerItem(ceramicBucketRopeWater, ceramicBucketRopeWater.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketRope, woodenBucketRope.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketRopeWater, woodenBucketRopeWater.getUnlocalizedName());

        GameRegistry.registerItem(roughStoneBrick, roughStoneBrick.getUnlocalizedName());
        GameRegistry.registerItem(roughStoneTile, roughStoneTile.getUnlocalizedName());

        GameRegistry.registerItem(largeClayBowl, largeClayBowl.getUnlocalizedName());
        GameRegistry.registerItem(freshWaterLargeBowl, freshWaterLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(saltWaterLargeBowl, saltWaterLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(vinegarLargeBowl, vinegarLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(milkLargeBowl, milkLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(honeyLargeBowl, honeyLargeBowl.getUnlocalizedName());

        GameRegistry.registerItem(copperAdze, copperAdze.getUnlocalizedName());
        GameRegistry.registerItem(bronzeAdze, bronzeAdze.getUnlocalizedName());
        GameRegistry.registerItem(bismuthBronzeAdze, bismuthBronzeAdze.getUnlocalizedName());
        GameRegistry.registerItem(blackBronzeAdze, blackBronzeAdze.getUnlocalizedName());
    }

}
