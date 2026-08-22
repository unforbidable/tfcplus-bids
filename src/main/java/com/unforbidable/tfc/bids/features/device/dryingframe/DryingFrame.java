package com.unforbidable.tfc.bids.features.device.dryingframe;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingframe.block.BlockDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingframe.eventhandler.DryingPegsEventHandler;
import com.unforbidable.tfc.bids.features.device.dryingframe.render.RenderDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingframe.render.RenderTileDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingframe.waila.DryingPegsWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("dryingFrame")
public class DryingFrame extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.DRYING_PEGS, BlockDryingPegs::new);

        init.tileEntity(TileEntityDryingPegs.class, "BidsDryingPegs");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderDryingPegs())
            .block(BlockDryingPegs.class);

        client.render(new RenderTileDryingPegs())
            .tileEntity(TileEntityDryingPegs.class);

        client.nei()
            .hide(BidsBlocks.dryingPegs);

        client.waila()
            .data(new DryingPegsWailaProvider(), TileEntityDryingPegs.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new DryingPegsEventHandler());
    }

}
