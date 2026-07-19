package com.unforbidable.tfc.bids.features.food.milk;

import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemCommonDrink;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBucketFluid;
import com.unforbidable.tfc.bids.core.drink.DrinkRegistry;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkFluid;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.food.milk.eventhandler.AnimalMilkHandler;
import com.unforbidable.tfc.bids.features.food.milk.eventhandler.EntitySpawnHandler;
import com.unforbidable.tfc.bids.features.food.milk.eventhandler.MilkingInteractHandler;
import com.unforbidable.tfc.bids.features.food.milk.waila.AnimalMilkWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("milk")
public class Milk extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(MilkConfig::load, "husbandry");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.GOAT_MILK, FluidCommon::new)
            .color(0xffffff);

        init.item(ItemNames.BOTTLE_GOAT_MILK, () -> new ItemCommonDrink(1000, false, 0, 20, 40, 60, 80, 100))
            .apply(i -> i.setCanDrinkInParts(true)
                .setFoodGroup(EnumFoodGroup.Dairy)
                .setCalories(0.642f)
                .setWaterRestoreRatio(1f));
        init.item(ItemNames.JUG_GOAT_MILK, () -> new ItemCommonDrink(1000, true, 0, 20, 40, 60, 80, 100))
            .apply(i -> i.setCanDrinkInParts(true)
                .setFoodGroup(EnumFoodGroup.Dairy)
                .setCalories(0.642f)
                .setWaterRestoreRatio(1f));

        init.item(ItemNames.WOODEN_BUCKET_GOAT_MILK, () -> new ItemBucketFluid(false));
        init.item(ItemNames.CERAMIC_BUCKET_GOAT_MILK, () -> new ItemBucketFluid(true));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.waila()
            .entity(new AnimalMilkWailaProvider(), IAnimal.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(BidsFluids.goatMilk)
            .container(BidsItems.goatMilkBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugGoatMilk, 1000, true, TFCItems.potteryJug, 1)
            .container(BidsItems.woodenBucketGoatMilk, 1000, false, TFCItems.woodenBucketEmpty)
            .container(BidsItems.ceramicBucketGoatMilk, 1000, false, TFCItems.clayBucketEmpty, 1);

        setup.registry(DrinkRegistry.drinks)
            .add(new DrinkFluid("GoatMilk", BidsFluids.goatMilk, 1, 0.642f, EnumFoodGroup.Dairy));

        setup.ores("itemMilkingContainer")
            .add(TFCItems.clayBucketEmpty)
            .add(TFCItems.woodenBucketEmpty);

        setup.event()
            .handler(new EntitySpawnHandler())
            .handler(new MilkingInteractHandler())
            .handler(new AnimalMilkHandler());
    }

}
