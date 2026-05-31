package com.unforbidable.tfc.bids.features.resource.well;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.block.itemblock.ItemGenericSoil;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.well.block.BlockAquifer;
import com.unforbidable.tfc.bids.features.resource.well.item.ItemBucketRopeEmpty;
import com.unforbidable.tfc.bids.features.resource.well.item.ItemBucketRopeFluid;
import com.unforbidable.tfc.bids.features.resource.well.tileentity.TileEntityAquifer;
import com.unforbidable.tfc.bids.features.resource.well.waila.GenericSoilWailaProvider;
import com.unforbidable.tfc.bids.features.resource.well.worldgen.AquiferWorldGen;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.AQUIFER;
import static com.unforbidable.tfc.bids.api.names.BlockNames.AQUIFER_2;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CERAMIC_BUCKET_AND_ROPE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CERAMIC_BUCKET_AND_ROPE_FRESH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_BUCKET_AND_ROPE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_BUCKET_AND_ROPE_FRESH_WATER;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

@FeatureName("well")
public class Well extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(WellConfig::load, "worldGen");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WOODEN_BUCKET_AND_ROPE, () -> new ItemBucketRopeEmpty(false));
        init.item(WOODEN_BUCKET_AND_ROPE_FRESH_WATER, () -> new ItemBucketRopeFluid(false));

        init.item(CERAMIC_BUCKET_AND_ROPE, () -> new ItemBucketRopeEmpty(true));
        init.item(CERAMIC_BUCKET_AND_ROPE_FRESH_WATER, () -> new ItemBucketRopeFluid(true));

        init.block(AQUIFER, () -> new BlockAquifer(0, TFCBlocks.gravel), ItemGenericSoil.class);
        init.block(AQUIFER_2, () -> new BlockAquifer(16, TFCBlocks.gravel2), ItemGenericSoil.class);

        init.tileEntity(TileEntityAquifer.class, "BidsAquifer");
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.waila()
            .data(new GenericSoilWailaProvider(), TileEntityAquifer.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.world()
            .gen(new AquiferWorldGen(), 0);

        setup.fluid(TFCFluids.FRESHWATER)
            .container(BidsItems.ceramicBucketRopeWater, 1000, false, BidsItems.ceramicBucketRope)
            .container(BidsItems.woodenBucketRopeWater, 1000, false, BidsItems.woodenBucketRope);

        setup.recipes().addShapeless(new ItemStack(BidsItems.ceramicBucketRope),
            TFCItems.rope, TFCItems.clayBucketEmpty);

        setup.recipes().addShapeless(new ItemStack(BidsItems.woodenBucketRope),
            TFCItems.rope, TFCItems.woodenBucketEmpty);

        setup.recipes().addShapeless(new ItemStack(TFCItems.clayBucketEmpty),
                BidsItems.ceramicBucketRope)
            .action(extraDrop(new ItemStack(TFCItems.rope)));

        setup.recipes().addShapeless(new ItemStack(TFCItems.woodenBucketEmpty),
                BidsItems.woodenBucketRope)
            .action(extraDrop(new ItemStack(TFCItems.rope)));
    }

}
