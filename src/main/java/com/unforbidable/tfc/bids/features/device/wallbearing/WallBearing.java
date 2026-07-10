package com.unforbidable.tfc.bids.features.device.wallbearing;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.wallbearing.block.BlockAxleWallBearing;
import com.unforbidable.tfc.bids.features.device.wallbearing.eventhandler.WallBearingPlacementHighlightHandler;
import com.unforbidable.tfc.bids.features.device.wallbearing.render.RenderAxleWallBearing;
import com.unforbidable.tfc.bids.features.device.wallbearing.tileentity.TileEntityAxleWallBearing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

/**
 * <li><b>wooden axle wall bearing</b> - allows the passage of axles seamlessly through walls</li>
 */
@FeatureName("wallBearing")
public class WallBearing extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.WOOD_AXLE_WALL_BEARING, () -> new BlockAxleWallBearing(Material.wood))
            .hardness(0.5f)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityAxleWallBearing.class, "BidsAxleWallBearing");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderAxleWallBearing())
            .block(BlockAxleWallBearing.class);

        client.event()
            .handler(new WallBearingPlacementHighlightHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Also used in screw press
        setup.ores("supportWood")
            .add(TFCBlocks.woodSupportH)
            .add(TFCBlocks.woodSupportH)
            .add(TFCBlocks.woodSupportH2)
            .add(TFCBlocks.woodSupportH3)
            .add(TFCBlocks.woodSupportV)
            .add(TFCBlocks.woodSupportV2)
            .add(TFCBlocks.woodSupportV3);

        setup.recipes().addShaped(new ItemStack(BidsBlocks.woodAxleWallBearing),
            "LSL", "L L", "LSL", 'L', "woodLumber", 'S', "supportWood");
    }

}
