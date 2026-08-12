package com.unforbidable.tfc.bids.features.resource.flora;

import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.flora.block.BlockMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.block.blockitem.ItemMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.render.RenderBlockMoreGrass;
import com.unforbidable.tfc.bids.features.resource.flora.worldgen.FloraWorldGen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("flora")
public class Flora extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.MORE_GRASS, BlockMoreGrass::new, ItemMoreGrass.class)
            .meta("Nettle")
            .harvest("knife", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderBlockMoreGrass())
            .block(BlockMoreGrass.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.world()
            .gen(new FloraWorldGen(), 0);
    }

}
