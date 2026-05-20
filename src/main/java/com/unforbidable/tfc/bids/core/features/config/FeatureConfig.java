package com.unforbidable.tfc.bids.core.features.config;

import net.minecraftforge.common.config.Configuration;

import java.util.function.Consumer;

public class FeatureConfig {

    private final Configuration config;
    private String category;

    public FeatureConfig(Configuration config, String category) {
        this.config = config;
        this.category = category;
    }

    public float getFloat(String name, float defaultValue, float minValue, float maxValue, String description) {
        return config.getFloat(name, category, defaultValue, minValue, maxValue, description);
    }

    public int getInt(String name, int defaultValue, int minValue, int maxValue, String description) {
        return config.getInt(name, category, defaultValue, minValue, maxValue, description);
    }

    public boolean getBoolean(String name, boolean defaultValue, String description) {
        return config.getBoolean(name, category, defaultValue, description);
    }

    public void using(Consumer<FeatureConfig> configurator) {
        configurator.accept(this);
    }

    public void using(Consumer<FeatureConfig> configurator, String category) {
        this.category = category;

        configurator.accept(this);
    }

}
