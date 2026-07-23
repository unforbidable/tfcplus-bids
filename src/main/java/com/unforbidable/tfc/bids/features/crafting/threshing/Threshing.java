package com.unforbidable.tfc.bids.features.crafting.threshing;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.threshing.ThreshingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.threshing.eventhandler.ThreshingEventHandler;
import com.unforbidable.tfc.bids.features.crafting.threshing.nei.ThreshingNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("threshing")
public class Threshing extends Feature {

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new ThreshingNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new ThreshingEventHandler());

        setup.ores("itemThreshingTool")
            .add(TFCItems.pole);
        setup.ores("itemPrimitiveTool")
            .add(TFCItems.pole);

        setup.ores("blockThreshingSurface")
            .add(TFCBlocks.stoneSed, TFCBlocks.stoneMM, TFCBlocks.stoneIgEx, TFCBlocks.stoneIgIn)
            .add(TFCBlocks.stoneSedSmooth, TFCBlocks.stoneMMSmooth, TFCBlocks.stoneIgExSmooth, TFCBlocks.stoneIgInSmooth)
            .add(BidsBlocks.roughStoneSed, BidsBlocks.roughStoneMM, BidsBlocks.roughStoneIgEx, BidsBlocks.roughStoneIgIn)
            .add(BidsBlocks.roughStoneTileSed, BidsBlocks.roughStoneTileMM, BidsBlocks.roughStoneTileIgEx, BidsBlocks.roughStoneTileIgIn);

        setup.registry(ThreshingRegistry.recipes)
            .add(new ThreshingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatGrain), 2),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatWhole), 4), new ItemStack(TFCItems.straw), 20))
            .add(new ThreshingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyGrain), 2),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyWhole), 4), new ItemStack(TFCItems.straw), 20))
            .add(new ThreshingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.oatGrain), 2),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.oatWhole), 4), new ItemStack(TFCItems.straw), 20))
            .add(new ThreshingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeGrain), 2),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeWhole), 4), new ItemStack(TFCItems.straw), 20))
            .add(new ThreshingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceGrain), 2),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.riceWhole), 4), new ItemStack(TFCItems.straw), 10));
    }

}
