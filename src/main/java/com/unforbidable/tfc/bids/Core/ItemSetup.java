package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.Render.Item.FoodItemRenderer;
import com.dunk.tfc.api.*;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IBoots;
import com.dunk.tfc.api.Interfaces.IEquipable;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockUnfinishedAnvil;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.*;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderLogsTFC;
import com.unforbidable.tfc.bids.Core.WoodPile.Rendering.RenderThickLogsTFC;
import com.unforbidable.tfc.bids.Items.*;
import com.unforbidable.tfc.bids.Render.Item.SeasonableItemRenderer;
import com.unforbidable.tfc.bids.Render.Item.SeasonedItemRenderer;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsConstants.ExtraClothing;
import com.unforbidable.tfc.bids.api.*;
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
        registerOre();
        registerWoodPileItems();
        registerFirepitFuel();
    }

    public static void postInit() {
        registerFluidContainers();
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

        Metal[] toolMetals = new Metal[] { Global.COPPER, Global.BRONZE, Global.BISMUTHBRONZE, Global.BLACKBRONZE };
        clayMoldAdze = new ItemGenericPotteryMold()
            .setCounter(4)
            .setMetals(toolMetals)
            .setMetaNames(new String[] { "Clay", "Ceramic", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze" })
            .setUnlocalizedName("Mold Adze");
        clayMoldDrill = new ItemGenericPotteryMold()
            .setCounter(4)
            .setMetals(toolMetals)
            .setMetaNames(new String[] { "Clay", "Ceramic", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze" })
            .setUnlocalizedName("Mold Drill");

        copperAdzeHead = new ItemGenericToolHead()
            .setUnlocalizedName("Copper Adze Head");
        bronzeAdzeHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bronze Adze Head");
        bismuthBronzeAdzeHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bismuth Bronze Adze Head");
        blackBronzeAdzeHead = new ItemGenericToolHead()
            .setUnlocalizedName("Black Bronze Adze Head");

        copperDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Copper Drill Head");
        bronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bronze Drill Head");
        bismuthBronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bismuth Bronze Drill Head");
        blackBronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Black Bronze Drill Head");

        copperAdze = new ItemAdze(TFCItems.copperToolMaterial)
            .setEquipmentTier(1)
            .setUnlocalizedName("Copper Adze");
        bronzeAdze = new ItemAdze(TFCItems.bronzeToolMaterial)
            .setEquipmentTier(2)
            .setUnlocalizedName("Bronze Adze");
        bismuthBronzeAdze = new ItemAdze(TFCItems.bismuthBronzeToolMaterial)
            .setEquipmentTier(2)
            .setUnlocalizedName("Bismuth Bronze Adze");
        blackBronzeAdze = new ItemAdze(TFCItems.blackBronzeToolMaterial)
            .setEquipmentTier(2)
            .setUnlocalizedName("Black Bronze Adze");

        copperDrill = new ItemDrill(TFCItems.copperToolMaterial)
            .setEquipmentTier(1)
            .setDurationMultiplier(0.8f)
            .setUnlocalizedName("Copper Drill");
        bronzeDrill = new ItemDrill(TFCItems.bronzeToolMaterial)
            .setEquipmentTier(2)
            .setDurationMultiplier(0.5f)
            .setUnlocalizedName("Bronze Drill");
        bismuthBronzeDrill = new ItemDrill(TFCItems.bismuthBronzeToolMaterial)
            .setEquipmentTier(2)
            .setDurationMultiplier(0.5f)
            .setUnlocalizedName("Bismuth Bronze Drill");
        blackBronzeDrill = new ItemDrill(TFCItems.blackBronzeToolMaterial)
            .setEquipmentTier(2)
            .setDurationMultiplier(0.5f)
            .setUnlocalizedName("Black Bronze Drill");

        // Obsolete - replaced with roughStoneBrick
        sedRoughStoneLooseBrick = new ItemRoughBrick().setNames(Global.STONE_SED)
                .setMetaOnly() // No actual items
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Sed Rough Stone Loose Brick");

        roughStoneBrick = new ItemRoughBrick().setNames(Global.STONE_ALL)
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Rough Stone Brick");

        roughStoneTile = new ItemRoughBrick().setNames(Global.STONE_ALL)
                .setMetaOnly(Global.STONE_SED_START, Global.STONE_SED_START + 4, Global.STONE_MM_START + 1) // Shale, Sandstone and Slate only
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

        coatBodyFrontLeather = new ItemExtraClothingPiece()
            .setExtraPieceTypes(ExtraClothing.LEATHER)
            .setUnlocalizedName("Coat Front");
        coatBodyBackLeather = new ItemExtraClothingPiece()
            .setExtraPieceTypes(ExtraClothing.LEATHER)
            .setUnlocalizedName("Coat Back");

        leatherCoat = new ItemExtraCoat(IEquipable.ClothingType.COAT)
            .setResourceLocation(Tags.MOD_ID, "textures/models/armor/leather_coat_color.png")
            .setBodySunProtection(1f)
            .setColdResistance(2)
            .setHeatResistance(-1)
            .setArmorType(Armor.leather)
            .setMaxDamage(TFCItems.leatherUses)
            .setUnlocalizedName("Leather Coat");
        ((ItemClothing) leatherCoat).setRepairCost(8);
        ((ItemClothing) leatherCoat).setArmorCoverage("LONG_SLEEVES", 2)
            .setArmorCoverage("FULL_SHIRT_TORSO", 1)
            .setArmorCoverage("SHORTS_LEGS", 3);

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

        goatCheese = new ItemExtraFood(EnumFoodGroup.Dairy, 0, 35, 20, 0, 20)
            .setWaterPercentage(0.6f)
            .setDecayRate(0.5f)
            .setCanSmoke()
            .setUnlocalizedName("Goat Cheese");
        ((ItemFoodTFC) goatCheese).setSmokeAbsorbMultiplier(1F);

        suet = new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 30)
            .setDecayRate(2.5f)
            .setPoisonOnRaw(true)
            .setUnlocalizedName("Suet");
        tallow = new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 60)
            .setDecayRate(0.05f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Tallow");

        seaBeet = new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 40, 10, 0)
            .setWaterPercentage(0.2f)
            .setDecayRate(1.4f)
            .setUnlocalizedName("Sea Beet");
        beetroot = new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 10, 30)
            .setWaterPercentage(0.1f)
            .setDecayRate(0.8f)
            .setUnlocalizedName("Beetroot");
        sugarBeet = new ItemExtraFood(EnumFoodGroup.Vegetable, 60, 0, 0, 0, 0)
            .setWaterPercentage(0.1f)
            .setDecayRate(0.8f)
            .setUnlocalizedName("Sugar Beet");
        wildBeans = new ItemExtraFood(EnumFoodGroup.Protein, 10, 0, 0, 10, 20)
            .setCookTempIndex(1)
            .setDecayRate(0.5f)
            .setUnlocalizedName("Wild Beans");
        broadBeans = new ItemExtraFood(EnumFoodGroup.Protein, 10, 0, 0, 10, 40)
            .setCookTempIndex(1)
            .setDecayRate(0.25f)
            .setUnlocalizedName("Broad Beans");

        seedsSeaBeet = new ItemNewCustomSeeds(BidsCrops.SEEBEAT)
            .setUnlocalizedName("Seeds Sea Beet");
        seedsBeetroot = new ItemNewCustomSeeds(BidsCrops.BEETROOT)
            .setUnlocalizedName("Seeds Beetroot");
        seedsSugarBeet = new ItemNewCustomSeeds(BidsCrops.SUGARBEET)
            .setUnlocalizedName("Seeds Sugar Beet");
        seedsWildBeans = new ItemNewCustomSeeds(BidsCrops.WILDBEANS)
            .setUnlocalizedName("Seeds Wild Bean");
        seedsBroadBeans = new ItemNewCustomSeeds(BidsCrops.BROADBEANS)
            .setUnlocalizedName("Seeds Broad Bean");

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
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.SaltWater");
        fishOilBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.FishOil");
        oilyFishWaterBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.OilyFishWater");
        goatMilkBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.GoatMilk");

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
        fishOilBowl = new ItemBowlFluid(new String[] { "PotteryBowl", "Bowl" })
            .setContainerItem(TFCItems.potteryBowl)
            .setUnlocalizedName("Bowl Fish Oil");

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
        goatMilkLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.GoatMilk");

        ceramicBucketBrine = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.Brine");
        ceramicBucketHoneyWater = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.HoneyWater");
        ceramicBucketMilkVinegar = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.MilkVinegar");
        ceramicBucketOilyFishWater = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.OilyFishWater");
        ceramicBucketGoatMilk = new ItemBucketFluid(true)
            .setContainerItem((TFCItems.clayBucketEmpty))
            .setUnlocalizedName("Ceramic Bucket.GoatMilk");

        woodenBucketBrine = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.Brine");
        woodenBucketHoneyWater = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.HoneyWater");
        woodenBucketMilkVinegar = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.MilkVinegar");
        woodenBucketOilyFishWater = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.OilyFishWater");
        woodenBucketGoatMilk = new ItemBucketFluid(false)
            .setContainerItem((TFCItems.woodenBucketEmpty))
            .setUnlocalizedName("Wooden Bucket.GoatMilk");

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

        steamingMeshCloth = new ItemSteamingMeshCloth()
            .setMaxDamage(TFCItems.linenUses)
            .setUnlocalizedName("Steaming Mesh Cloth");

        stuffedPepper = new ItemMoreSandwich(new float[] { 3, 6, 4, 2, 1 })
            .setMetaNames(new String[]{"Stuffed Pepper.Green", "Stuffed Pepper.Yellow", "Stuffed Pepper.Red"})
            .setUnlocalizedName("Stuffed Pepper");
        stuffedMushroom = new ItemMoreSandwich(new float[] { 2, 3, 2, 2, 1 })
            .setMetaNames(new String[]{"Stuffed Mushroom.Brown"})
            .setUnlocalizedName("Stuffed Mushroom");
        pemmican = new ItemPemmican(new float[] { 0, 20, 10, 5, 5 })
            .setMetaNames(new String[]{"Pemmican"})
            .setUnlocalizedName("Pemmican");
    }

    private static void registerFluidContainers() {
        FluidHelper.registerPartialFluidContainer(TFCFluids.OLIVEOIL, TFCItems.glassBottle, 0, oliveOilBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.VINEGAR, TFCItems.glassBottle, 0, vinegarBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.BRINE, TFCItems.glassBottle, 0, brineBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.HONEY, TFCItems.glassBottle, 0, honeyBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.SALTWATER, TFCItems.glassBottle, 0, saltWaterBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.FISHOIL, TFCItems.glassBottle, 0, fishOilBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.OILYFISHWATER, TFCItems.glassBottle, 0, oilyFishWaterBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.GOATMILK, TFCItems.glassBottle, 0, goatMilkBottle, 50, 1000);

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
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.FISHOIL, 250),
            new ItemStack(fishOilBowl, 1, 0), new ItemStack(TFCItems.potteryBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.FISHOIL, 250),
            new ItemStack(fishOilBowl, 1, 1), new ItemStack(TFCItems.potteryBowl, 1, 2));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, 1000),
            new ItemStack(ceramicBucketBrine), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEYWATER, 1000),
            new ItemStack(ceramicBucketHoneyWater), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILKVINEGAR, 1000),
            new ItemStack(ceramicBucketMilkVinegar), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.OILYFISHWATER, 1000),
            new ItemStack(ceramicBucketOilyFishWater), new ItemStack(TFCItems.clayBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.GOATMILK, 1000),
            new ItemStack(ceramicBucketGoatMilk), new ItemStack(TFCItems.clayBucketEmpty));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, 1000),
            new ItemStack(woodenBucketBrine), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HONEYWATER, 1000),
            new ItemStack(woodenBucketHoneyWater), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILKVINEGAR, 1000),
            new ItemStack(woodenBucketMilkVinegar), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.OILYFISHWATER, 1000),
            new ItemStack(woodenBucketOilyFishWater), new ItemStack(TFCItems.woodenBucketEmpty));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.GOATMILK, 1000),
            new ItemStack(woodenBucketGoatMilk), new ItemStack(TFCItems.woodenBucketEmpty));

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
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.GOATMILK, 500),
            new ItemStack(goatMilkLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
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

        final Item[] drills = new Item[]{sedStoneDrill, mMStoneDrill, igInStoneDrill, igExStoneDrill,
            copperDrill, bronzeDrill, bismuthBronzeDrill, blackBronzeDrill};
        for (int i = 0; i < drills.length; i++) {
            OreDictionary.registerOre("itemDrill", new ItemStack(drills[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemDrillStone", new ItemStack(drills[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemDrillMetal", new ItemStack(drills[i], 1, WILD));
            }
        }

        final Item[] adzes = new Item[]{sedStoneAdze, mMStoneAdze, igInStoneAdze, igExStoneAdze,
            copperAdze, bronzeAdze, bismuthBronzeAdze, blackBronzeAdze};
        for (int i = 0; i < adzes.length; i++) {
            OreDictionary.registerOre("itemAdze", new ItemStack(adzes[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemAdzeStone", new ItemStack(adzes[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemAdzeMetal", new ItemStack(adzes[i], 1, WILD));
            }
        }

        final Item[] logs = new Item[]{logsSeasoned, peeledLog, peeledLogSeasoned};
        for (Item log : logs) {
            OreDictionary.registerOre("itemLogExtra", new ItemStack(log, 1, WILD));
        }

        // Proper steaming mesh
        for (Item item : new Item[]{ BidsItems.steamingMeshCloth }) {
            OreDictionary.registerOre("itemCookingPotAccessory", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("itemCookingPotAccessorySteamingMesh", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
        }

        // Items that are automatically consumed as a vessel from the cooking prep storage slots
        OreDictionary.registerOre("itemCookingPrepVessel",  new ItemStack(TFCItems.potteryBowl, 1, 1));
        OreDictionary.registerOre("itemCookingPrepVessel",  new ItemStack(TFCItems.potteryBowl, 1, 2));
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
                cornmealPorridge, appleCrushed, oliveCrushed, goatCheese,
                stuffedPepper, stuffedMushroom, pemmican,
                seaBeet, beetroot, sugarBeet, wildBeans, broadBeans,
                suet, tallow };

        for (Item item : foodItems) {
            MinecraftForgeClient.registerItemRenderer(item, new FoodItemRenderer());
        }
    }

    private static void registerPartialMolds() {
        Bids.LOG.info("Register items as TFC molds");

        Global.GLASS.addValidPartialMold(metalBlowpipe, 2, metalBlowpipe, 1, 2);
        Global.GLASS.addValidPartialMold(brassBlowpipe, 2, brassBlowpipe, 1, 2);

        Global.COPPER.addValidPartialMold(clayMoldAdze, 2, clayMoldAdze, 4, 2);
        Global.BRONZE.addValidPartialMold(clayMoldAdze, 3, clayMoldAdze, 4, 2);
        Global.BISMUTHBRONZE.addValidPartialMold(clayMoldAdze, 4, clayMoldAdze, 4, 2);
        Global.BLACKBRONZE.addValidPartialMold(clayMoldAdze, 5, clayMoldAdze, 4, 2);

        Global.COPPER.addValidPartialMold(clayMoldDrill, 2, clayMoldDrill, 4, 2);
        Global.BRONZE.addValidPartialMold(clayMoldDrill, 3, clayMoldDrill, 4, 2);
        Global.BISMUTHBRONZE.addValidPartialMold(clayMoldDrill, 4, clayMoldDrill, 4, 2);
        Global.BLACKBRONZE.addValidPartialMold(clayMoldDrill, 5, clayMoldDrill, 4, 2);
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

        registerHeatUnfinishedAnvilHelper(reg, 1, TFCItems.copperUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 2, TFCItems.bronzeUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 3, TFCItems.wroughtIronUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 4, TFCItems.steelUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 5, TFCItems.blackSteelUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 6, TFCItems.redSteelUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 7, TFCItems.blueSteelUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 8, TFCItems.roseGoldUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 9, TFCItems.bismuthUnshaped);
        registerHeatUnfinishedAnvilHelper(reg, 10, TFCItems.blackBronzeUnshaped);

        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(wheatCrushed, 1), 1, 88, null, new ItemStack(wheatPorridge, 1), null).setKeepNBT(true));
        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(barleyCrushed, 1), 1, 88, null, new ItemStack(barleyPorridge, 1), null).setKeepNBT(true));
        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(oatCrushed, 1), 1, 88, null, new ItemStack(oatPorridge, 1), null).setKeepNBT(true));
        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(ryeCrushed, 1), 1, 88, null, new ItemStack(ryePorridge, 1), null).setKeepNBT(true));
        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(riceCrushed, 1), 1, 88, null, new ItemStack(ricePorridge, 1), null).setKeepNBT(true));
        reg.addIndex(new BidsFoodHeatIndex(new ItemStack(cornmealCrushed, 1), 1, 88, null, new ItemStack(cornmealPorridge, 1), null).setKeepNBT(true));

        reg.addIndex(new HeatIndex(new ItemStack(wheatPorridge, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(barleyPorridge, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(oatPorridge, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(ryePorridge, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(ricePorridge, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(cornmealPorridge, 1), 1, 177, null));

        reg.addIndex(new HeatIndex(new ItemStack(seaBeet, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(beetroot, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(sugarBeet, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(wildBeans, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(broadBeans, 1), 1, 177, null));
    }

    private static void registerHeatUnfinishedAnvilHelper(HeatRegistry reg, int mat, Item unshaped) {
        HeatIndex hi = reg.findMatchingIndex(new ItemStack(unshaped, 1));
        if (hi != null) {
            HeatRaw raw = new HeatRaw(hi.specificHeat, hi.meltTemp);
            for (int j = 0; j < BlockUnfinishedAnvil.NUM_STAGES; j++) {
                reg.addIndex(new HeatIndex(BlockUnfinishedAnvil.getUnfinishedAnvil(mat, j), raw, new ItemStack(unshaped, 2 * j + 4, 0)));
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

        GameRegistry.registerItem(clayMoldAdze, clayMoldAdze.getUnlocalizedName());
        GameRegistry.registerItem(copperAdzeHead, copperAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(bronzeAdzeHead, bronzeAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(bismuthBronzeAdzeHead, bismuthBronzeAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(blackBronzeAdzeHead, blackBronzeAdzeHead.getUnlocalizedName());

        GameRegistry.registerItem(copperAdze, copperAdze.getUnlocalizedName());
        GameRegistry.registerItem(bronzeAdze, bronzeAdze.getUnlocalizedName());
        GameRegistry.registerItem(bismuthBronzeAdze, bismuthBronzeAdze.getUnlocalizedName());
        GameRegistry.registerItem(blackBronzeAdze, blackBronzeAdze.getUnlocalizedName());

        GameRegistry.registerItem(clayMoldDrill, clayMoldDrill.getUnlocalizedName());
        GameRegistry.registerItem(copperDrillHead, copperDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(bronzeDrillHead, bronzeDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(bismuthBronzeDrillHead, bismuthBronzeDrillHead.getUnlocalizedName());
        GameRegistry.registerItem(blackBronzeDrillHead, blackBronzeDrillHead.getUnlocalizedName());

        GameRegistry.registerItem(copperDrill, copperDrill.getUnlocalizedName());
        GameRegistry.registerItem(bronzeDrill, bronzeDrill.getUnlocalizedName());
        GameRegistry.registerItem(bismuthBronzeDrill, bismuthBronzeDrill.getUnlocalizedName());
        GameRegistry.registerItem(blackBronzeDrill, blackBronzeDrill.getUnlocalizedName());

        GameRegistry.registerItem(coatBodyFrontLeather, coatBodyFrontLeather.getUnlocalizedName());
        GameRegistry.registerItem(coatBodyBackLeather, coatBodyBackLeather.getUnlocalizedName());
        GameRegistry.registerItem(leatherCoat, leatherCoat.getUnlocalizedName());

        GameRegistry.registerItem(fishOilBottle, fishOilBottle.getUnlocalizedName());
        GameRegistry.registerItem(fishOilBowl, fishOilBowl.getUnlocalizedName());
        GameRegistry.registerItem(oilyFishWaterBottle, oilyFishWaterBottle.getUnlocalizedName());
        GameRegistry.registerItem(ceramicBucketOilyFishWater, ceramicBucketOilyFishWater.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketOilyFishWater, woodenBucketOilyFishWater.getUnlocalizedName());

        GameRegistry.registerItem(steamingMeshCloth, steamingMeshCloth.getUnlocalizedName());

        GameRegistry.registerItem(goatMilkBottle, goatMilkBottle.getUnlocalizedName());
        GameRegistry.registerItem(goatMilkLargeBowl, goatMilkLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(ceramicBucketGoatMilk, ceramicBucketGoatMilk.getUnlocalizedName());
        GameRegistry.registerItem(woodenBucketGoatMilk, woodenBucketGoatMilk.getUnlocalizedName());

        GameRegistry.registerItem(goatCheese, goatCheese.getUnlocalizedName());

        GameRegistry.registerItem(stuffedPepper, stuffedPepper.getUnlocalizedName());
        GameRegistry.registerItem(stuffedMushroom, stuffedMushroom.getUnlocalizedName());

        GameRegistry.registerItem(seaBeet, seaBeet.getUnlocalizedName());
        GameRegistry.registerItem(beetroot, beetroot.getUnlocalizedName());
        GameRegistry.registerItem(sugarBeet, sugarBeet.getUnlocalizedName());
        GameRegistry.registerItem(wildBeans, wildBeans.getUnlocalizedName());
        GameRegistry.registerItem(broadBeans, broadBeans.getUnlocalizedName());

        GameRegistry.registerItem(seedsSeaBeet, seedsSeaBeet.getUnlocalizedName());
        GameRegistry.registerItem(seedsBeetroot, seedsBeetroot.getUnlocalizedName());
        GameRegistry.registerItem(seedsSugarBeet, seedsSugarBeet.getUnlocalizedName());
        GameRegistry.registerItem(seedsWildBeans, seedsWildBeans.getUnlocalizedName());
        GameRegistry.registerItem(seedsBroadBeans, seedsBroadBeans.getUnlocalizedName());

        GameRegistry.registerItem(suet, suet.getUnlocalizedName());
        GameRegistry.registerItem(tallow, tallow.getUnlocalizedName());

        GameRegistry.registerItem(pemmican, pemmican.getUnlocalizedName());
    }

}
