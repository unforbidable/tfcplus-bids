package com.unforbidable.tfc.bids.features.device.dryingsurface;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingsurface.block.BlockDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.eventhandler.DryingSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.device.dryingsurface.main.rendering.SoapRenderInfo;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.render.RenderTileDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.tileentity.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.features.device.dryingsurface.waila.DryingSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.DRYING_SURFACE;

@FeatureName("dryingSurface")
public class DryingSurface extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(DryingSurfaceConfig::load, "crafting");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(DRYING_SURFACE, BlockDryingSurface::new);

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
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new DryingSurfaceEventHandler());

        // TODO move to respective feature
        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.barkFibre))
                .produces(new ItemStack(BidsItems.barkFibreCoarse))
                .dry()
                .hours(12)
                .build())
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.flaxStalk))
                .produces(new ItemStack(BidsItems.flaxStalkRetted))
                .wet()
                .warm()
                .hours(20)
                .build())
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.flaxStalkRetted))
                .produces(new ItemStack(BidsItems.flaxStalkDried))
                .dry()
                .hours(20)
                .build());

        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1))
                .dry()
                .cover()
                .hours(40)
                .build());

        setup.registry(DryingSurfaceRegistry.render)
            .add(BidsItems.soap, new SoapRenderInfo(true))
            .add(BidsItems.uncuredSoap, new SoapRenderInfo(false));
    }

}
