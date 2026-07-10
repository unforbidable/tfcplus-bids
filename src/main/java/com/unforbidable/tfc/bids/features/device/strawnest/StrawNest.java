package com.unforbidable.tfc.bids.features.device.strawnest;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.strawnest.block.BlockStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.container.ContainerStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.eventhandler.ChickenEntitySpawnHandler;
import com.unforbidable.tfc.bids.features.device.strawnest.gui.GuiStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.render.RenderStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.tileentity.TileEntityStrawNest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.STRAW_NEST;

@FeatureName("strawNest")
public class StrawNest extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(STRAW_NEST, BlockStrawNest::new)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityStrawNest.class, "BidsStrawNest");

        init.gui(STRAW_NEST, ContainerStrawNest::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderStrawNest())
            .block(BlockStrawNest.class);

        client.gui(STRAW_NEST, GuiStrawNest::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new ChickenEntitySpawnHandler());

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsBlocks.strawNest, 1),
                "     ", "#   #", "#   #", " ### ", "     ", '#',
                new ItemStack(TFCItems.flatStraw, 1)));
    }

}
