package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.RecipeManager;
import com.unforbidable.tfc.bids.core.crafting.RecipeManagerSession;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;
import net.minecraftforge.oredict.OreDictionary;

public class FeatureSetup {

    public static void setup(FeatureSetupParams setup) {
        setup.ores.stream()
            .peek(oreGroup -> Bids.LOG.info("Register {} ore(s) for '{}'", oreGroup.items.size(), oreGroup.name))
            .forEach(FeatureSetup::registerOres);

        RecipeManagerSession session = RecipeManager.getSession();
        setup.crafting.recipes.stream()
            .peek(r -> Bids.LOG.info("Register crafting recipe for {}", r.recipe.getRecipeOutput()))
            .forEach(session::add);

        setup.crafting.matchers.stream()
            .peek(m -> Bids.LOG.info("Handle crafting recipes changes"))
            .forEach(session::match);

        setup.values.stream()
            .peek(reg -> Bids.LOG.info("Register {} value(s)", reg.values.size()))
            .forEach(FeatureSetup::registerValues);

        setup.apply.forEach(Runnable::run);
    }

    private static <T> void registerValues(RegistryGroup<T> group) {
        group.values.forEach(group.registry::add);
    }

    private static void registerOres(OreGroup oreGroup) {
        oreGroup.items.forEach(o -> OreDictionary.registerOre(oreGroup.name, o));
    }

}
