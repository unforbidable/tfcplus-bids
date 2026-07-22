package com.unforbidable.tfc.bids.features.food.tallow;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.food.tallow.eventhandler.SuetLivingDropsEventHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("tallow")
public class Tallow extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.TALLOW, FluidCommon::new)
            .color(0xf0db3a);

        init.item(ItemNames.SUET, () -> new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 30))
            .food(2.5f, true, false, true, false);
        init.item(ItemNames.TALLOW, () -> new ItemExtraFood(EnumFoodGroup.Protein, 0, 0, 0, 0, 60))
            .food(0.05f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.suet)
            .item(BidsItems.tallow);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new SuetLivingDropsEventHandler());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.suet), Global.FOOD_MAX_WEIGHT / 8000))
                .produces(new FluidStack(BidsFluids.tallow, 1))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inTime(4000 / 5000f)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.tallow, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
                .produces(new FluidStack(BidsFluids.tallow, 1))
                .withHeat()
                .inTime(250 / 5000f)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.tallow, 200), "materialString")
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .build());
    }

}
