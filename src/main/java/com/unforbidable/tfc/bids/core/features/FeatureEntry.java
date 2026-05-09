package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpec;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfigContext;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpec;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupParams;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FeatureEntry {

    public final Feature feature;
    public final FeatureMetadata metadata;

    private FeatureInitSpec init;
    private FeatureClientSpec client;
    private FeatureSetupParams setup;

    public FeatureEntry(Feature feature, FeatureMetadata metadata) {
        this.feature = feature;
        this.metadata = metadata;
    }

    public FeatureInitSpec init(FeatureContext context) {
        if (init == null) {
            FeatureInitSpecBuilder builder = new FeatureInitSpecBuilder();
            feature.init(builder, context.lookup);
            init = builder.build();
        }

        return init;
    }

    @SideOnly(Side.CLIENT)
    public FeatureClientSpec client(FeatureContext context) {
        if (client == null) {
            FeatureClientSpecBuilder builder = new FeatureClientSpecBuilder();
            feature.client(builder);
            client = builder.build();
        }

        return client;
    }

    public FeatureSetupParams setup(FeatureContext context) {
        if (setup == null) {
            FeatureSetupBuilder builder = new FeatureSetupBuilder();
            feature.setup(builder);
            setup = builder.build();
        }

        return setup;
    }

    public void config(FeatureConfigContext context) {
        FeatureConfig config = new FeatureConfig(context.config, metadata.name);
        feature.config(config);
    }

}
