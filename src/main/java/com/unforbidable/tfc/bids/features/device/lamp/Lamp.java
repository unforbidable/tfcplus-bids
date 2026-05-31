package com.unforbidable.tfc.bids.features.device.lamp;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.lamp.block.BlockClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.block.itemblock.ItemClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.main.fuel.FuelOliveOil;
import com.unforbidable.tfc.bids.features.device.lamp.render.RenderClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.tileentity.TileEntityClayLamp;
import com.unforbidable.tfc.bids.features.device.lamp.waila.ClayLampWailaProvider;
import com.unforbidable.tfc.bids.features.material.fishoil.fuel.FuelFishOil;
import com.unforbidable.tfc.bids.features.material.linseed.fuel.FuelFlaxSeedOil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.CLAY_LAMP;

@FeatureName("lamp")
public class Lamp extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(LampConfig::load, "lightSources");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(CLAY_LAMP, BlockClayLamp::new, ItemClayLamp.class);

        init.tileEntity(TileEntityClayLamp.class, "BidsClayLamp");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderClayLamp())
            .block(BlockClayLamp.class);

        client.waila()
            .data(new ClayLampWailaProvider(), TileEntityClayLamp.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShaped(new ItemStack(BidsBlocks.clayLamp),
            "S ", "B ", 'S', "materialString",
            'B', new ItemStack(TFCItems.potteryBowl, 1, 1));

        setup.registry(LampRegistry.fuel)
            .add(TFCFluids.OLIVEOIL, new FuelOliveOil());

        // TODO register lamp fuels in respective features

        setup.registry(LampRegistry.fuel)
            .add(BidsFluids.FISHOIL, new FuelFishOil());

        setup.registry(LampRegistry.fuel)
            .add(BidsFluids.FLAXSEEDOIL, new FuelFlaxSeedOil());
    }

}
