package com.unforbidable.tfc.bids.features.device.firepit;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.firestarting.FireStartingRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.block.BlockNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.block.BlockTiedStickBundle;
import com.unforbidable.tfc.bids.features.device.firepit.container.ContainerNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.eventhandler.FirepitInteractHandler;
import com.unforbidable.tfc.bids.features.device.firepit.gui.GuiNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.item.ItemSmallStickBundle;
import com.unforbidable.tfc.bids.features.device.firepit.item.ItemTiedStickBundle;
import com.unforbidable.tfc.bids.features.device.firepit.main.firestarting.FirepitFireStartingHandler;
import com.unforbidable.tfc.bids.features.device.firepit.main.fuels.FuelCoalTFC;
import com.unforbidable.tfc.bids.features.device.firepit.main.fuels.FuelLogsTFC;
import com.unforbidable.tfc.bids.features.device.firepit.main.fuels.FuelPeatTFC;
import com.unforbidable.tfc.bids.features.device.firepit.main.fuels.FuelStickBundleTFC;
import com.unforbidable.tfc.bids.features.device.firepit.main.fuels.FuelStickTFC;
import com.unforbidable.tfc.bids.features.device.firepit.nei.FirepitFuelNeiHandler;
import com.unforbidable.tfc.bids.features.device.firepit.render.RenderNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.waila.FirepitWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FeatureName("firepit")
public class Firepit extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(FirepitConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.FIREPIT, BlockNewFirepit::new);
        init.block(BlockNames.TIED_STICK_BUNDLE, BlockTiedStickBundle::new);

        init.item(ItemNames.STICK_BUNDLE_SMALL, ItemSmallStickBundle::new);
        init.item(ItemNames.STICK_BUNDLE_TIED, ItemTiedStickBundle::new);

        init.tileEntity(TileEntityNewFirepit.class, "BidsNewFirepit");

        init.gui(BlockNames.FIREPIT, ContainerNewFirepit::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderNewFirepit())
            .block(BlockNewFirepit.class);

        client.gui(BlockNames.FIREPIT, GuiNewFirepit::new);

        client.nei()
            .handler(new FirepitFuelNeiHandler())
            .hide(BidsBlocks.newFirepit)
            .hide(BidsBlocks.tiedStickBundle);

        client.waila()
            .data(new FirepitWailaProvider(), TileEntityNewFirepit.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new FirepitInteractHandler());

        setup.ores("materialKindling")
            .add(BidsItems.smallStickBundle)
            .add(BidsItems.tiedStickBundle)
            .add(TFCItems.stick)
            .add(TFCItems.stickBundle);

        setup.recipes().addShapeless(new ItemStack(BidsItems.smallStickBundle),
            "stickWood", "stickWood", "stickWood");
        setup.recipes().addShapeless(new ItemStack(TFCItems.stick, 3),
            BidsItems.smallStickBundle);

        setup.recipes().addShapeless(new ItemStack(BidsItems.tiedStickBundle),
            BidsItems.smallStickBundle, new ItemStack(BidsItems.smallStickBundle),
            BidsItems.smallStickBundle, TFCItems.grassCordage);
        setup.recipes().addShapeless(new ItemStack(BidsItems.tiedStickBundle),
            TFCItems.stickBundle, TFCItems.grassCordage);

        setup.recipes().addShapeless(new ItemStack(TFCItems.stick, 9),
            BidsItems.tiedStickBundle);

        setup.registry(FirepitRegistry.fuel)
            .add(BidsItems.smallStickBundle, (FirepitFuelMaterial) BidsItems.smallStickBundle)
            .add(BidsItems.tiedStickBundle, (FirepitFuelMaterial) BidsItems.tiedStickBundle)
            .add(TFCItems.stick, new FuelStickTFC())
            .add(TFCItems.fireStarter, new FuelStickTFC())
            .add(TFCItems.stickBundle, new FuelStickBundleTFC())
            .add(Item.getItemFromBlock(TFCBlocks.peat), new FuelPeatTFC());

        if (FirepitConfig.allowFuelLogsTFC) {
            setup.registry(FirepitRegistry.fuel)
                .add(TFCItems.logs, new FuelLogsTFC());
        }

        if (FirepitConfig.allowFuelCharcoal) {
            setup.registry(FirepitRegistry.fuel)
                .add(TFCItems.coal, new FuelCoalTFC());
        }

        if (FirepitConfig.replaceFirepitTFC) {
            setup.run(() -> {
                TFCBlocks.firepit = BidsBlocks.newFirepit;
            });
        }

        setup.registry(FireStartingRegistry.handlers)
            .add(new FirepitFireStartingHandler(8));
    }

}
