package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpecCollector;
import com.unforbidable.tfc.bids.core.features.setup.fluidcontainer.FluidContainerGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.help.HelpGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.network.NetworkSetupHelper;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.recipe.CraftingRecipeSetupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.registry.MapRegistryGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.worldgen.WorldGenSpecCollector;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.MapRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

public class FeatureSetupBuilder {

    private final List<RegistryGroupBuilder<?>> lists = new ArrayList<>();
    private final List<MapRegistryGroupBuilder<?, ?>> maps = new ArrayList<>();
    private final List<OreGroupBuilder> ores = new ArrayList<>();
    private final List<HelpGroupBuilder> help = new ArrayList<>();
    private final List<FluidContainerGroupBuilder> fluids = new ArrayList<>();
    private final CraftingRecipeSetupBuilder craftingRecipes = new CraftingRecipeSetupBuilder();
    private final NetworkSetupHelper network = new NetworkSetupHelper();
    private final List<Runnable> runs = new ArrayList<>();
    private final EventHandlerSpecCollector handlers = new EventHandlerSpecCollector();
    private final WorldGenSpecCollector generators = new WorldGenSpecCollector();

    public CraftingRecipeSetupBuilder recipes() {
        return craftingRecipes;
    }

    public <T> RegistryGroupBuilder<T> registry(ListRegistry<T> registry) {
        RegistryGroupBuilder<T> builder = new RegistryGroupBuilder<>(registry);
        lists.add(builder);

        return builder;
    }

    public <K, V> MapRegistryGroupBuilder<K, V> registry(MapRegistry<K, V> registry) {
        MapRegistryGroupBuilder<K, V> builder = new MapRegistryGroupBuilder<>(registry);
        maps.add(builder);

        return builder;
    }

    public OreGroupBuilder ores(String name) {
        OreGroupBuilder builder = new OreGroupBuilder(name);
        ores.add(builder);

        return builder;
    }

    public HelpGroupBuilder help(Item item) {
        return help(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public HelpGroupBuilder help(ItemStack itemStack) {
        HelpGroupBuilder builder = new HelpGroupBuilder(itemStack);
        this.help.add(builder);

        return builder;
    }

    public FluidContainerGroupBuilder fluid(Fluid fluid) {
        FluidContainerGroupBuilder builder = new FluidContainerGroupBuilder(fluid);
        fluids.add(builder);

        return builder;
    }

    public NetworkSetupHelper network() {
        return network;
    }

    public void run(Runnable apply) {
        runs.add(apply);
    }

    public EventHandlerSpecCollector event() {
        return handlers;
    }

    public WorldGenSpecCollector world() {
        return generators;
    }

    public FeatureSetupParams build() {
        return new FeatureSetupParams(
            lists.stream()
                .map(RegistryGroupBuilder::build)
                .collect(Collectors.toList()),
            maps.stream()
                .map(MapRegistryGroupBuilder::build)
                .collect(Collectors.toList()),
            ores.stream()
                .map(OreGroupBuilder::build)
                .collect(Collectors.toList()),
            help.stream()
                .map(HelpGroupBuilder::build)
                .collect(Collectors.toList()),
            fluids.stream()
                .map(FluidContainerGroupBuilder::build)
                .collect(Collectors.toList()),
            craftingRecipes.build(),
            runs,
            handlers.build(),
            generators.build()
        );
    }

}
