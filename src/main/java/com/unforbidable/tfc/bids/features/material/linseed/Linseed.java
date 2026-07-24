package com.unforbidable.tfc.bids.features.material.linseed;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.pressing.PressingRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBowlFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemPotteryFluid;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.pressing.PressingRegistry;
import com.unforbidable.tfc.bids.features.device.lamp.LampRegistry;
import com.unforbidable.tfc.bids.features.material.linseed.fuel.FuelFlaxSeedOil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("linseed")
public class Linseed extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.FLAX_SEED_OIL, FluidCommon::new)
            .color(0x977d59);

        init.item(ItemNames.FLAX_SEEDS, () -> new ItemExtraFood(EnumFoodGroup.Protein, 10, 0, 0, 10, 0))
            .food(0.01f);

        init.item(ItemNames.BOTTLE_FLAX_SEED_OIL, ItemGlassBottleFluid::new);
        init.item(ItemNames.JUG_FLAX_SEED_OIL, ItemPotteryFluid::new);
        init.item(ItemNames.BOWL_FLAX_SEED_OIL, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.flaxSeeds);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(BidsFluids.flaxSeedOil)
            .container(BidsItems.flaxSeedOilBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugFlaxSeedOil, 1000, true, TFCItems.potteryJug, 1)
            .container(BidsItems.flaxSeedOilBowl, 0, 250, false, TFCItems.potteryBowl, 1)
            .container(BidsItems.flaxSeedOilBowl, 1, 250, false, TFCItems.potteryBowl, 2);

        setup.registry(LampRegistry.fuel)
            .add(BidsFluids.flaxSeedOil, new FuelFlaxSeedOil());

        setup.registry(PressingRegistry.recipes)
            .add(new PressingRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 0.8f),
                new FluidStack(BidsFluids.flaxSeedOil, 10), 0.25f));
    }

}
