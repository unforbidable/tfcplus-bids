package com.unforbidable.tfc.bids.features.food.pemmican;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepIngredient;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.cookingprep.CookingPrepRegistry;
import com.unforbidable.tfc.bids.features.food.pemmican.item.ItemPemmican;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.PEMMICAN;

@FeatureName("pemmican")
public class Pemmican extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(PEMMICAN, () -> new ItemPemmican(new float[]{0, 40, 20, 10, 10}))
            .meta("Pemmican");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.pemmican);

        client.nei()
            .hide(BidsItems.pemmican);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CookingPrepIngredient leanMeat = CookingPrepIngredient.builder()
            .allow(TFCItems.beefRaw)
            .allow(TFCItems.venisonRaw)
            .allow(TFCItems.muttonRaw)
            .allow(TFCItems.horseMeatRaw)
            .build();
        CookingPrepIngredient tallow = CookingPrepIngredient.builder()
            .allow(BidsItems.tallow)
            .build();
        CookingPrepIngredient berriesAndFlours = CookingPrepIngredient.builder()
            .allow("foodFruitBerry")
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .build();
        CookingPrepRegistry.recipes.add(new CookingPrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.pemmican)),
            CookingPrepIngredient.from(BidsItems.moreHide, 0).toSpec(),
            leanMeat.toSpec(40, true), tallow.toSpec(20, true), berriesAndFlours.toSpec(10), berriesAndFlours.toSpec(10)));
    }

}
