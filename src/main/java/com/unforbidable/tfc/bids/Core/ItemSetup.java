package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemClothing;
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
import com.unforbidable.tfc.bids.Render.Item.FoodItemRenderer;
import com.unforbidable.tfc.bids.Render.Item.SeasonableItemRenderer;
import com.unforbidable.tfc.bids.Render.Item.SeasonedItemRenderer;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsConstants.ExtraClothing;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api.Crafting.CookingManager;
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
        registerCookingIngredientOverrides();
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

        igInHandAxe = new ItemHandAxe(TFCItems.igInToolMaterial)
            .setUnlocalizedName("IgIn Hand Axe");
        sedHandAxe = new ItemHandAxe(TFCItems.sedToolMaterial)
            .setUnlocalizedName("Sed Hand Axe");
        igExHandAxe = new ItemHandAxe(TFCItems.igExToolMaterial)
            .setUnlocalizedName("IgEx Hand Axe");
        mMHandAxe = new ItemHandAxe(TFCItems.mMToolMaterial)
            .setUnlocalizedName("MM Hand Axe");

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
        wroughtIronAdzeHead = new ItemGenericToolHead()
            .setUnlocalizedName("Wrought Iron Adze Head");

        copperDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Copper Drill Head");
        bronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bronze Drill Head");
        bismuthBronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Bismuth Bronze Drill Head");
        blackBronzeDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Black Bronze Drill Head");
        wroughtIronDrillHead = new ItemGenericToolHead()
            .setUnlocalizedName("Wrought Iron Drill Head");

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
        wroughtIronAdze = new ItemAdze(TFCItems.ironToolMaterial)
            .setEquipmentTier(3)
            .setUnlocalizedName("Wrought Iron Adze");

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
        wroughtIronDrill = new ItemDrill(TFCItems.ironToolMaterial)
            .setEquipmentTier(3)
            .setDurationMultiplier(0.25f)
            .setUnlocalizedName("Wrought Iron Drill");

        // Obsolete - replaced with roughStoneBrick
        sedRoughStoneLooseBrick = new ItemRoughBrick().setNames(Global.STONE_SED)
                .setMetaOnly() // No actual items
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Sed Rough Stone Loose Brick");

        roughStoneBrick = new ItemRoughBrick().setNames(Global.STONE_ALL)
                .setTextureName("Rough Brick")
                .setUnlocalizedName("Rough Stone Brick");

        roughStoneTile = new ItemRoughBrick().setNames(Global.STONE_ALL)
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
                .setIngredientOverride(TFCItems.wheatWhole)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Wheat Crushed");
        barleyCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20, false, false)
                .setIngredientOverride(TFCItems.barleyWhole)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Barley Crushed");
        oatCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20, false, false)
                .setIngredientOverride(TFCItems.oatWhole)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Oat Crushed");
        ryeCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20, false, false)
                .setIngredientOverride(TFCItems.ryeWhole)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Rye Crushed");
        riceCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20, false, false)
                .setIngredientOverride(TFCItems.riceWhole)
                .setDecayRate(1.5f)
                .setUnlocalizedName("Rice Crushed");
        cornmealCrushed = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20, false, false)
                .setIngredientOverride(TFCItems.maizeEar)
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

        wheatDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setFlatDoughDamage(0)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Wheat Dough Unshaped");
        barleyDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
            .setFlatDoughDamage(1)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Barley Dough Unshaped");
        oatDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setFlatDoughDamage(2)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Oat Dough Unshaped");
        ryeDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
            .setFlatDoughDamage(3)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rye Dough Unshaped");
        riceDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setFlatDoughDamage(4)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rice Dough Unshaped");
        cornmealDoughUnshaped = new ItemUnshapedDough(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
            .setFlatDoughDamage(5)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Cornmeal Dough Unshaped");

        wheatDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Wheat Dough Hardtack");
        barleyDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Barley Dough Hardtack");
        oatDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Oat Dough Hardtack");
        ryeDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rye Dough Hardtack");
        riceDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rice Dough Hardtack");
        cornmealDoughHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Cornmeal Dough Hardtack");

        wheatHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Wheat Hardtack");
        barleyHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Barley Hardtack");
        oatHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Oat Hardtack");
        ryeHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Rye Hardtack");
        riceHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Rice Hardtack");
        cornmealHardtack = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
            .setDecayRate(0.02f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Cornmeal Hardtack");

        wheatDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Wheat Dough Flatbread");
        barleyDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Barley Dough Flatbread");
        oatDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Oat Dough Flatbread");
        ryeDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rye Dough Flatbread");
        riceDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Rice Dough Flatbread");
        cornmealDoughFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
            .setWaterPercentage(0.7f)
            .setUnlocalizedName("Cornmeal Dough Flatbread");

        wheatFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Wheat Flatbread");
        barleyFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Barley Flatbread");
        oatFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Oat Flatbread");
        ryeFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Rye Flatbread");
        riceFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Rice Flatbread");
        cornmealFlatbread = new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Cornmeal Flatbread");

        flatDough = new ItemGenericFlat().setTextureFolder("food")
            .setUnlocalizedName("Flat Dough");

        goatCheese = new ItemExtraFood(EnumFoodGroup.Dairy, 0, 35, 20, 0, 20)
            .setWaterPercentage(0.6f)
            .setDecayRate(0.5f)
            .setCanSmoke()
            .setUnlocalizedName("Goat Cheese");
        ((ItemFoodTFC) goatCheese).setSmokeAbsorbMultiplier(1F);
        hardCheese = new ItemExtraFood(EnumFoodGroup.Dairy, 0, 35, 20, 0, 20)
            .setWaterPercentage(0.3f)
            .setDecayRate(0.25f)
            .setCanSmoke()
            .setUnlocalizedName("Hard Cheese");
        ((ItemFoodTFC) hardCheese).setSmokeAbsorbMultiplier(0.5F);

        suet = new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 30)
            .setDecayRate(2.5f)
            .setPoisonOnRaw(true)
            .setUnlocalizedName("Suet");
        tallow = new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 60)
            .setDecayRate(0.05f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Tallow");

        butter = new ItemExtraFood(EnumFoodGroup.Dairy, 35, 0, 10, 0, 50)
            .setDecayRate(1f)
            .setNutritionAsIfCooked(true)
            .setUnlocalizedName("Butter");

        bambooShoot = new ItemExtraFood(EnumFoodGroup.Vegetable, 20, 0, 0, 0, 10, true, true, true, true)
            .setWaterPercentage(0.3f)
            .setDecayRate(1.8f)
            .setHasCookedIcon()
            .setUnlocalizedName("Bamboo Shoot");

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

        seedsNewBarley = new ItemNewCustomSeeds(BidsCrops.BARLEY)
            .setUnlocalizedName("Seeds New Barley");
        seedsNewOat = new ItemNewCustomSeeds(BidsCrops.OAT)
            .setUnlocalizedName("Seeds New Oat");
        seedsNewRye = new ItemNewCustomSeeds(BidsCrops.RYE)
            .setUnlocalizedName("Seeds New Rye");
        seedsNewWheat = new ItemNewCustomSeeds(BidsCrops.WHEAT)
            .setUnlocalizedName("Seeds New Wheat");

        seedsWinterBarley = new ItemNewCustomSeeds(BidsCrops.WINTERBARLEY)
            .setUnlocalizedName("Seeds Winter Barley");
        seedsWinterOat = new ItemNewCustomSeeds(BidsCrops.WINTEROAT)
            .setUnlocalizedName("Seeds Winter Oat");
        seedsWinterRye = new ItemNewCustomSeeds(BidsCrops.WINTERRYE)
            .setUnlocalizedName("Seeds Winter Rye");
        seedsWinterWheat = new ItemNewCustomSeeds(BidsCrops.WINTERWHEAT)
            .setUnlocalizedName("Seeds Winter Wheat");

        seedsNewOnion = new ItemNewCustomSeeds(BidsCrops.ONION)
            .setUnlocalizedName("Seeds New Onion");
        seedsNewCabbage = new ItemNewCustomSeeds(BidsCrops.CABBAGE)
            .setUnlocalizedName("Seeds New Cabbage");
        seedsNewGarlic = new ItemNewCustomSeeds(BidsCrops.GARLIC)
            .setUnlocalizedName("Seeds New Garlic");
        seedsNewCarrot = new ItemNewCustomSeeds(BidsCrops.CARROT)
            .setUnlocalizedName("Seeds New Carrot");

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
        creamBottle = new ItemGlassBottleFluid()
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.Cream");

        waterskinCream = new ItemWaterskinChurn()
            .setContainerItem(TFCItems.waterskinEmpty)
            .setMaxDamage(2000 / 50)
            .setUnlocalizedName("Waterskin.Cream");

        goatMilkBottle = new ItemGenericDrink(1000, false, 0, 20, 40, 60, 80, 100)
            .setCanDrinkInParts(true)
            .setFoodGroup(EnumFoodGroup.Dairy)
            .setCalories(0.642f)
            .setWaterRestoreRatio(1f)
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.GoatMilk");
        skimmedMilkBottle = new ItemGenericDrink(1000, false, 0, 20, 40, 60, 80, 100)
            .setCanDrinkInParts(true)
            .setFoodGroup(EnumFoodGroup.Dairy)
            .setCalories(0.321f)
            .setWaterRestoreRatio(1f)
            .setContainerItem((TFCItems.glassBottle))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Glass Bottle.SkimmedMilk");

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
        skimmedMilkLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.SkimmedMilk");
        creamLargeBowl = new ItemLargeBowlFluid(new String[] { "Pottery" })
            .setContainerItem(BidsItems.largeClayBowl)
            .setUnlocalizedName("Large Bowl.Cream");

        woodenPailEmpty = new ItemPailEmpty()
            .setUnlocalizedName("Wooden Pail");

        woodenPailFreshWater = new ItemPailFluid(4000)
            .setContainerItem(woodenPailEmpty)
            .setUnlocalizedName("Wooden Pail.FreshWater");
        woodenPailMilk = new ItemPailFluid(4000)
            .setContainerItem(woodenPailEmpty)
            .setUnlocalizedName("Wooden Pail.Milk");
        woodenPailGoatMilk = new ItemPailFluid(4000)
            .setContainerItem(woodenPailEmpty)
            .setUnlocalizedName("Wooden Pail.GoatMilk");

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
        pemmican = new ItemPemmican(new float[] { 0, 40, 20, 10, 10 })
            .setMetaNames(new String[]{"Pemmican"})
            .setUnlocalizedName("Pemmican");
        wrap = new ItemWrap(new float[] { 3, 6, 4, 2, 1 })
            .setMetaNames(new String[]{"Wrap.Wheat", "Wrap.Barley", "Wrap.Oat", "Wrap.Rye", "Wrap.Corn", "Wrap.Rice" })
            .setUnlocalizedName("Wrap");

        cookingMixture = new ItemCookingMixture()
            .setUnlocalizedName("CookingMixture");

        stew = new ItemCookedMeal()
            .setMetaNames(new String[] {"Stew.Bean", "Stew.Meat", "Stew.Fish", "Stew.Vegetable"})
            .setUnlocalizedName("Stew");
        soup = new ItemCookedMeal()
            .setMetaNames(new String[] {"Soup.Bean", "Soup.Meat", "Soup.Fish", "Soup.Vegetable"})
            .setUnlocalizedName("Soup");
        porridge = new ItemCookedMeal()
            .setMetaNames(new String[] {"Porridge.Water", "Porridge.Milk"})
            .setUnlocalizedName("Porridge");
        omelet = new ItemCookedMeal()
            .setUnlocalizedName("Omelet");

        moreHide = new ItemMoreRawhide()
            .setUnlocalizedName("More Hide");

        potteryJugVinegar = new ItemPotteryFluid(1000)
            .setContainerItem(TFCItems.potteryJug)
            .setUnlocalizedName("Pottery Jug.Vinegar");
        potteryJugOliveOil = new ItemPotteryFluid(1000)
            .setContainerItem(TFCItems.potteryJug)
            .setUnlocalizedName("Pottery Jug.OliveOil");
        potteryJugFishOil = new ItemPotteryFluid(1000)
            .setContainerItem(TFCItems.potteryJug)
            .setUnlocalizedName("Pottery Jug.FishOil");

        potteryJugGoatMilk = new ItemGenericDrink(1000, true, 0, 20, 40, 60, 80, 100)
            .setCanDrinkInParts(true)
            .setFoodGroup(EnumFoodGroup.Dairy)
            .setCalories(0.642f)
            .setWaterRestoreRatio(1f)
            .setContainerItem((TFCItems.potteryJug))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Pottery Jug.GoatMilk");
        potteryJugSkimmedMilk = new ItemGenericDrink(1000, true, 0, 20, 40, 60, 80, 100)
            .setCanDrinkInParts(true)
            .setFoodGroup(EnumFoodGroup.Dairy)
            .setCalories(0.321f)
            .setWaterRestoreRatio(1f)
            .setContainerItem((TFCItems.potteryJug))
            .setMaxDamage(1000 / 50)
            .setUnlocalizedName("Pottery Jug.SkimmedMilk");

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
        FluidHelper.registerPartialFluidContainer(BidsFluids.SKIMMEDMILK, TFCItems.glassBottle, 0, skimmedMilkBottle, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.CREAM, TFCItems.glassBottle, 0, creamBottle, 50, 1000);

        FluidHelper.registerPartialFluidContainer(BidsFluids.CREAM, TFCItems.waterskinEmpty, 0, waterskinCream, 50, 2000);

        FluidHelper.registerPartialFluidContainer(TFCFluids.VINEGAR, TFCItems.potteryJug, 1, potteryJugVinegar, 50, 1000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.OLIVEOIL, TFCItems.potteryJug, 1, potteryJugOliveOil, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.FISHOIL, TFCItems.potteryJug, 1, potteryJugFishOil, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.GOATMILK, TFCItems.potteryJug, 1, potteryJugGoatMilk, 50, 1000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.SKIMMEDMILK, TFCItems.potteryJug, 1, potteryJugSkimmedMilk, 50, 1000);

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
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.SKIMMEDMILK, 500),
            new ItemStack(skimmedMilkLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BidsFluids.CREAM, 500),
            new ItemStack(creamLargeBowl, 1, 0), new ItemStack(largeClayBowl, 1, 1));

        FluidHelper.registerPartialFluidContainer(TFCFluids.FRESHWATER, woodenPailEmpty, 0, woodenPailFreshWater, 50, 4000);
        FluidHelper.registerPartialFluidContainer(TFCFluids.MILK, woodenPailEmpty, 0, woodenPailMilk, 50, 4000);
        FluidHelper.registerPartialFluidContainer(BidsFluids.GOATMILK, woodenPailEmpty, 0, woodenPailGoatMilk, 50, 4000);
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
        wroughtIronAdze.setHarvestLevel("shovel", 1);

        sedStoneAdze.setHarvestLevel("axe", 1);
        mMStoneAdze.setHarvestLevel("axe", 1);
        igInStoneAdze.setHarvestLevel("axe", 1);
        igExStoneAdze.setHarvestLevel("axe", 1);
        copperAdze.setHarvestLevel("axe", 1);
        bronzeAdze.setHarvestLevel("axe", 1);
        bismuthBronzeAdze.setHarvestLevel("axe", 1);
        blackBronzeAdze.setHarvestLevel("axe", 1);
        wroughtIronAdze.setHarvestLevel("axe", 1);
    }

    private static void registerOre() {
        Bids.LOG.info("Register item ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        final Item[] drills = new Item[]{sedStoneDrill, mMStoneDrill, igInStoneDrill, igExStoneDrill,
            copperDrill, bronzeDrill, bismuthBronzeDrill, blackBronzeDrill, wroughtIronDrill};
        for (int i = 0; i < drills.length; i++) {
            OreDictionary.registerOre("itemDrill", new ItemStack(drills[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemDrillStone", new ItemStack(drills[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemDrillMetal", new ItemStack(drills[i], 1, WILD));
            }
        }

        final Item[] adzes = new Item[]{sedStoneAdze, mMStoneAdze, igInStoneAdze, igExStoneAdze,
            copperAdze, bronzeAdze, bismuthBronzeAdze, blackBronzeAdze, wroughtIronAdze};
        for (int i = 0; i < adzes.length; i++) {
            OreDictionary.registerOre("itemAdze", new ItemStack(adzes[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemAdzeStone", new ItemStack(adzes[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemAdzeMetal", new ItemStack(adzes[i], 1, WILD));
            }
        }

        final Item[] handAxes = new Item[] { sedHandAxe, mMHandAxe, igInHandAxe, igExHandAxe };
        for (int i = 0; i < handAxes.length; i++) {
            OreDictionary.registerOre("itemHandAxe", new ItemStack(handAxes[i], 1, WILD));

            // Registering "itemKnifeStone" allow straw harvestable with a hand axe
            // but no usable in recipes where "itemKnife" is used
            OreDictionary.registerOre("itemKnifeStone", new ItemStack(handAxes[i], 1, WILD));

            // Registering "itemAxeStone" allow bushes (and trees) harvestable with a hand axe
            // but no usable in recipes where "itemAxe" is used
            OreDictionary.registerOre("itemAxeStone", new ItemStack(handAxes[i], 1, WILD));
        }

        for (int i = 0; i < 6; i++) {
            OreDictionary.registerOre("itemPlugAndFeather", new ItemStack(BidsItems.plugAndFeather, 1, i));
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

        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsBeetroot));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsSugarBeet));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterBarley));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterOat));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterRye));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterWheat));

        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterBarley));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterOat));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterRye));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterWheat));

        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(TFCItems.clayBucketEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(TFCItems.woodenBucketEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.largeClayBowl, 1, 1));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailMilk, 1, WILD));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailGoatMilk, 1, WILD));
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

        if (BidsOptions.Firepit.allowFuelUnseasonedFirewood) {
            FirepitRegistry.registerFuel(firewood);
        }
    }

    private static void registerCookingIngredientOverrides() {
        Bids.LOG.info("Register cooking ingredient overrides");

        CookingManager.registerCookingIngredientOverride(TFCItems.barleyGrain, TFCItems.barleyWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.oatGrain, TFCItems.oatWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.ryeGrain, TFCItems.ryeWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.wheatGrain, TFCItems.wheatWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.riceGrain, TFCItems.riceWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.barleyGround, TFCItems.barleyWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.oatGround, TFCItems.oatWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.ryeGround, TFCItems.ryeWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.wheatGround, TFCItems.wheatWhole);
        CookingManager.registerCookingIngredientOverride(TFCItems.riceGround, TFCItems.riceWhole);
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

        Item[] foodItems = {
            wheatCrushed, barleyCrushed, oatCrushed, riceCrushed, ryeCrushed, cornmealCrushed,
            wheatPorridge, barleyPorridge, oatPorridge, ricePorridge, ryePorridge, cornmealPorridge,
            wheatDoughUnshaped, barleyDoughUnshaped, oatDoughUnshaped, riceDoughUnshaped, ryeDoughUnshaped, cornmealDoughUnshaped,
            wheatDoughHardtack, barleyDoughHardtack, oatDoughHardtack, riceDoughHardtack, ryeDoughHardtack, cornmealDoughHardtack,
            wheatHardtack, barleyHardtack, oatHardtack, riceHardtack, ryeHardtack, cornmealHardtack,
            wheatDoughFlatbread, barleyDoughFlatbread, oatDoughFlatbread, riceDoughFlatbread, ryeDoughFlatbread, cornmealDoughFlatbread,
            wheatFlatbread, barleyFlatbread, oatFlatbread, riceFlatbread, ryeFlatbread, cornmealFlatbread,
            appleCrushed, oliveCrushed,
            goatCheese, hardCheese,
            butter,
            stuffedPepper, stuffedMushroom, pemmican, wrap,
            seaBeet, beetroot, sugarBeet, wildBeans, broadBeans,
            suet, tallow, bambooShoot,
            soup, stew, porridge, omelet
        };

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
        registerHeatUnfinishedAnvilHelper(reg, 9, TFCItems.bismuthBronzeUnshaped);
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

        // The melt temp needs to be less than the cooked temp, otherwise the cooked icon does not render properly
        reg.addIndex(new HeatIndex(new ItemStack(bambooShoot, 1), 1, 82, null));

        reg.addIndex(new HeatIndex(new ItemStack(wheatDoughHardtack, 1), 1, 88, new ItemStack(wheatHardtack, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(barleyDoughHardtack, 1), 1, 88, new ItemStack(barleyHardtack, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(oatDoughHardtack, 1), 1, 88, new ItemStack(oatHardtack, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(ryeDoughHardtack, 1), 1, 88, new ItemStack(ryeHardtack, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(riceDoughHardtack, 1), 1, 88, new ItemStack(riceHardtack, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(cornmealDoughHardtack, 1), 1, 88, new ItemStack(cornmealHardtack, 1)).setKeepNBT(true));

        reg.addIndex(new HeatIndex(new ItemStack(wheatHardtack, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(barleyHardtack, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(oatHardtack, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(ryeHardtack, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(riceHardtack, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(cornmealHardtack, 1), 1, 177, null));

        reg.addIndex(new HeatIndex(new ItemStack(wheatDoughFlatbread, 1), 1, 88, new ItemStack(wheatFlatbread, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(barleyDoughFlatbread, 1), 1, 88, new ItemStack(barleyFlatbread, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(oatDoughFlatbread, 1), 1, 88, new ItemStack(oatFlatbread, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(ryeDoughFlatbread, 1), 1, 88, new ItemStack(ryeFlatbread, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(riceDoughFlatbread, 1), 1, 88, new ItemStack(riceFlatbread, 1)).setKeepNBT(true));
        reg.addIndex(new HeatIndex(new ItemStack(cornmealDoughFlatbread, 1), 1, 88, new ItemStack(cornmealFlatbread, 1)).setKeepNBT(true));

        reg.addIndex(new HeatIndex(new ItemStack(wheatFlatbread, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(barleyFlatbread, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(oatFlatbread, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(ryeFlatbread, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(riceFlatbread, 1), 1, 177, null));
        reg.addIndex(new HeatIndex(new ItemStack(cornmealFlatbread, 1), 1, 177, null));
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

        GameRegistry.registerItem(wroughtIronAdze, wroughtIronAdze.getUnlocalizedName());
        GameRegistry.registerItem(wroughtIronAdzeHead, wroughtIronAdzeHead.getUnlocalizedName());
        GameRegistry.registerItem(wroughtIronDrill, wroughtIronDrill.getUnlocalizedName());
        GameRegistry.registerItem(wroughtIronDrillHead, wroughtIronDrillHead.getUnlocalizedName());

        GameRegistry.registerItem(seedsNewBarley, seedsNewBarley.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewOat, seedsNewOat.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewRye, seedsNewRye.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewWheat, seedsNewWheat.getUnlocalizedName());
        GameRegistry.registerItem(seedsWinterBarley, seedsWinterBarley.getUnlocalizedName());
        GameRegistry.registerItem(seedsWinterOat, seedsWinterOat.getUnlocalizedName());
        GameRegistry.registerItem(seedsWinterRye, seedsWinterRye.getUnlocalizedName());
        GameRegistry.registerItem(seedsWinterWheat, seedsWinterWheat.getUnlocalizedName());

        GameRegistry.registerItem(seedsNewOnion, seedsNewOnion.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewCabbage, seedsNewCabbage.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewGarlic, seedsNewGarlic.getUnlocalizedName());
        GameRegistry.registerItem(seedsNewCarrot, seedsNewCarrot.getUnlocalizedName());

        GameRegistry.registerItem(cookingMixture, cookingMixture.getUnlocalizedName());
        GameRegistry.registerItem(stew, stew.getUnlocalizedName());
        GameRegistry.registerItem(soup, soup.getUnlocalizedName());
        GameRegistry.registerItem(porridge, porridge.getUnlocalizedName());

        GameRegistry.registerItem(bambooShoot, bambooShoot.getUnlocalizedName());

        GameRegistry.registerItem(omelet, omelet.getUnlocalizedName());

        GameRegistry.registerItem(wheatDoughUnshaped, wheatDoughUnshaped.getUnlocalizedName());
        GameRegistry.registerItem(barleyDoughUnshaped, barleyDoughUnshaped.getUnlocalizedName());
        GameRegistry.registerItem(oatDoughUnshaped, oatDoughUnshaped.getUnlocalizedName());
        GameRegistry.registerItem(ryeDoughUnshaped, ryeDoughUnshaped.getUnlocalizedName());
        GameRegistry.registerItem(riceDoughUnshaped, riceDoughUnshaped.getUnlocalizedName());
        GameRegistry.registerItem(cornmealDoughUnshaped, cornmealDoughUnshaped.getUnlocalizedName());

        GameRegistry.registerItem(wheatDoughHardtack, wheatDoughHardtack.getUnlocalizedName());
        GameRegistry.registerItem(barleyDoughHardtack, barleyDoughHardtack.getUnlocalizedName());
        GameRegistry.registerItem(oatDoughHardtack, oatDoughHardtack.getUnlocalizedName());
        GameRegistry.registerItem(ryeDoughHardtack, ryeDoughHardtack.getUnlocalizedName());
        GameRegistry.registerItem(riceDoughHardtack, riceDoughHardtack.getUnlocalizedName());
        GameRegistry.registerItem(cornmealDoughHardtack, cornmealDoughHardtack.getUnlocalizedName());

        GameRegistry.registerItem(wheatHardtack, wheatHardtack.getUnlocalizedName());
        GameRegistry.registerItem(barleyHardtack, barleyHardtack.getUnlocalizedName());
        GameRegistry.registerItem(oatHardtack, oatHardtack.getUnlocalizedName());
        GameRegistry.registerItem(ryeHardtack, ryeHardtack.getUnlocalizedName());
        GameRegistry.registerItem(riceHardtack, riceHardtack.getUnlocalizedName());
        GameRegistry.registerItem(cornmealHardtack, cornmealHardtack.getUnlocalizedName());

        GameRegistry.registerItem(wheatDoughFlatbread, wheatDoughFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(barleyDoughFlatbread, barleyDoughFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(oatDoughFlatbread, oatDoughFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(ryeDoughFlatbread, ryeDoughFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(riceDoughFlatbread, riceDoughFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(cornmealDoughFlatbread, cornmealDoughFlatbread.getUnlocalizedName());

        GameRegistry.registerItem(wheatFlatbread, wheatFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(barleyFlatbread, barleyFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(oatFlatbread, oatFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(ryeFlatbread, ryeFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(riceFlatbread, riceFlatbread.getUnlocalizedName());
        GameRegistry.registerItem(cornmealFlatbread, cornmealFlatbread.getUnlocalizedName());

        GameRegistry.registerItem(flatDough, flatDough.getUnlocalizedName());

        GameRegistry.registerItem(wrap, wrap.getUnlocalizedName());

        GameRegistry.registerItem(moreHide, moreHide.getUnlocalizedName());

        GameRegistry.registerItem(woodenPailEmpty, woodenPailEmpty.getUnlocalizedName());
        GameRegistry.registerItem(woodenPailFreshWater, woodenPailFreshWater.getUnlocalizedName());
        GameRegistry.registerItem(woodenPailMilk, woodenPailMilk.getUnlocalizedName());
        GameRegistry.registerItem(woodenPailGoatMilk, woodenPailGoatMilk.getUnlocalizedName());

        GameRegistry.registerItem(potteryJugVinegar, potteryJugVinegar.getUnlocalizedName());
        GameRegistry.registerItem(potteryJugOliveOil, potteryJugOliveOil.getUnlocalizedName());
        GameRegistry.registerItem(potteryJugFishOil, potteryJugFishOil.getUnlocalizedName());

        GameRegistry.registerItem(butter, butter.getUnlocalizedName());

        GameRegistry.registerItem(hardCheese, hardCheese.getUnlocalizedName());

        GameRegistry.registerItem(potteryJugGoatMilk, potteryJugGoatMilk.getUnlocalizedName());
        GameRegistry.registerItem(potteryJugSkimmedMilk, potteryJugSkimmedMilk.getUnlocalizedName());
        GameRegistry.registerItem(skimmedMilkBottle, skimmedMilkBottle.getUnlocalizedName());
        GameRegistry.registerItem(skimmedMilkLargeBowl, skimmedMilkLargeBowl.getUnlocalizedName());
        GameRegistry.registerItem(creamBottle, creamBottle.getUnlocalizedName());
        GameRegistry.registerItem(creamLargeBowl, creamLargeBowl.getUnlocalizedName());

        GameRegistry.registerItem(waterskinCream, waterskinCream.getUnlocalizedName());

        GameRegistry.registerItem(igInHandAxe, igInHandAxe.getUnlocalizedName());
        GameRegistry.registerItem(sedHandAxe, sedHandAxe.getUnlocalizedName());
        GameRegistry.registerItem(igExHandAxe, igExHandAxe.getUnlocalizedName());
        GameRegistry.registerItem(mMHandAxe, mMHandAxe.getUnlocalizedName());
    }

}
