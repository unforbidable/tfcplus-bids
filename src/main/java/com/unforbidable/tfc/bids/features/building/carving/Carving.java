package com.unforbidable.tfc.bids.features.building.carving;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.keybinding.KeyBindings;
import com.unforbidable.tfc.bids.features.building.carving.block.BlockCarving;
import com.unforbidable.tfc.bids.features.building.carving.eventhandler.AdzeHighlightHandler;
import com.unforbidable.tfc.bids.features.building.carving.eventhandler.AdzeRenderOverlayHandler;
import com.unforbidable.tfc.bids.features.building.carving.nei.CarvingNeiHandler;
import com.unforbidable.tfc.bids.features.building.carving.network.CarvingPacket;
import com.unforbidable.tfc.bids.features.building.carving.render.RenderCarving;
import com.unforbidable.tfc.bids.features.building.carving.tileentity.TileEntityCarving;
import com.unforbidable.tfc.bids.features.building.carving.waila.CarvingWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;

import static com.unforbidable.tfc.bids.api.names.BlockNames.CARVING_ROCK;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CARVING_WOOD;

@FeatureName("carving")
public class Carving extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(CarvingConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(CARVING_ROCK, () -> new BlockCarving(Material.rock))
            .harvest("shovel", 0);

        init.block(CARVING_WOOD, () -> new BlockCarving(Material.wood))
            .harvest("axe", 0)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityCarving.class, "BidsCarving");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        // only register once and use for any carving block
        client.render(new RenderCarving())
            .block(BlockCarving.class);

        client.keys()
            .action(KeyBindings.toolMode, CarvingKeyBinding::changeToolMode);

        client.event()
            .handler(new AdzeHighlightHandler())
            .handler(new AdzeRenderOverlayHandler());

        client.waila()
            .data(new CarvingWailaProvider(), TileEntityCarving.class);

        client.nei()
            .handler(new CarvingNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.network()
            .register(CarvingPacket.class);

        // TODO register carving in respective feature
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableLogWall());
//        BidsRegistry.CARVING_BLOCKS.register(new CarvableLogWallVert());
    }

}
