package com.unforbidable.tfc.bids.features.food.bamboo;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BAMBOO_SHOOT;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("bamboo")
public class Bamboo extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(BAMBOO_SHOOT, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 20, 0, 0, 0, 10))
            .food(1.8f, 0.3f, true, true, true, true)
            .apply(ItemFoodTFC::setHasCookedIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.bambooShoot);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.bambooShoot), 2.5f),
                new ItemStack(TFCBlocks.sapling2, 1, 8), "itemKnife")
            .action(damageTool("itemKnife"));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.add(new ItemStack(BidsItems.bambooShoot), 1, 82, null));
    }

}
