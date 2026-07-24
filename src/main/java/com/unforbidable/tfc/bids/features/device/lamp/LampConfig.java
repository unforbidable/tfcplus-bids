package com.unforbidable.tfc.bids.features.device.lamp;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class LampConfig {

    // TODO use single config for fuel consumption multiplier, no config for light intensity

    public static float clayLampOliveOilLightLevel = 0.8f;
    public static float clayLampOliveOilConsumption = 0.25f;
    public static float clayLampFishOilLightLevel = 0.65f;
    public static float clayLampFishOilConsumption = 0.20f;
    public static float clayLampFlaxSeedOilLightLevel = 1f;
    public static float clayLampFlaxSeedOilConsumption = 0.3f;

    public static void load(FeatureConfig config) {
        clayLampOliveOilLightLevel = config.getFloat("clayLampOliveOilLightLevel",
            clayLampOliveOilLightLevel, 0.1f, 1f,
            "Higher values increase the light level of clay lamps consuming olive oil, 1f being equal to the light level of TFC metal oil lamps.");
        clayLampOliveOilConsumption = config.getFloat("clayLampOliveOilConsumption",
            clayLampOliveOilConsumption, 0.1f, 10f,
            "Higher values increase the olive oil consumption in clay lamps, 0.125f being equal to the olive oil consumption of TFC metal oil lamps.");
        clayLampFishOilLightLevel = config.getFloat("clayLampFishOilLightLevel",
            clayLampFishOilLightLevel, 0.1f, 1f,
            "Higher values increase the light level of clay lamps consuming fish oil, 1f being equal to the light level of TFC metal oil lamps.");
        clayLampFishOilConsumption = config.getFloat("clayLampFishOilConsumption",
            clayLampFishOilConsumption, 0.1f, 10f,
            "Higher values increase the fish oil consumption in clay lamps, 0.125f being equal to the olive oil consumption of TFC metal oil lamps.");
        clayLampFlaxSeedOilLightLevel = config.getFloat("clayLampFlaxSeedOilLightLevel",
            clayLampFlaxSeedOilLightLevel, 0.1f, 1f,
            "Higher values increase the light level of clay lamps consuming flax seed oil, 1f being equal to the light level of TFC metal oil lamps.");
        clayLampFlaxSeedOilConsumption = config.getFloat("clayLampFlaxSeedOilConsumption",
            clayLampFlaxSeedOilConsumption, 0.1f, 10f,
            "Higher values increase the flax seed oil consumption in clay lamps, 0.125f being equal to the olive oil consumption of TFC metal oil lamps.");
    }

}
