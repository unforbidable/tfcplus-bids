package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpec;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.recipe.CraftingRecipeSetup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;

import java.util.List;

public class FeatureSetupParams {

    public final List<RegistryGroup<?>> values;
    public final List<OreGroup> ores;
    public final CraftingRecipeSetup crafting;
    public final List<Runnable> apply;
    public final List<EventHandlerSpec> handlers;

    public FeatureSetupParams(List<RegistryGroup<?>> values, List<OreGroup> ores,
                              CraftingRecipeSetup crafting, List<Runnable> apply,
                              List<EventHandlerSpec> handlers) {
        this.values = values;
        this.ores = ores;
        this.crafting = crafting;
        this.apply = apply;
        this.handlers = handlers;
    }

}
