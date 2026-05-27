package com.unforbidable.tfc.bids.features.crafting.dough;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonFlat;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.dough.container.ContainerSpecialCraftingDough;
import com.unforbidable.tfc.bids.features.crafting.dough.gui.GuiKnappingDough;
import com.unforbidable.tfc.bids.features.crafting.dough.item.ItemUnshapedDough;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_DOUGH_UNSHAPED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_DOUGH_UNSHAPED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAT_DOUGH;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_DOUGH_UNSHAPED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_DOUGH_UNSHAPED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_DOUGH_UNSHAPED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_DOUGH_UNSHAPED;

@FeatureName("dough")
public class Dough extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(FLAT_DOUGH, ItemCommonFlat::new)
            .apply(i -> i.setTextureFolder("food"));

        init.item(WHEAT_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(0));
        init.item(BARLEY_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(1));
        init.item(OAT_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(2));
        init.item(RYE_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(3));
        init.item(RICE_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(4));
        init.item(CORN_DOUGH_UNSHAPED, () -> new ItemUnshapedDough(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(1, 0.7f)
            .apply(i -> i.setFlatDoughDamage(5));

        init.gui(GuiNames.DOUGH, ContainerSpecialCraftingDough::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.wheatDoughUnshaped)
            .item(BidsItems.barleyDoughUnshaped)
            .item(BidsItems.oatDoughUnshaped)
            .item(BidsItems.ryeDoughUnshaped)
            .item(BidsItems.riceDoughUnshaped)
            .item(BidsItems.cornmealDoughUnshaped);

        client.gui(GuiNames.DOUGH, GuiKnappingDough::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatGround)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyGround)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.oatGround)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeGround)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.riceGround)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughUnshaped)),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.cornmealGround)), "itemLargeBowlWater");

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 0)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 1)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.oatDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 2)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 3)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 4)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(TFCItems.cornmealDough), 160),
                "     ", " ### ", "#####", "#####", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 5)));
    }

}
