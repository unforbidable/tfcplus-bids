package com.unforbidable.tfc.bids.features.food.crushedfruit;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.pressing.StonePressRecipe;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.saddlequern.SaddleQuernRegistry;
import com.unforbidable.tfc.bids.features.device.saddlequern.StonePressConfig;
import com.unforbidable.tfc.bids.features.device.saddlequern.StonePressRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.APPLE_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OLIVE_CRUSHED;

/**
 * <li>crushed apple and olive - intermediate material for pressing apples and olive in stone press</li>
 */
@FeatureName("crushedFruit")
public class CrushedFruit extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        // TODO add water % that of apple 0.86f and olive 0.6f or somewhat higher (FIX)
        init.item(APPLE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Fruit, 40, 20, 0, 10, 0))
            .food(4f);
        init.item(OLIVE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Fruit, 10, 0, 3, 50, 0))
            .food(4f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.appleCrushed)
            .item(BidsItems.oliveCrushed);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed), new ItemStack(TFCItems.greenApple)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed), new ItemStack(TFCItems.redApple)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.oliveCrushed), new ItemStack(TFCItems.olive)));

        float inputRatio = 1 / StonePressConfig.efficiency; // input multiplier (for non-food input)
        setup.registry(StonePressRegistry.recipes)
            .add(new StonePressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.oliveCrushed), 0.64f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.appleCrushed), 0.7f * inputRatio)));
    }

}
