package com.unforbidable.tfc.bids.api._obsolete;

import com.unforbidable.tfc.bids.api._obsolete.Crafting.*;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.*;
import com.unforbidable.tfc.bids.api._obsolete.Registry.*;
import com.unforbidable.tfc.bids.api._obsolete.Registry.Values.WetnessInfo;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IKilnChamber;
import com.unforbidable.tfc.bids.api.features.woodpile.SeasoningRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BidsRegistry {

    public static final ItemRegistry<WetnessInfo> DRYING_ITEM_WETNESS = new ItemRegistry<>();
    public static final ItemRegistry<IDryingItemRenderInfo> DRYING_ITEM_RENDER_INFO = new ItemRegistry<>();
    public static final ItemRegistry<IFirepitFuelMaterial> FIREPIT_FUEL = new ItemRegistry<>();
//    public static final ItemRegistry<IWoodPileRenderProvider> WOODPILE_RENDER_PROVIDERS = new ItemRegistry<>();
//    public static final ListRegistry<Crackable> WOODPILE_CRACKABLE_BLOCKS = new ListRegistry<>("woodpile-crackable-blocks");
    public static final ListRegistry<ISurfaceItemPlacer> SURFACE_ITEM_PLACERS = new ListRegistry<>("surface-item-placers");
//    public static final BlockRegistry<IQuarriable> QUARRY_BLOCKS = new BlockRegistry<>();
//    public static final ListRegistry<Carvable> CARVING_BLOCKS = new ListRegistry<>("carving-blocks");
    public static final ListRegistry<IDrinkable> DRINKS = new ListRegistry<>("drinks");
    public static final ListRegistry<Class<? extends IKilnChamber>> KILN_CHAMBERS = new ListRegistry<>("kiln-chambers");
    public static final FluidRegistry<ILampFuelMaterial> LAMP_FUEL = new FluidRegistry<>();
    public static final ItemRegistry<Item> COOKING_INGREDIENT_OVERRIDE = new ItemRegistry<>();
    public static final ListRegistry<CookingMixture> COOKING_MIXTURES = new ListRegistry<>("cooking-mixtures");
    public static final ListRegistry<DryingRackTyingEquipment> DRYING_RACK_TYING_EQUIPMENT = new ListRegistry<>("drying-rack-tying-equipment");

//    public static final RecipeRegistry<CarvingRecipe> CARVING_RECIPES = new RecipeRegistry<>("carving");
    public static final SimpleRecipeRegistry<ChurningRecipe, FluidStack> CHURNING_RECIPES = new SimpleRecipeRegistry<>("churning");
    public static final SimpleRecipeRegistry<CardingRecipe, ItemStack> CARDING_RECIPES = new SimpleRecipeRegistry<>("carding");
    public static final SimpleRecipeRegistry<HandworkRecipe, ItemStack> HANDWORK_RECIPES = new SimpleRecipeRegistry<>("handwork");
    public static final SimpleRecipeRegistry<HecklingRecipe, ItemStack> HECKLING_RECIPES = new SimpleRecipeRegistry<>("heckling");
    public static final SimpleRecipeRegistry<RopeMakingRecipe, ItemStack> ROPEMAKING_RECIPES = new SimpleRecipeRegistry<>("ropemaking");
    public static final SimpleRecipeRegistry<SpinningRecipe, ItemStack> SPINNING_RECIPES = new SimpleRecipeRegistry<>("spinning");
    public static final RecipeRegistry<ChoppingBlockRecipe> CHOPPING_BLOCK_RECIPES = new RecipeRegistry<>("chopping");
    public static final RecipeRegistry<CookingRecipe> COOKING_RECIPES = new RecipeRegistry<>("cooking");
    public static final SimpleRecipeRegistry<DryingRackRecipe, ItemStack> DRYING_RACK_RECIPES = new SimpleRecipeRegistry<>("drying-rack");
    public static final SimpleRecipeRegistry<DryingSurfaceRecipe, ItemStack> DRYING_SURFACE_RECIPES = new SimpleRecipeRegistry<>("drying-surface");
    public static final SimpleRecipeRegistry<PrepRecipe, ItemStack[]> PREP_RECIPES = new SimpleRecipeRegistry<>("prep");
    public static final RecipeRegistry<ProcessingSurfaceRecipe> PROCESSING_SURFACE_RECIPES = new RecipeRegistry<>("processing-surface");
    public static final RecipeRegistry<SoakingSurfaceRecipe> SOAKING_SURFACE_RECIPES = new RecipeRegistry<>("soaking-surface");
//    public static final SimpleRecipeRegistry<SeasoningRecipe, ItemStack> SEASONING_RECIPES = new SimpleRecipeRegistry<>("seasoning");
    public static final SimpleRecipeRegistry<SaddleQuernRecipe, ItemStack> SADDLE_QUERN_RECIPES = new SimpleRecipeRegistry<>("saddle-quern");
    public static final SimpleRecipeRegistry<StonePressRecipe, ItemStack> STONE_PRESS_RECIPES = new SimpleRecipeRegistry<>("stone-press");
    public static final SimpleRecipeRegistry<ScrewPressRecipe, ItemStack> SCREW_PRESS_RECIPES = new SimpleRecipeRegistry<>("screw-press");
    public static final SimpleRecipeRegistry<WoodworkingRecipe, ItemStack> WOODWORKING_RECIPES = new SimpleRecipeRegistry<>("woodworking");

}
