package com.unforbidable.tfc.bids.features.building.decorativesurface;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.building.decorativesurface.block.BlockDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.eventhandler.DecorativeSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.building.decorativesurface.render.RenderDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.render.RenderTileDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.tileentity.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.features.building.decorativesurface.waila.DecorativeSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.DECORATIVE_SURFACE;

@FeatureName("decorativeSurface")
public class DecorativeSurface extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(DECORATIVE_SURFACE, BlockDecorativeSurface::new);

        init.tileEntity(TileEntityDecorativeSurface.class, "BidsDecorativeSurface");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderDecorativeSurface())
            .block(BlockDecorativeSurface.class);

        client.render(new RenderTileDecorativeSurface())
            .tileEntity(TileEntityDecorativeSurface.class);

        client.waila()
            .data(new DecorativeSurfaceWailaProvider(), TileEntityDecorativeSurface.class);

        client.nei()
            .hide(BidsBlocks.decorativeSurface);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new DecorativeSurfaceEventHandler());

        setup.ores("itemDecorativeSurface")
            .add(TFCItems.fur)
            .add(TFCItems.furScrap)
            .add(TFCItems.wolfFur)
            .add(TFCItems.wolfFurScrap)
            .add(TFCItems.bearFur)
            .add(TFCItems.bearFurScrap)
            .add(TFCItems.hide)
            .add(new ItemStack(TFCItems.sheepSkin, 1, 0))
            .add(new ItemStack(TFCItems.sheepSkin, 1, 1))
            .add(new ItemStack(TFCItems.sheepSkin, 1, 2));
    }

}
