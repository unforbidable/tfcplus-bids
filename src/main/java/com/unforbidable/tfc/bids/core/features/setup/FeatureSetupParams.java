package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpec;
import com.unforbidable.tfc.bids.core.features.setup.fluidcontainer.FluidContainerGroup;
import com.unforbidable.tfc.bids.core.features.setup.help.HelpGroup;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.recipe.CraftingRecipeSetup;
import com.unforbidable.tfc.bids.core.features.setup.registry.MapRegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.worldgen.WorldGenSpec;
import java.util.List;

public class FeatureSetupParams {

    public final List<RegistryGroup<?>> lists;
    public final List<MapRegistryGroup<?, ?>> maps;
    public final List<OreGroup> ores;
    public final List<HelpGroup> help;
    public final List<FluidContainerGroup> fluids;
    public final CraftingRecipeSetup crafting;
    public final List<Runnable> runs;
    public final List<EventHandlerSpec> handlers;
    public final List<WorldGenSpec> generators;

    public FeatureSetupParams(List<RegistryGroup<?>> lists, List<MapRegistryGroup<?, ?>> maps,
                              List<OreGroup> ores, List<HelpGroup> help, List<FluidContainerGroup> fluids,
                              CraftingRecipeSetup crafting, List<Runnable> runs,
                              List<EventHandlerSpec> handlers, List<WorldGenSpec> generators) {
        this.lists = lists;
        this.maps = maps;
        this.ores = ores;
        this.help = help;
        this.fluids = fluids;
        this.crafting = crafting;
        this.runs = runs;
        this.handlers = handlers;
        this.generators = generators;
    }

}
