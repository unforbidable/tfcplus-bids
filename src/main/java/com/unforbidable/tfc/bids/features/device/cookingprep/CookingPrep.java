package com.unforbidable.tfc.bids.features.device.cookingprep;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepIngredient;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepSaladRecipe;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.cookingprep.block.BlockCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.container.ContainerCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.eventhandler.CookingPrepEventHandler;
import com.unforbidable.tfc.bids.features.device.cookingprep.gui.GuiCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.nei.CookingPrepNeiHandler;
import com.unforbidable.tfc.bids.features.device.cookingprep.render.RenderTileCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.tileentity.TileEntityCookingPrep;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FeatureName("cookingPrep")
public class CookingPrep extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.COOKING_PREP, BlockCookingPrep::new);

        init.tileEntity(TileEntityCookingPrep.class, "BidsCookingPrep");

        init.gui(BlockNames.COOKING_PREP, ContainerCookingPrep::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.gui(BlockNames.COOKING_PREP, GuiCookingPrep::new);

        client.render(new RenderTileCookingPrep())
            .tileEntity(TileEntityCookingPrep.class);

        client.nei()
            .handler(new CookingPrepNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new CookingPrepEventHandler());

        setup.ores("itemCookingPrepVessel")
            .add(new ItemStack(TFCItems.potteryBowl, 1, 1))
            .add(new ItemStack(TFCItems.potteryBowl, 1, 2));

        // TFC sandwich
        CookingPrepIngredient sandwichIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .build();
        Item[] breads = new Item[]{TFCItems.wheatBread, TFCItems.oatBread, TFCItems.barleyBread, TFCItems.ryeBread, TFCItems.cornBread, TFCItems.riceBread};
        for (int i = 0; i < breads.length; i++) {
            setup.registry(CookingPrepRegistry.recipes)
                .add(new CookingPrepRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.sandwich, 1, i)), 7,
                    CookingPrepIngredient.from(breads[i]).toSpec(2),
                    sandwichIngredients.toSpec(3), sandwichIngredients.toSpec(2),
                    sandwichIngredients.toSpec(2), sandwichIngredients.toSpec(1)));
        }

        // TFC Salas
        CookingPrepIngredient bowls = CookingPrepIngredient.builder()
            .allow(TFCItems.potteryBowl, 1)
            .allow(TFCItems.potteryBowl, 2)
            .build();
        CookingPrepIngredient saladIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(TFCItems.riceGrain)
            .build();
        setup.registry(CookingPrepRegistry.recipes)
            .add(new CookingPrepSaladRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.salad)), 14,
                bowls.toSpec(), saladIngredients.toSpec(10),
                saladIngredients.toSpec(4), saladIngredients.toSpec(4), saladIngredients.toSpec(2)));
    }

}
