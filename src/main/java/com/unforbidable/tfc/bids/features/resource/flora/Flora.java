package com.unforbidable.tfc.bids.features.resource.flora;

import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.chunk.ChunkRegistry;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.flora.block.BlockBrackenFern;
import com.unforbidable.tfc.bids.features.resource.flora.block.BlockMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.block.blockitem.ItemBrackenFern;
import com.unforbidable.tfc.bids.features.resource.flora.block.blockitem.ItemMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.eventhandler.FernChunkEventHandler;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.render.RenderBlockBrackenFern;
import com.unforbidable.tfc.bids.features.resource.flora.render.RenderBlockMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.worldgen.BrackenFernWorldGen;
import com.unforbidable.tfc.bids.features.resource.flora.worldgen.FloraWorldGen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("flora")
public class Flora extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(FloraConfig::load, "worldGen");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.MORE_GRASS, BlockMoreGrass::new, ItemMoreGrass.class)
            .meta("Nettle")
            .harvest("knife", 0);
        init.block(BlockNames.BRACKEN_FERN, BlockBrackenFern::new, ItemBrackenFern.class)
            .texture("Bracken Fern")
            .harvest("digger", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderBlockMoreGrass())
            .block(BlockMoreGrass.class);

        client.render(new RenderBlockBrackenFern())
            .block(BlockBrackenFern.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.world()
            .gen(new FloraWorldGen(), 0)
            .gen(new BrackenFernWorldGen(), 0);

        setup.event()
            .handler(new FernChunkEventHandler());

        setup.registry(ChunkRegistry.data)
            .add(FernChunkData.class);
    }

}
