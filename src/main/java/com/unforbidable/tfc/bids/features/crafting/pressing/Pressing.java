package com.unforbidable.tfc.bids.features.crafting.pressing;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.pressing.PressingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.pressing.nei.ScrewPressNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.pressing.nei.StonePressNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("pressing")
public class Pressing extends Feature {

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new StonePressNeiHandler())
            .handler(new ScrewPressNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(PressingRegistry.recipes)
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.olive), 0.64f),
                new FluidStack(TFCFluids.OLIVEOIL, 10), 1f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.redApple), 0.7f),
                new FluidStack(TFCFluids.APPLEJUICE, 10), 1f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.greenApple), 0.7f),
                new FluidStack(TFCFluids.APPLEJUICE, 10), 1f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f),
                new FluidStack(TFCFluids.GRAPEJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f),
                new FluidStack(TFCFluids.CANEJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f),
                new FluidStack(TFCFluids.LEMONJUICE, 10), 0.65f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f),
                new FluidStack(TFCFluids.ORANGEJUICE, 10), 0.65f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f),
                new FluidStack(TFCFluids.PEACHJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f),
                new FluidStack(TFCFluids.PLUMJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f),
                new FluidStack(TFCFluids.FIGJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f),
                new FluidStack(TFCFluids.CHERRYJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f),
                new FluidStack(TFCFluids.DATEJUICE, 6), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f),
                new FluidStack(TFCFluids.PAPAYAJUICE, 10), 0.8f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f),
                new FluidStack(TFCFluids.BERRYJUICE, 10), 0.5f))
            .add(new PressingRecipe(new ItemStack(TFCItems.agave, 1),
                new FluidStack(TFCFluids.AGAVEJUICE, 40), 0.8f));
    }

}
