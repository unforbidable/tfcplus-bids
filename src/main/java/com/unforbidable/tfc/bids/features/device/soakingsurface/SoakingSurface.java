package com.unforbidable.tfc.bids.features.device.soakingsurface;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingSurfaceRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.soakingsurface.block.BlockSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.eventhandler.SoakingSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.render.RenderTileSoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.tileentity.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.features.device.soakingsurface.waila.SoakingSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.SOAKING_SURFACE;

@FeatureName("soakingSurface")
public class SoakingSurface extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(SOAKING_SURFACE, BlockSoakingSurface::new);

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
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new SoakingSurfaceEventHandler());

        setup.ores("blockFreshWater")
            .add(TFCBlocks.freshWater)
            .add(TFCBlocks.freshWaterStationary);

        setup.registry(SoakingSurfaceRegistry.recipes)
            .add(new SoakingSurfaceRecipe(new ItemStack(TFCItems.flaxFiber),
                new ItemStack(TFCItems.flax), "blockFreshWater", 1));


        setup.registry(SoakingSurfaceRegistry.recipes)
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkRetted),
                new ItemStack(BidsItems.flaxStalk), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.juteStalkRetted),
                new ItemStack(BidsItems.juteStalk), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.sisalFiberRinsed),
                new ItemStack(TFCItems.sisalFiber), "blockFreshWater", 0))
            // Washing wool can be skipped however the wool needs to be rinsed for an extended period of time
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed),
                new ItemStack(TFCItems.wool), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed),
                new ItemStack(BidsItems.woolWashed), "blockFreshWater", 0));
    }

}
