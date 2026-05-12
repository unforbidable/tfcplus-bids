package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpec;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.recipe.CraftingRecipeSetup;
import com.unforbidable.tfc.bids.core.features.setup.registry.MapRegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;

import java.util.List;

public class FeatureSetupParams {

    public final List<RegistryGroup<?>> lists;
    public final List<MapRegistryGroup<?, ?>> maps;
    public final List<OreGroup> ores;
    public final CraftingRecipeSetup crafting;
    public final List<Runnable> apply;
    public final List<EventHandlerSpec> handlers;

    public FeatureSetupParams(List<RegistryGroup<?>> lists, List<MapRegistryGroup<?, ?>> maps,
                              List<OreGroup> ores,
                              CraftingRecipeSetup crafting, List<Runnable> apply,
                              List<EventHandlerSpec> handlers) {
        this.lists = lists;
        this.maps = maps;
        this.ores = ores;
        this.crafting = crafting;
        this.apply = apply;
        this.handlers = handlers;
    }

}
