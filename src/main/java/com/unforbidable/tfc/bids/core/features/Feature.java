package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Feature {

    public void config(FeatureConfig config) {}

    /**
     * Block, item and fluid initialization, during which items are not registered, but may have been initialized by other features.
     * Use <code>FeatureRegistryLookup</code> to find objects initialized by other features.
     *
     * @param init Initialization builder instance
     * @param lookup FeatureRegistryLookup instance for finding objects initialized by other features
     */
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {}

    /**
     * Client only initialization, especially around rendering, and UI.
     * Implementing method is advised to use <code>SideOnly(Side.CLIENT)</code> attribute if implemented.
     * <code>BidsBlocks</code>, <code>BidsItems</code> and <code>BidsFluids</code> can be used freely.
     *
     * @param client Client initialization builder instance
     */
    @SideOnly(Side.CLIENT)
    public void client(FeatureClientSpecBuilder client) {}

    /**
     * Setup of ores, recipes and so on.
     * <code>BidsBlocks</code>, <code>BidsItems</code> and <code>BidsFluids</code> can be used freely.
     *
     * @param setup Setup builder instance
     */
    public void setup(FeatureSetupBuilder setup) {}

}
