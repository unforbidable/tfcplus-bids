package com.unforbidable.tfc.bids.features.building.peg;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.EntityNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.building.peg.block.BlockPeg;
import com.unforbidable.tfc.bids.features.building.peg.entity.EntityPegLeashKnot;
import com.unforbidable.tfc.bids.features.building.peg.eventhandler.LeashKnotEventHandler;
import com.unforbidable.tfc.bids.features.building.peg.render.RenderPeg;
import com.unforbidable.tfc.bids.features.building.peg.render.RenderPegLeashKnot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("peg")
public class Peg extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.WOODEN_PEG, BlockPeg::new);

        init.entity(EntityNames.PEG_LEASH_KNOT, EntityPegLeashKnot.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderPeg())
            .block(BlockPeg.class);

        client.render(new RenderPegLeashKnot())
            .entity(EntityPegLeashKnot.class);

        client.nei()
            .hide(BidsBlocks.woodenPeg);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new LeashKnotEventHandler());
    }

}
