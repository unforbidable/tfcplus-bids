package com.unforbidable.tfc.bids.core.features.setup;

import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.recipe.CraftingRecipeSetupBuilder;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroupBuilder;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureSetupBuilder {

    private final List<RegistryGroupBuilder<?>> values = new ArrayList<>();
    private final List<OreGroupBuilder> ores = new ArrayList<>();
    private final CraftingRecipeSetupBuilder craftingRecipes = new CraftingRecipeSetupBuilder();
    private final List<Runnable> applies = new ArrayList<>();

    public CraftingRecipeSetupBuilder recipes() {
        return craftingRecipes;
    }

    public <T> RegistryGroupBuilder<T> registry(ListRegistry<T> registry) {
        RegistryGroupBuilder<T> builder = new RegistryGroupBuilder<>(registry);
        values.add(builder);

        return builder;
    }

    public OreGroupBuilder ores(String name) {
        OreGroupBuilder builder = new OreGroupBuilder(name);
        ores.add(builder);

        return builder;
    }

    public void apply(Runnable apply) {
        applies.add(apply);
    }

    public FeatureSetupParams build() {

        return new FeatureSetupParams(
            values.stream()
                .map(RegistryGroupBuilder::build)
                .collect(Collectors.toList()),
            ores.stream()
                .map(OreGroupBuilder::build)
                .collect(Collectors.toList()),
            craftingRecipes.build(),
            applies
        );
    }

}
