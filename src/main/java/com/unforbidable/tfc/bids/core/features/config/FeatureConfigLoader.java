package com.unforbidable.tfc.bids.core.features.config;

import com.unforbidable.tfc.bids.core.features.FeatureLoader;
import net.minecraftforge.common.config.Configuration;

public class FeatureConfigLoader {

    public static void load(Configuration config) {
        FeatureLoader loader = new FeatureLoader();
        FeatureConfigContext context = new FeatureConfigContext(config);

        loader.getFeatures()
            .forEach(f -> f.config(context));
    }

}
