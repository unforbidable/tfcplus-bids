package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistry;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetup;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Comparator;

public class FeatureInit extends Initializable {

    private final FeatureRegistry registry = new FeatureRegistry();
    private final FeatureContext context = new FeatureContext(registry.lookup);
    private final FeatureLoader loader = new FeatureLoader();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Bids.LOG.info("Register items");
        // create and register item instances
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).items.stream())
            .map(registry::initItem)
            .forEach(registry::registerItem);

        Bids.LOG.info("Register blocks");
        // create block instances in order of feature dependency
        // but register blocks in order of ids
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).blocks.stream())
            .map(registry::initBlock)
            .sorted(Comparator.comparingInt(b -> b.id))
            .forEach(registry::registerBlock);

        Bids.LOG.info("Register tile entities");
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).tileEntities.stream())
            .forEach(registry::registerTileEntity);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClientOnly(FMLPreInitializationEvent event) {
        Bids.LOG.info("Register simple block renderers");
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).blocks.stream())
            .filter(b -> b.blockRender != null)
            .forEach(registry::registerBlockRender);

        Bids.LOG.info("Register tile entity special renderers");
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).tileEntities.stream())
            .forEach(registry::registerTileEntitySpecialRender);

        Bids.LOG.info("Register item renderers");
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).items.stream())
            .filter(i -> i.itemRender != null)
            .forEach(registry::registerItemRenderer);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).items.stream())
            .filter(i -> i.container != null && i.fluid != null)
            .forEach(registry::registerFluidContainer);
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).items.stream())
            .filter(i -> i.drink != null)
            .forEach(registry::registerDrinks);

        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).containers.stream())
            .forEach(FeatureGuiSetup::registerGuiContainer);

        loader.getFeatures().stream()
            .peek(f -> Bids.LOG.info("Setup feature '{}'", f.metadata.name))
            .map(f -> f.setup(context))
            .forEach(FeatureSetup::setup);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClientOnly(FMLInitializationEvent event) {
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).screens.stream())
            .forEach(FeatureGuiSetup::registerGuiScreen);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Bids.LOG.info("Register fire info");
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).blocks.stream())
            .filter(b -> b.fireInfo != null)
            .forEach(registry::registerFireInfo);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClientOnly(FMLPostInitializationEvent event) {
    }

}
