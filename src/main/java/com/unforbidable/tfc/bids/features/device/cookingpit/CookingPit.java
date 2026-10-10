package com.unforbidable.tfc.bids.features.device.cookingpit;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.cookingpit.block.BlockClayCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.block.BlockHideCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.eventhandler.CookingPitEventHandler;
import com.unforbidable.tfc.bids.features.device.cookingpit.render.RenderHideCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.render.RenderTileHideCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityClayCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.waila.ClayCookingPitWailaProvider;
import com.unforbidable.tfc.bids.features.device.cookingpit.waila.HideCookingPitWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("cookingPit")
public class CookingPit extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.HIDE_COOKING_PIT, BlockHideCookingPit::new)
            .apply(b -> b.setBlockTextureName("Cooking Pit.Hide"));
        init.block(BlockNames.CLAY_COOKING_PIT, BlockClayCookingPit::new)
            .apply(b -> b.setBlockTextureName("Cooking Pit.Clay"));

        init.tileEntity(TileEntityHideCookingPit.class, "BidsHideCookingPit");
        init.tileEntity(TileEntityClayCookingPit.class, "BidsClayCookingPit");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderHideCookingPit())
            .block(BlockHideCookingPit.class)
            .block(BlockClayCookingPit.class);

        client.render(new RenderTileHideCookingPit())
            .tileEntity(TileEntityHideCookingPit.class)
            .tileEntity(TileEntityClayCookingPit.class);

        client.nei()
            .hide(BidsBlocks.hideCookingPit)
            .hide(BidsBlocks.clayCookingPit);

        client.waila()
            .data(new HideCookingPitWailaProvider(), TileEntityHideCookingPit.class)
            .data(new ClayCookingPitWailaProvider(), TileEntityClayCookingPit.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new CookingPitEventHandler());
    }

}
