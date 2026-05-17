package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.crafting.RecipeManager;
import com.unforbidable.tfc.bids.core.crafting.RecipeManagerSession;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistry;
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
        Bids.LOG.info("Register fluid containers");
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).items.stream())
            .filter(i -> i.container != null && i.fluid != null)
            .forEach(registry::registerFluidContainer);

        Bids.LOG.info("Register drinks");
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).items.stream())
            .filter(i -> i.drink != null)
            .forEach(registry::registerDrinks);

        Bids.LOG.info("Register GUI containers");
        loader.getFeatures().stream()
            .flatMap(f -> f.init(context).containers.stream())
            .forEach(registry::registerGuiContainer);

        Bids.LOG.info("Register ores");
        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).ores.stream())
            .forEach(registry::registerOre);

        Bids.LOG.info("Register crafting recipes and modifications");
        RecipeManagerSession session = RecipeManager.getSession();
        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).crafting.recipes.stream())
            .peek(r -> Bids.LOG.info("Register crafting recipe for {}", r.recipe.getRecipeOutput()))
            .forEach(session::add);

        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).crafting.matchers.stream())
            .peek(m -> Bids.LOG.info("Handle crafting recipes changes"))
            .forEach(session::match);
        session.flush();

        Bids.LOG.info("Register list values");
        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).lists.stream())
            .forEach(registry::registerList);

        Bids.LOG.info("Register map values");
        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).maps.stream())
            .forEach(registry::registerMap);

        Bids.LOG.info("Register event handlers");
        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).handlers.stream())
            .forEach(registry::registerEventHandler);

        loader.getFeatures().stream()
            .flatMap(f -> f.setup(context).runs.stream())
            .forEach(Runnable::run);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClientOnly(FMLInitializationEvent event) {
        Bids.LOG.info("Register client GUI screens");
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).screens.stream())
            .forEach(registry::registerGuiScreen);

        Bids.LOG.info("Register client event handlers");
        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).handlers.stream())
            .forEach(registry::registerClientEventHandler);

        loader.getFeatures().stream()
            .flatMap(f -> f.client(context).runs.stream())
            .forEach(Runnable::run);
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
        registry.check();
    }

}
