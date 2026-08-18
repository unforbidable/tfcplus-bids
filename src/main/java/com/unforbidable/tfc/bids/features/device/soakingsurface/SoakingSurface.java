package com.unforbidable.tfc.bids.features.device.soakingsurface;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.soaking.SoakingRegistry;
import com.unforbidable.tfc.bids.features.device.soakingsurface.block.BlockSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.eventhandler.SoakingSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceHelper;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderTileSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.tileentity.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.waila.SoakingSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("soakingSurface")
public class SoakingSurface extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.SOAKING_SURFACE, BlockSoakingSurface::new);

        init.tileEntity(TileEntitySoakingSurface.class, "BidsSoakingSurface");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderSoakingSurface())
            .block(BlockSoakingSurface.class);

        client.render(new RenderTileSoakingSurface())
            .tileEntity(TileEntitySoakingSurface.class);

        client.waila()
            .data(new SoakingSurfaceWailaProvider(), TileEntitySoakingSurface.class);

        client.nei()
            .hide(BidsBlocks.soakingSurface);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new SoakingSurfaceEventHandler());

        setup.registry(SoakingSurfaceRegistry.recipes)
            .adapt(SoakingRegistry.recipes, SoakingSurfaceHelper::adaptSoakingRecipe);
    }

}
