package com.unforbidable.tfc.bids.features.device.dryingsurface;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingsurface.block.BlockDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.eventhandler.DryingSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderTileDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.tileentity.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.waila.DryingSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("dryingSurface")
public class DryingSurface extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(DryingSurfaceConfig::load, "crafting");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.DRYING_SURFACE, BlockDryingSurface::new);

        init.tileEntity(TileEntityDryingSurface.class, "BidsDryingSurface");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderDryingSurface())
            .block(BlockDryingSurface.class);

        client.render(new RenderTileDryingSurface())
            .tileEntity(TileEntityDryingSurface.class);

        client.waila()
            .data(new DryingSurfaceWailaProvider(), TileEntityDryingSurface.class);

        client.nei()
            .hide(BidsBlocks.dryingSurface);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new DryingSurfaceEventHandler());
    }

}
